package data_access;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import entity.Accommodation;
import entity.Destination;
import entity.Flight;
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
 * DAO for user data implemented using a File to persist the data.
 */
public class FileUserDataAccessObject implements SignupUserDataAccessInterface,
        LoginUserDataAccessInterface,
        ChangePasswordUserDataAccessInterface,
        LogoutUserDataAccessInterface,
        LoadTripListUserDataAccessInterface,
        LoadTripDetailDataAccessInterface,
        use_case.complete_current_trip.CompleteCurrentTripDataAccessInterface,
        use_case.delete_trip.DeleteTripDataAccessInterface {

    private static final String HEADER = "username,password";

    private final File csvFile;
    private final File tripsJsonFile;
    private final Map<String, Integer> headers = new LinkedHashMap<>();
    private final Map<String, User> accounts = new HashMap<>();
    private final Map<String, List<Trip>> userTrips = new HashMap<>();

    private String currentUsername;

    /**
     * Construct this DAO for saving to and reading from a local file.
     * @param csvPath the path of the file to save to
     * @param userFactory factory for creating user objects
     * @throws RuntimeException if there is an IOException when accessing the file
     */
    public FileUserDataAccessObject(String csvPath, UserFactory userFactory) {
        this(csvPath, "userTrips.json", userFactory);
    }

    /**
     * Construct this DAO for saving to and reading from a local file.
     * @param csvPath the path of the file to save to
     * @param tripsJsonPath the path of the JSON file to save trips to
     * @param userFactory factory for creating user objects
     * @throws RuntimeException if there is an IOException when accessing the file
     */
    public FileUserDataAccessObject(String csvPath, String tripsJsonPath, UserFactory userFactory) {
        csvFile = new File(csvPath);
        tripsJsonFile = new File(tripsJsonPath);
        headers.put("username", 0);
        headers.put("password", 1);

        if (csvFile.length() == 0) {
            save();
        }
        else {
            try (BufferedReader reader = new BufferedReader(new FileReader(csvFile))) {
                final String header = reader.readLine();

                if (!header.equals(HEADER)) {
                    throw new RuntimeException(String.format("header should be%n: %s%n but was:%n%s", HEADER, header));
                }

                String row;
                while ((row = reader.readLine()) != null) {
                    final String[] col = row.split(",");
                    final String username = String.valueOf(col[headers.get("username")]);
                    final String password = String.valueOf(col[headers.get("password")]);
                    final User user = userFactory.create(username, password);
                    accounts.put(username, user);
                }
            }
            catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }

        // Load trips from JSON file
        loadTrips();
    }

    private void save() {
        final BufferedWriter writer;
        try {
            writer = new BufferedWriter(new FileWriter(csvFile));
            writer.write(String.join(",", headers.keySet()));
            writer.newLine();

            for (User user : accounts.values()) {
                final String line = String.format("%s,%s",
                        user.getName(), user.getPassword());
                writer.write(line);
                writer.newLine();
            }

            writer.close();

        }
        catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void save(User user) {
        accounts.put(user.getName(), user);
        this.save();
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
        accounts.put(user.getName(), user);
        save();
    }

    /**
     * Gets trips for a user.
     * @param username the username
     * @return list of trips for the user
     */
    public List<Trip> getTrips(String username){
        List<Trip> trips = userTrips.get(username);
        return trips != null ? new ArrayList<>(trips): new ArrayList<>();
    }

    /**
     * Gets the current user name.
     * @return the current user name
     */
    public String getCurrentUserName() {
        return "";
    }

    /**
     * Deletes a trip for a user.
     * @param username the username
     * @param tripName the name of the trip to delete
     * @return true if the trip was deleted, false otherwise
     */
    public boolean deleteTrip(String username, String tripName) {
        List<Trip> trips = userTrips.get(username);
        if (trips != null) {
            boolean removed = trips.removeIf(trip -> trip.getName().equals(tripName));
            if (removed) {
                saveTrips(); // Save to file after deletion
            }
            return removed;
        }
        return false;
    }

    /**
     * Adds a trip for a user. This is a helper method that can be used when creating trips.
     * @param username the username
     * @param trip the trip to add
     */
    public void addTrip(String username, Trip trip) {
        userTrips.computeIfAbsent(username, k -> new ArrayList<>()).add(trip);
        saveTrips(); // Save to file immediately
    }

    /**
     * Gets a trip by name for a user.
     * @param username the username
     * @param tripName the name of the trip
     * @return the trip if found, null otherwise
     */
    public Trip getTripByName(String username, String tripName) {
        List<Trip> trips = userTrips.get(username);
        if (trips != null) {
            for (Trip trip : trips) {
                if (trip.getName().equals(tripName)) {
                    return trip;
                }
            }
        }
        return null;
    }

    /**
     * Loads trips from JSON file.
     */
    private void loadTrips() {
        if (!tripsJsonFile.exists() || tripsJsonFile.length() == 0) {
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(tripsJsonFile))) {
            StringBuilder jsonContent = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonContent.append(line);
            }

            JSONObject jsonObject = new JSONObject(jsonContent.toString());

            for (String username : jsonObject.keySet()) {
                JSONObject userData = jsonObject.getJSONObject(username);
                JSONArray tripsArray = userData.getJSONArray("trips");

                List<Trip> trips = new ArrayList<>();
                for (int i = 0; i < tripsArray.length(); i++) {
                    JSONObject tripJson = tripsArray.getJSONObject(i);
                    Trip trip = tripFromJSON(tripJson);
                    if (trip != null) {
                        trips.add(trip);
                    }
                }
                userTrips.put(username, trips);
            }
        } catch (IOException | JSONException e) {
            // If file doesn't exist or is invalid, start with empty trips
            System.err.println("Warning: Could not load trips from file: " + e.getMessage());
        }
    }

    /**
     * Saves trips to JSON file.
     */
    private void saveTrips() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(tripsJsonFile))) {
            JSONObject jsonObject = new JSONObject();

            for (Map.Entry<String, List<Trip>> entry : userTrips.entrySet()) {
                String username = entry.getKey();
                List<Trip> trips = entry.getValue();

                JSONObject userData = new JSONObject();
                JSONArray tripsArray = new JSONArray();

                for (Trip trip : trips) {
                    tripsArray.put(tripToJSON(trip));
                }

                userData.put("trips", tripsArray);
                jsonObject.put(username, userData);
            }

            writer.write(jsonObject.toString(2)); // Pretty print with 2-space indent
            writer.flush();
        } catch (IOException | JSONException e) {
            throw new RuntimeException("Error saving trips to file: " + e.getMessage(), e);
        }
    }

    /**
     * Converts a Trip entity to JSON.
     */
    private JSONObject tripToJSON(Trip trip) {
        JSONObject tripJson = new JSONObject();
        tripJson.put("name", trip.getName());
        tripJson.put("cityName", trip.getCityName());

        // Serialize dates
        JSONArray datesArray = new JSONArray();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for (Date date : trip.getTripDates()) {
            datesArray.put(sdf.format(date));
        }
        tripJson.put("dates", datesArray);

        // Serialize destinations
        JSONArray destArray = new JSONArray();
        for (Destination dest : trip.getDestinations()) {
            JSONObject destJson = new JSONObject();
            destJson.put("name", dest.getName());
            destJson.put("address", dest.getAddress());
            destJson.put("description", dest.getDescription());
            destJson.put("price", dest.getPrice());
            destArray.put(destJson);
        }
        tripJson.put("destinations", destArray);

        // Serialize accommodations
        JSONArray accArray = new JSONArray();
        for (Accommodation acc : trip.getAccommodations()) {
            JSONObject accJson = new JSONObject();
            accJson.put("name", acc.getName());
            accJson.put("address", acc.getAddress());
            accJson.put("price", acc.getPrice());
            accArray.put(accJson);
        }
        tripJson.put("accommodations", accArray);

        // Serialize flights
        JSONArray flightArray = new JSONArray();
        for (Flight flight : trip.getFlights()) {
            JSONObject flightJson = new JSONObject();
            flightJson.put("airlineName", flight.getAirlineName());
            if (flight.getDeparture() != null && !flight.getDeparture().isEmpty()) {
                flightJson.put("departure", flight.getDeparture());
                flightJson.put("arrival", flight.getArrival());
                flightJson.put("date", flight.getDate());
            } else if (flight.getDepartureTimes() != null) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                flightJson.put("departureTime", dateFormat.format(flight.getDepartureTimes()));
                flightJson.put("price", flight.getPrice());
            }
            flightArray.put(flightJson);
        }
        tripJson.put("flights", flightArray);

        return tripJson;
    }

    /**
     * Converts JSON to a Trip entity.
     */
    private Trip tripFromJSON(JSONObject tripJson) {
        try {
            String name = tripJson.getString("name");
            String cityName = tripJson.getString("cityName");

            // Parse dates
            List<Date> tripDates = new ArrayList<>();
            if (tripJson.has("dates") && !tripJson.isNull("dates")) {
                JSONArray datesArray = tripJson.getJSONArray("dates");
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                for (int i = 0; i < datesArray.length(); i++) {
                    try {
                        Date date = sdf.parse(datesArray.getString(i));
                        tripDates.add(date);
                    } catch (Exception e) {
                        // Skip invalid dates
                    }
                }
            }

            // Parse destinations
            List<Destination> destinations = new ArrayList<>();
            if (tripJson.has("destinations") && !tripJson.isNull("destinations")) {
                JSONArray destArray = tripJson.getJSONArray("destinations");
                for (int i = 0; i < destArray.length(); i++) {
                    JSONObject destJson = destArray.getJSONObject(i);
                    String destName = destJson.getString("name");
                    String address = destJson.optString("address", "");
                    String description = destJson.optString("description", "");
                    float price = (float) destJson.optDouble("price", 0.0);
                    destinations.add(new Destination(destName, address, description, price));
                }
            }

            // Parse accommodations
            List<Accommodation> accommodations = new ArrayList<>();
            if (tripJson.has("accommodations") && !tripJson.isNull("accommodations")) {
                JSONArray accArray = tripJson.getJSONArray("accommodations");
                for (int i = 0; i < accArray.length(); i++) {
                    JSONObject accJson = accArray.getJSONObject(i);
                    String accName = accJson.getString("name");
                    String address = accJson.optString("address", "");
                    float price = (float) accJson.optDouble("price", 0.0);
                    accommodations.add(new Accommodation(accName, address, price));
                }
            }

            // Parse flights
            List<Flight> flights = new ArrayList<>();
            if (tripJson.has("flights") && !tripJson.isNull("flights")) {
                JSONArray flightArray = tripJson.getJSONArray("flights");
                for (int i = 0; i < flightArray.length(); i++) {
                    JSONObject flightJson = flightArray.getJSONObject(i);
                    String airline = flightJson.getString("airlineName");
                    String departure = flightJson.optString("departure", "");
                    String arrival = flightJson.optString("arrival", "");
                    String date = flightJson.optString("date", "");
                    if (!departure.isEmpty() && !arrival.isEmpty()) {
                        flights.add(new Flight(airline, departure, arrival, date));
                    } else {
                        // Legacy format with Date
                        Date departureTime = null;
                        if (flightJson.has("departureTime") && !flightJson.isNull("departureTime")) {
                            try {
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                departureTime = sdf.parse(flightJson.getString("departureTime"));
                            } catch (Exception e) {
                                // Skip invalid dates
                            }
                        }
                        float price = (float) flightJson.optDouble("price", 0.0);
                        flights.add(new Flight(airline, departureTime, price));
                    }
                }
            }

            return new Trip(name, cityName, tripDates, destinations, accommodations, flights);
        } catch (JSONException e) {
            System.err.println("Error parsing trip from JSON: " + e.getMessage());
            return null;
        }
    }
}
