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

/**
 * DAO for trip data implemented using a JSON file to persist the data.
 * Supports full CRUD operations: create, read, update, delete.
 */
public class FileTripDataAccessObject {
    private final File jsonFile;
    private final Map<String, Trip> trips = new HashMap<>();

    /**
     * Construct this DAO for saving to and reading from a local JSON file.
     * @param jsonPath the path of the JSON file to save to
     */
    public FileTripDataAccessObject(String jsonPath) {
        this.jsonFile = new File(jsonPath);

        if (jsonFile.exists() && jsonFile.length() > 0) {
            load();
        } else {
            // Create empty JSON file if it doesn't exist
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
            java.util.List<String> tripIds = new ArrayList<>();

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
                String name = hotelJson.has("name") ? hotelJson.getString("name") : "";
                String address = hotelJson.has("address") ? hotelJson.getString("address") : "";
                float price = hotelJson.has("price") ? (float) hotelJson.getDouble("price") : 0.0f;
                hotels.add(new Accommodation(name, address, price));
            }
        }

        // Parse flights - read as objects from trips.json
        List<Flight> flights = new ArrayList<>();
        if (tripJson.has("flights") && !tripJson.isNull("flights")) {
            JSONArray flightsArray = tripJson.getJSONArray("flights");
            for (int i = 0; i < flightsArray.length(); i++) {
                JSONObject flightJson = flightsArray.getJSONObject(i);
                String airlineName = flightJson.has("airlineName") ? flightJson.getString("airlineName") : "";
                String departureTimes = flightJson.has("departureTimes") ? flightJson.getString("departureTimes") : "";
                float price = flightJson.has("price") ? (float) flightJson.getDouble("price") : 0.0f;
                flights.add(new Flight(airlineName, departureTimes, price));
            }
        }

        // Parse attractions as Destination objects
        List<Destination> attractions = new ArrayList<>();
        if (tripJson.has("attractions") && !tripJson.isNull("attractions")) {
            JSONArray attractionsArray = tripJson.getJSONArray("attractions");
            for (int i = 0; i < attractionsArray.length(); i++) {
                JSONObject attractionJson = attractionsArray.getJSONObject(i);
                String name = attractionJson.has("name") ? attractionJson.getString("name") : "";
                String address = attractionJson.has("address") ? attractionJson.getString("address") : "";
                String description = attractionJson.has("description") ? attractionJson.getString("description") : "";
                float price = attractionJson.has("price") ? (float) attractionJson.getDouble("price") : 0.0f;
                attractions.add(new Destination(name, address, description, price));
            }
        }

        return new Trip(tripId, tripName, ownerUserName, status, dates,
                destination, hotels, flights, attractions);
    }


    /**
     * Saves trips to the JSON file.
     */
    private void save() {
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
                tripJson.put("destination", trip.getDestination() != null ? trip.getDestination() : "");

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
                    attractionJson.put("address", attraction.getAddress());
                    attractionJson.put("description", attraction.getDescription());
                    attractionJson.put("price", attraction.getPrice());
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
     * Gets a trip by ID.
     * @param tripId the trip ID
     * @return the Trip object, or null if not found
     */
    public Trip get(String tripId) {
        return trips.get(tripId);
    }

    /**
     * Gets all trips for a user.
     * @param username the username
     * @return list of trips owned by the user
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
     * Saves a trip (create or update).
     * If the trip doesn't have an ID or has an empty ID, a new ID will be generated.
     * @param trip the trip to save
     * @return the trip with its ID (newly generated if it was missing)
     */
    public Trip save(Trip trip) {
        String tripId = trip.getTripId();

        // Generate new ID if trip doesn't have one or has empty ID
        if (tripId == null || tripId.isEmpty() || tripId.trim().isEmpty()) {
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
     * Creates and saves a new trip with auto-generated ID.
     * This is the preferred method for creating new trips.
     * @param tripName the name of the trip
     * @param ownerUserName the username of the trip owner
     * @param status the status of the trip (e.g., "CURRENT", "COMPLETED")
     * @param dates the date range string
     * @param destination the main destination location (from "To" field)
     * @param hotels list of hotels
     * @param flights list of flights
     * @param attractions list of Destination objects (attractions)
     * @return the created trip with auto-generated ID
     */
    public Trip createTrip(String tripName, String ownerUserName, String status, String dates,
                           String destination, List<Accommodation> hotels,
                           List<Flight> flights, List<Destination> attractions) {
        String tripId = TripIdGenerator.nextId();
        Trip trip = new Trip(tripId, tripName, ownerUserName, status, dates,
                destination, hotels, flights, attractions);
        return save(trip);
    }

    /**
     * Deletes a trip by ID.
     * @param tripId the trip ID to delete
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
     * @param tripId the trip ID
     * @return true if the trip exists, false otherwise
     */
    public boolean exists(String tripId) {
        return trips.containsKey(tripId);
    }

    /**
     * Gets all trips.
     * @return list of all trips
     */
    public List<Trip> getAllTrips() {
        return new ArrayList<>(trips.values());
    }

    /**
     * update the status of a trip.
     * Since the Trip Object is immutable, create a new Trip with updated status.
     * @param trip the trip to update
     * @param newStatus the new status (e.g.: "CURRENT"->"COMPLETED")
     * @return the updated trip
     */
    public Trip updateTripStatus(Trip trip, String newStatus) {
        Trip updatedTrip = new Trip(
                trip.getTripId(),
                trip.getTripName(),
                trip.getOwnerUserName(),
                newStatus,
                trip.getDates(),
                trip.getDestination(),
                trip.getHotels(),
                trip.getFlights(),
                trip.getAttractions()
        );
        return save(updatedTrip);
    }

    public List<Trip> getTripsByUserAndStatus(String username, String status) {
        List<Trip> userTrips = new ArrayList<>();
        for (Trip trip: trips.values()){
            if (trip.getOwnerUserName().equals(username) && trip.getStatus().equals(status)){
                userTrips.add(trip);
            }
        }
        return userTrips;
    }
}
