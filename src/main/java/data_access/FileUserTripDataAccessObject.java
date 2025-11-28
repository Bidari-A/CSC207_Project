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

/**
 * DAO for user-trip relationship data implemented using a JSON file.
 * Stores which trips belong to which users.
 */
public class FileUserTripDataAccessObject {
    private final File jsonFile;
    private final Map<String, List<String>> userTripMap = new HashMap<>(); // username -> list of tripIds

    /**
     * Construct this DAO for saving to and reading from a local JSON file.
     * @param jsonPath the path of the JSON file to save to
     */
    public FileUserTripDataAccessObject(String jsonPath) {
        this.jsonFile = new File(jsonPath);

        if (jsonFile.exists() && jsonFile.length() > 0) {
            load();
        } else {
            save();
        }
    }

    /**
     * Loads user-trip relationships from the JSON file.
     */
    private void load() {
        try (FileReader reader = new FileReader(jsonFile)) {
            JSONObject jsonObject = new JSONObject(new JSONTokener(reader));

            for (String username : jsonObject.keySet()) {
                JSONArray tripIdsArray = jsonObject.getJSONArray(username);
                List<String> tripIds = new ArrayList<>();

                for (int i = 0; i < tripIdsArray.length(); i++) {
                    tripIds.add(tripIdsArray.getString(i));
                }

                userTripMap.put(username, tripIds);
            }
        } catch (IOException ex) {
            throw new RuntimeException("Error loading user-trip data from JSON file", ex);
        }
    }

    /**
     * Saves user-trip relationships to the JSON file.
     */
    private void save() {
        try (FileWriter writer = new FileWriter(jsonFile)) {
            JSONObject jsonObject = new JSONObject();

            for (Map.Entry<String, List<String>> entry : userTripMap.entrySet()) {
                String username = entry.getKey();
                List<String> tripIds = entry.getValue();

                JSONArray tripIdsArray = new JSONArray();
                for (String tripId : tripIds) {
                    tripIdsArray.put(tripId);
                }

                jsonObject.put(username, tripIdsArray);
            }

            writer.write(jsonObject.toString(2));
        } catch (IOException ex) {
            throw new RuntimeException("Error saving user-trip data to JSON file", ex);
        }
    }

    /**
     * Gets all trip IDs for a specific user.
     * @param username the username
     * @return list of trip IDs for the user
     */
    public List<String> getTripIdsByUser(String username) {
        return new ArrayList<>(userTripMap.getOrDefault(username, new ArrayList<>()));
    }

    /**
     * Adds a trip to a user's trip list.
     * @param username the username
     * @param tripId the trip ID to add
     */
    public void addTripToUser(String username, String tripId) {
        List<String> tripIds = userTripMap.computeIfAbsent(username, k -> new ArrayList<>());
        if (!tripIds.contains(tripId)) {
            tripIds.add(tripId);
            save();
        }
    }

    /**
     * Removes a trip from a user's trip list.
     * @param username the username
     * @param tripId the trip ID to remove
     */
    public void removeTripFromUser(String username, String tripId) {
        List<String> tripIds = userTripMap.get(username);
        if (tripIds != null) {
            tripIds.remove(tripId);
            save();
        }
    }

    /**
     * Sets the trip list for a user (replaces existing list).
     * @param username the username
     * @param tripIds list of trip IDs
     */
    public void setTripListForUser(String username, List<String> tripIds) {
        userTripMap.put(username, new ArrayList<>(tripIds));
        save();
    }

    /**
     * Checks if a user has a specific trip.
     * @param username the username
     * @param tripId the trip ID
     * @return true if the user has the trip, false otherwise
     */
    public boolean userHasTrip(String username, String tripId) {
        List<String> tripIds = userTripMap.get(username);
        return tripIds != null && tripIds.contains(tripId);
    }
}

