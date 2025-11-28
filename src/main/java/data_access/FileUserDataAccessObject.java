package data_access;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import entity.Trip;
import entity.User;
import entity.UserFactory;
import use_case.change_password.ChangePasswordUserDataAccessInterface;
import use_case.load_trip_detail.LoadTripDetailDataAccessInterface;
import use_case.load_trip_list.LoadTripListUserDataAccessInterface;
import use_case.login.LoginUserDataAccessInterface;
import use_case.logout.LogoutUserDataAccessInterface;
import use_case.signup.SignupUserDataAccessInterface;

/**
 * DAO for user data implemented using a JSON file to persist the data.
 */
public class FileUserDataAccessObject implements SignupUserDataAccessInterface,
        LoginUserDataAccessInterface,
        ChangePasswordUserDataAccessInterface,
        LogoutUserDataAccessInterface,
        LoadTripListUserDataAccessInterface,
        LoadTripDetailDataAccessInterface {

    private final File jsonFile;
    private final Map<String, User> accounts = new HashMap<>();
    private final UserFactory userFactory;
    private String currentUsername;
    private FileTripDataAccessObject tripDataAccessObject; // Reference to trip DAO

    /**
     * Construct this DAO for saving to and reading from a local JSON file.
     * @param jsonPath the path of the JSON file to save to
     * @param userFactory factory for creating user objects
     * @throws RuntimeException if there is an IOException when accessing the file
     */
    public FileUserDataAccessObject(String jsonPath, UserFactory userFactory) {
        this.jsonFile = new File(jsonPath);
        this.userFactory = userFactory;

        if (jsonFile.exists() && jsonFile.length() > 0) {
            load();
        } else {
            // Create empty JSON file if it doesn't exist
            save();
        }
    }

    /**
     * Loads users from the JSON file.
     */
    private void load() {
        try (FileReader reader = new FileReader(jsonFile)) {
            JSONObject jsonObject = new JSONObject(new JSONTokener(reader));

            for (String key : jsonObject.keySet()) {
                JSONObject userJson = jsonObject.getJSONObject(key);
                String username = userJson.getString("username");
                String password = userJson.getString("password");
                String currentTripId = userJson.has("currentTripId") && !userJson.isNull("currentTripId")
                        ? userJson.getString("currentTripId") : null;

                List<String> tripList = new ArrayList<>();
                if (userJson.has("tripList") && !userJson.isNull("tripList")) {
                    JSONArray tripListArray = userJson.getJSONArray("tripList");
                    for (int i = 0; i < tripListArray.length(); i++) {
                        tripList.add(tripListArray.getString(i));
                    }
                }

                User user = userFactory.create(username, password, currentTripId, tripList);
                accounts.put(username, user);
            }
        } catch (IOException ex) {
            throw new RuntimeException("Error loading users from JSON file", ex);
        }
    }

    /**
     * Saves users to the JSON file.
     */
    private void save() {
        try (FileWriter writer = new FileWriter(jsonFile)) {
            JSONObject jsonObject = new JSONObject();

            for (User user : accounts.values()) {
                JSONObject userJson = new JSONObject();
                userJson.put("username", user.getUsername());
                userJson.put("password", user.getPassword());
                userJson.put("currentTripId", user.getCurrentTripId() != null ? user.getCurrentTripId() : JSONObject.NULL);

                JSONArray tripListArray = new JSONArray();
                for (String tripId : user.getTripList()) {
                    tripListArray.put(tripId);
                }
                userJson.put("tripList", tripListArray);

                // Use username as key to match JSON format
                jsonObject.put(user.getUsername(), userJson);
            }

            writer.write(jsonObject.toString(2)); // Pretty print with 2-space indent
        } catch (IOException ex) {
            throw new RuntimeException("Error saving users to JSON file", ex);
        }
    }

    @Override
    public void save(User user) {
        accounts.put(user.getUsername(), user);
        save();
    }

    @Override
    public User get(String username) {
        return accounts.get(username);
    }

    @Override
    public void setCurrentUsername(String name) {
        currentUsername = name;
    }

    @Override
    public String getCurrentUsername() {
        return currentUsername;
    }

    @Override
    public boolean existsByName(String identifier) {
        return accounts.containsKey(identifier);
    }

    @Override
    public void changePassword(User user) {
        // Replace the User object in the map
        accounts.put(user.getUsername(), user);
        save();
    }

    /**
     * Sets the trip data access object for integration.
     * @param tripDataAccessObject the trip DAO
     */
    public void setTripDataAccessObject(FileTripDataAccessObject tripDataAccessObject) {
        this.tripDataAccessObject = tripDataAccessObject;
    }

    /**
     * Gets trips for a user.
     * @param username the username
     * @return list of trips for the user
     */
    @Override
    public List<Trip> getTrips(String username) {
        if (tripDataAccessObject != null) {
            return tripDataAccessObject.getTripsByUser(username);
        }
        return new ArrayList<>();
    }

    /**
     * Gets the current user name.
     * @return the current user name
     */
    public String getCurrentUserName() {
        return currentUsername;
    }

    /**
     * Gets a trip by its ID.
     * @param tripId the trip ID
     * @return the Trip object, or null if not found
     */
    @Override
    public Trip getTrip(String tripId) {
        if (tripDataAccessObject != null) {
            return tripDataAccessObject.get(tripId);
        }
        return null;
    }
}
