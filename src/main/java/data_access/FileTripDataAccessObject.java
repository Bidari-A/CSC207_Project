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

import entity.Accommodation;
import entity.Destination;
import entity.Flight;
import entity.Trip;
import entity.TripIdGenerator;
import use_case.create_new_trip.CreateNewTripTripDataAccessInterface;

/**
 * DAO for trip data implemented using a JSON file to persist the data.
 * Supports full CRUD operations: create, read, update, delete.
 */
public class FileTripDataAccessObject
        implements data_access.TripDataAccessInterface, CreateNewTripTripDataAccessInterface {

    private final File jsonFile;
    private final Map<String, Trip> trips = new HashMap<>();

    private final String historyFilePath;
    private final String currentTripFilePath;

    /**
     * Simple constructor: only JSON path, uses default history/current paths.
     */
    public FileTripDataAccessObject(String jsonPath) {
        this(jsonPath, "data/trip_history.txt", "data/current_trip.txt");
    }

    /**
     * Full constructor: JSON + history + current trip paths.
     */
    public FileTripDataAccessObject(String tripJsonPath, String historyPath, String currentTripPath) {
        this.jsonFile = new File(tripJsonPath);
        this.historyFilePath = historyPath;
        this.currentTripFilePath = currentTripPath;

        if (jsonFile.exists() && jsonFile.length() > 0) {
            load();
        } else {
            // Create empty JSON structure on disk
            Trip trip = null;
            save();
        }
    }

    /**
     * Loads trips from the JSON file.
     */
    private void load() {
        try (FileReader reader = new FileReader(jsonFile)) {
            JSONObject jsonObject = new JSONObject(new JSONTokener(reader));

            // Collect all trip IDs to initialize TripIdGenerator
            List<String> tripIds = new ArrayList<>();

            for (String tripId : jsonObject.keySet()) {
                tripIds.add(tripId);
                JSONObject tripJson = jsonObject.getJSONObject(tripId);
                Trip trip = parseTripFromJson(tripJson);
                trips.put(tripId, trip);
            }

            // Initialize TripIdGenerator to avoid ID conflicts
            TripIdGenerator.initializeFromExistingIds(tripIds);
        } catch (IOException ex) {
            throw new RuntimeException("Error loading trips from JSON file", ex);
        }
    }

    /**
     * Parses a Trip object from JSON.
     */
    private Trip parseTripFromJson(JSONObject tripJson) {
        String tripId = tripJson.getString("tripId");
        String tripName = tripJson.getString("tripName");
        String ownerUserName = tripJson.getString("ownerUserName");
        String status = tripJson.getString("status");
        String dates = tripJson.getString("dates");

        // Parse destination (String - main destination location from "To" field)
        String destination = "";
        if (tripJson.has("destination") && !tripJson.isNull("destination")) {
            destination = tripJson.getString("destination");
        }

        // Parse hotels (accommodations) - read as objects from trips.json
        List<Accommodation> hotels = new ArrayList<>();
        if (tripJson.has("hotels") && !tripJson.isNull("hotels")) {
            JSONArray hotelsArray = tripJson.getJSONArray("hotels");
            for (int i = 0; i < hotelsArray.length(); i++) {
                JSONObject hotelJson = hotelsArray.getJSONObject(i);
                String name = hotelJson.optString("name", "");
                String address = hotelJson.optString("address", "");
                float price = (float) hotelJson.optDouble("price", 0.0);
                hotels.add(new Accommodation(name, address, price));
            }
        }

        // Parse flights - read as objects from trips.json
        List<Flight> flights = new ArrayList<>();
        if (tripJson.has("flights") && !tripJson.isNull("flights")) {
            JSONArray flightsArray = tripJson.getJSONArray("flights");
            for (int i = 0; i < flightsArray.length(); i++) {
                JSONObject flightJson = flightsArray.getJSONObject(i);
                String airlineName = flightJson.optString("airlineName", "");
                String departureTimes = flightJson.optString("departureTimes", "");
                float price = (float) flightJson.optDouble("price", 0.0);
                flights.add(new Flight(airlineName, departureTimes, price));
            }
        }

        // Parse attractions as Destination objects
        List<Destination> attractions = new ArrayList<>();
        if (tripJson.has("attractions") && !tripJson.isNull("attractions")) {
            JSONArray attractionsArray = tripJson.getJSONArray("attractions");
            for (int i = 0; i < attractionsArray.length(); i++) {
                JSONObject attractionJson = attractionsArray.getJSONObject(i);
                String name = attractionJson.optString("name", "");
                attractions.add(new Destination(name));
            }
        }

        return new Trip(tripId, tripName, ownerUserName, status, dates,
                destination, hotels, flights, attractions);
    }

    /**
     * Saves trips to the JSON file.
     */
    public void save() {
        try (FileWriter writer = new FileWriter(jsonFile)) {
            JSONObject jsonObject = new JSONObject();

            for (Trip trip : trips.values()) {
                JSONObject tripJson = new JSONObject();
                tripJson.put("tripId", trip.getTripId());
                tripJson.put("tripName", trip.getTripName());
                tripJson.put("ownerUserName", trip.getOwnerUserName());
                tripJson.put("status", trip.getStatus());
                tripJson.put("dates", trip.getDates());

                // Save destination (String - main destination location)
                tripJson.put("destination",
                        trip.getDestination() != null ? trip.getDestination() : "");

                // Save hotels as objects
                JSONArray hotelsArray = new JSONArray();
                for (Accommodation hotel : trip.getHotels()) {
                    JSONObject hotelJson = new JSONObject();
                    hotelJson.put("name", hotel.getName());
                    hotelJson.put("address", hotel.getAddress());
                    hotelJson.put("price", hotel.getPrice());
                    hotelsArray.put(hotelJson);
                }
                tripJson.put("hotels", hotelsArray);

                // Save flights as objects
                JSONArray flightsArray = new JSONArray();
                for (Flight flight : trip.getFlights()) {
                    JSONObject flightJson = new JSONObject();
                    flightJson.put("airlineName", flight.getAirlineName());
                    flightJson.put("departureTimes", flight.getDepartureTimes());
                    flightJson.put("price", flight.getPrice());
                    flightsArray.put(flightJson);
                }
                tripJson.put("flights", flightsArray);

                // Save attractions as Destination objects
                JSONArray attractionsArray = new JSONArray();
                for (Destination attraction : trip.getAttractions()) {
                    JSONObject attractionJson = new JSONObject();
                    attractionJson.put("name", attraction.getName());
                    attractionsArray.put(attractionJson);
                }
                tripJson.put("attractions", attractionsArray);

                jsonObject.put(trip.getTripId(), tripJson);
            }

            writer.write(jsonObject.toString(2)); // Pretty print with 2-space indent
        } catch (IOException ex) {
            throw new RuntimeException("Error saving trips to JSON file", ex);
        }
    }

    /**
     * TripDataAccessInterface / CreateNewTripTripDataAccessInterface:
     * Save a trip and return the stored version.
     */
    @Override
    public Trip saveTrip(Trip trip) {
        return saveInternal(trip);
    }

    /**
     * Internal save method that updates the map and writes to disk.
     */
    private Trip saveInternal(Trip trip) {
        String tripId = trip.getTripId();

        // Generate new ID if trip doesn't have one or has empty ID
        if (tripId == null || tripId.trim().isEmpty()) {
            tripId = TripIdGenerator.nextId();
            // Create new trip with generated ID
            trip = new Trip(tripId, trip.getTripName(), trip.getOwnerUserName(),
                    trip.getStatus(), trip.getDates(), trip.getDestination(),
                    trip.getHotels(), trip.getFlights(), trip.getAttractions());
        }

        trips.put(tripId, trip);
        save();
        return trip;
    }

    /**
     * Return a trip object by its ID.
     */
    @Override
    public Trip getTrip(String tripId) {
        return trips.get(tripId);
    }

    /**
     * Update a trip's data (or create if not exists).
     */
    @Override
    public void updateTrip(Trip trip) {
        trips.put(trip.getTripId(), trip);
        save();
    }

    /**
     * Gets all trips as a Collection (matches TripDataAccessInterface).
     */
    @Override
    public Trip[] getAllTrips() {
        return trips.values().toArray(new Trip[0]);
    }

    /**
     * Gets all trips for a given user.
     */
    public List<Trip> getTripsByUser(String username) {
        List<Trip> userTrips = new ArrayList<>();
        for (Trip trip : trips.values()) {
            if (trip.getOwnerUserName().equals(username)) {
                userTrips.add(trip);
            }
        }
        return userTrips;
    }

    /**
     * Creates and saves a new trip with auto-generated ID.
     * This is the preferred method for creating new trips.
     */
    public Trip createTrip(String tripName, String ownerUserName, String status, String dates,
                           String destination, List<Accommodation> hotels,
                           List<Flight> flights, List<Destination> attractions) {
        String tripId = TripIdGenerator.nextId();
        Trip trip = new Trip(tripId, tripName, ownerUserName, status, dates,
                destination, hotels, flights, attractions);
        return saveInternal(trip);
    }

    /**
     * Deletes a trip by ID.
     * @return true if the trip was deleted, false if not found
     */
    public boolean delete(String tripId) {
        if (trips.containsKey(tripId)) {
            trips.remove(tripId);
            save();
            return true;
        }
        return false;
    }

    /**
     * Checks if a trip exists.
     */
    public boolean exists(String tripId) {
        return trips.containsKey(tripId);
    }

    /**
     * Append trip ID to trip history file.
     */
    @Override
    public void addToHistory(String tripId) {
        try (FileWriter fw = new FileWriter(historyFilePath, true)) {
            fw.write(tripId + System.lineSeparator());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Clear the currently active trip.
     */
    @Override
    public void clearCurrentTrip() {
        try (FileWriter fw = new FileWriter(currentTripFilePath)) {
            fw.write(""); // Clears the file content
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Trip get(String tripId) {
        return trips.get(tripId);
    }
}