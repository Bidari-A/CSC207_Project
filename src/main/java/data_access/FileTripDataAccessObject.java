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
 * Hotels, flights, and attractions are stored separately and loaded/saved via separate DAOs.
 */
public class FileTripDataAccessObject {
    private final File jsonFile;
    private final Map<String, Trip> trips = new HashMap<>();
    private FileHotelDataAccessObject hotelDAO;
    private FileFlightDataAccessObject flightDAO;
    private FileAttractionDataAccessObject attractionDAO;

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
     * Sets the hotel DAO for managing hotels separately.
     * @param hotelDAO the hotel DAO
     */
    public void setHotelDAO(FileHotelDataAccessObject hotelDAO) {
        this.hotelDAO = hotelDAO;
    }

    /**
     * Sets the flight DAO for managing flights separately.
     * @param flightDAO the flight DAO
     */
    public void setFlightDAO(FileFlightDataAccessObject flightDAO) {
        this.flightDAO = flightDAO;
    }

    /**
     * Sets the attraction DAO for managing attractions separately.
     * @param attractionDAO the attraction DAO
     */
    public void setAttractionDAO(FileAttractionDataAccessObject attractionDAO) {
        this.attractionDAO = attractionDAO;
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
     * Reads IDs from trips.json and loads full entities from separate DAOs.
     * Supports backward compatibility with inline JSON.
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

        // Parse hotel IDs
        List<String> hotelIds = new ArrayList<>();
        if (tripJson.has("hotelIds") && !tripJson.isNull("hotelIds")) {
            JSONArray hotelIdsArray = tripJson.getJSONArray("hotelIds");
            for (int i = 0; i < hotelIdsArray.length(); i++) {
                hotelIds.add(hotelIdsArray.getString(i));
            }
        }
        // Backward compatibility: migrate inline hotels
        else if (tripJson.has("hotels") && !tripJson.isNull("hotels")) {
            JSONArray hotelsArray = tripJson.getJSONArray("hotels");
            for (int i = 0; i < hotelsArray.length(); i++) {
                JSONObject hotelJson = hotelsArray.getJSONObject(i);
                String name = hotelJson.has("name") ? hotelJson.getString("name") : "";
                String address = hotelJson.has("address") ? hotelJson.getString("address") : "";
                float price = hotelJson.has("price") ? (float) hotelJson.getDouble("price") : 0.0f;
                Accommodation hotel = new Accommodation(null, name, address, price);
                if (hotelDAO != null) {
                    hotel = hotelDAO.save(hotel); // Save and get ID
                    hotelIds.add(hotel.getHotelId());
                }
            }
        }

        // Parse flight IDs
        List<String> flightIds = new ArrayList<>();
        if (tripJson.has("flightIds") && !tripJson.isNull("flightIds")) {
            JSONArray flightIdsArray = tripJson.getJSONArray("flightIds");
            for (int i = 0; i < flightIdsArray.length(); i++) {
                flightIds.add(flightIdsArray.getString(i));
            }
        }
        // Backward compatibility: migrate inline flights
        else if (tripJson.has("flights") && !tripJson.isNull("flights")) {
            JSONArray flightsArray = tripJson.getJSONArray("flights");
            for (int i = 0; i < flightsArray.length(); i++) {
                JSONObject flightJson = flightsArray.getJSONObject(i);
                String airlineName = flightJson.has("airlineName") ? flightJson.getString("airlineName") : "";
                String departureTimes = flightJson.has("departureTimes") ? flightJson.getString("departureTimes") : "";
                float price = flightJson.has("price") ? (float) flightJson.getDouble("price") : 0.0f;
                Flight flight = new Flight(null, airlineName, departureTimes, price);
                if (flightDAO != null) {
                    flight = flightDAO.save(flight); // Save and get ID
                    flightIds.add(flight.getFlightId());
                }
            }
        }

        // Parse attraction IDs
        List<String> attractionIds = new ArrayList<>();
        if (tripJson.has("attractionIds") && !tripJson.isNull("attractionIds")) {
            JSONArray attractionIdsArray = tripJson.getJSONArray("attractionIds");
            for (int i = 0; i < attractionIdsArray.length(); i++) {
                attractionIds.add(attractionIdsArray.getString(i));
            }
        }
        // Backward compatibility: migrate inline attractions
        else if (tripJson.has("attractions") && !tripJson.isNull("attractions")) {
            JSONArray attractionsArray = tripJson.getJSONArray("attractions");
            for (int i = 0; i < attractionsArray.length(); i++) {
                JSONObject attractionJson = attractionsArray.getJSONObject(i);
                String name = attractionJson.has("name") ? attractionJson.getString("name") : "";
                String address = attractionJson.has("address") ? attractionJson.getString("address") : "";
                String description = attractionJson.has("description") ? attractionJson.getString("description") : "";
                float price = attractionJson.has("price") ? (float) attractionJson.getDouble("price") : 0.0f;
                Destination attraction = new Destination(null, name, address, description, price);
                if (attractionDAO != null) {
                    attraction = attractionDAO.save(attraction); // Save and get ID
                    attractionIds.add(attraction.getAttractionId());
                }
            }
        }

        Trip trip = new Trip(tripId, tripName, ownerUserName, status, dates,
                destination, hotelIds, flightIds, attractionIds);
        
        // Load full objects from DAOs
        if (hotelDAO != null && !hotelIds.isEmpty()) {
            trip.setHotels(hotelDAO.getByIds(hotelIds));
        }
        if (flightDAO != null && !flightIds.isEmpty()) {
            trip.setFlights(flightDAO.getByIds(flightIds));
        }
        if (attractionDAO != null && !attractionIds.isEmpty()) {
            trip.setAttractions(attractionDAO.getByIds(attractionIds));
        }
        
        return trip;
    }


    /**
     * Saves trips to the JSON file.
     * Only saves IDs. Hotels/flights/attractions should already be saved in separate DAOs.
     */
    private void save() {
        try (FileWriter writer = new FileWriter(jsonFile)) {
            JSONObject jsonObject = new JSONObject();

            for (Trip trip : trips.values()) {
                String tripId = trip.getTripId();
                
                JSONObject tripJson = new JSONObject();
                tripJson.put("tripId", tripId);
                tripJson.put("tripName", trip.getTripName());
                tripJson.put("ownerUserName", trip.getOwnerUserName());
                tripJson.put("status", trip.getStatus());
                tripJson.put("dates", trip.getDates());

                // Save destination (String - main destination location)
                tripJson.put("destination", trip.getDestination() != null ? trip.getDestination() : "");

                // Save hotel/flight/attraction IDs only
                JSONArray hotelIdsArray = new JSONArray();
                for (String hotelId : trip.getHotelIds()) {
                    hotelIdsArray.put(hotelId);
                }
                tripJson.put("hotelIds", hotelIdsArray);

                JSONArray flightIdsArray = new JSONArray();
                for (String flightId : trip.getFlightIds()) {
                    flightIdsArray.put(flightId);
                }
                tripJson.put("flightIds", flightIdsArray);

                JSONArray attractionIdsArray = new JSONArray();
                for (String attractionId : trip.getAttractionIds()) {
                    attractionIdsArray.put(attractionId);
                }
                tripJson.put("attractionIds", attractionIdsArray);

                jsonObject.put(tripId, tripJson);
            }

            writer.write(jsonObject.toString(2)); // Pretty print with 2-space indent
        } catch (IOException ex) {
            throw new RuntimeException("Error saving trips to JSON file", ex);
        }
    }

    /**
     * Gets a trip by ID.
     * Loads full hotels/flights/attractions from separate DAOs using IDs.
     * @param tripId the trip ID
     * @return the Trip object with full entities loaded, or null if not found
     */
    public Trip get(String tripId) {
        Trip trip = trips.get(tripId);
        if (trip != null) {
            // Load full objects from DAOs using IDs
            if (hotelDAO != null && !trip.getHotelIds().isEmpty()) {
                trip.setHotels(hotelDAO.getByIds(trip.getHotelIds()));
            }
            if (flightDAO != null && !trip.getFlightIds().isEmpty()) {
                trip.setFlights(flightDAO.getByIds(trip.getFlightIds()));
            }
            if (attractionDAO != null && !trip.getAttractionIds().isEmpty()) {
                trip.setAttractions(attractionDAO.getByIds(trip.getAttractionIds()));
            }
        }
        return trip;
    }

    /**
     * Gets all trips for a user.
     * If hotels/flights/attractions are managed by separate DAOs, loads them from there.
     * @param username the username
     * @return list of trips owned by the user
     */
    public List<Trip> getTripsByUser(String username) {
        List<Trip> userTrips = new ArrayList<>();
        for (Trip trip : trips.values()) {
            if (trip.getOwnerUserName().equals(username)) {
                userTrips.add(get(trip.getTripId())); // Use get() which loads from separate DAOs
            }
        }
        return userTrips;
    }

    /**
     * Saves a trip (create or update).
     * Saves hotels/flights/attractions to separate DAOs and gets their IDs.
     * @param trip the trip to save (can have objects or IDs)
     * @return the trip with all IDs set
     */
    public Trip save(Trip trip) {
        String tripId = trip.getTripId();

        // Generate new ID if trip doesn't have one
        if (tripId == null || tripId.isEmpty() || tripId.trim().isEmpty()) {
            tripId = TripIdGenerator.nextId();
        }

        // Extract IDs from objects if objects are provided
        List<String> hotelIds = trip.getHotelIds();
        List<String> flightIds = trip.getFlightIds();
        List<String> attractionIds = trip.getAttractionIds();

        // If objects are provided, save them and get IDs
        if (hotelDAO != null && trip.getHotels() != null && !trip.getHotels().isEmpty()) {
            List<Accommodation> savedHotels = hotelDAO.saveAll(trip.getHotels());
            hotelIds = new ArrayList<>();
            for (Accommodation hotel : savedHotels) {
                hotelIds.add(hotel.getHotelId());
            }
            trip.setHotels(savedHotels);
        }

        if (flightDAO != null && trip.getFlights() != null && !trip.getFlights().isEmpty()) {
            List<Flight> savedFlights = flightDAO.saveAll(trip.getFlights());
            flightIds = new ArrayList<>();
            for (Flight flight : savedFlights) {
                flightIds.add(flight.getFlightId());
            }
            trip.setFlights(savedFlights);
        }

        if (attractionDAO != null && trip.getAttractions() != null && !trip.getAttractions().isEmpty()) {
            List<Destination> savedAttractions = attractionDAO.saveAll(trip.getAttractions());
            attractionIds = new ArrayList<>();
            for (Destination attraction : savedAttractions) {
                attractionIds.add(attraction.getAttractionId());
            }
            trip.setAttractions(savedAttractions);
        }

        // Create trip with IDs
        Trip tripWithIds = new Trip(tripId, trip.getTripName(), trip.getOwnerUserName(),
                trip.getStatus(), trip.getDates(), trip.getDestination(),
                hotelIds != null ? hotelIds : new ArrayList<>(),
                flightIds != null ? flightIds : new ArrayList<>(),
                attractionIds != null ? attractionIds : new ArrayList<>());
        tripWithIds.setHotels(trip.getHotels());
        tripWithIds.setFlights(trip.getFlights());
        tripWithIds.setAttractions(trip.getAttractions());

        trips.put(tripId, tripWithIds);
        save();
        return tripWithIds;
    }


    /**
     * Creates and saves a new trip with auto-generated ID.
     * Saves hotels/flights/attractions and gets their IDs.
     * @param tripName the name of the trip
     * @param ownerUserName the username of the trip owner
     * @param status the status of the trip (e.g., "CURRENT", "COMPLETED")
     * @param dates the date range string
     * @param destination the main destination location (from "To" field)
     * @param hotels list of hotels (will be saved and get IDs)
     * @param flights list of flights (will be saved and get IDs)
     * @param attractions list of Destination objects (will be saved and get IDs)
     * @return the created trip with auto-generated IDs for all entities
     */
    public Trip createTrip(String tripName, String ownerUserName, String status, String dates,
                           String destination, List<Accommodation> hotels,
                           List<Flight> flights, List<Destination> attractions) {
        String tripId = TripIdGenerator.nextId();
        
        // Create trip with empty IDs first - they'll be populated by save()
        Trip trip = new Trip(tripId, tripName, ownerUserName, status, dates,
                destination, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        trip.setHotels(hotels);
        trip.setFlights(flights);
        trip.setAttractions(attractions);
        
        return save(trip);
    }

    /**
     * Deletes a trip by ID.
     * Also deletes associated hotels/flights/attractions from separate DAOs by their IDs.
     * @param tripId the trip ID to delete
     * @return true if the trip was deleted, false if not found
     */
    public boolean delete(String tripId) {
        if (trips.containsKey(tripId)) {
            Trip trip = trips.get(tripId);
            // Delete associated entities from separate DAOs by their IDs
            if (hotelDAO != null && trip.getHotelIds() != null) {
                for (String hotelId : trip.getHotelIds()) {
                    hotelDAO.delete(hotelId);
                }
            }
            if (flightDAO != null && trip.getFlightIds() != null) {
                for (String flightId : trip.getFlightIds()) {
                    flightDAO.delete(flightId);
                }
            }
            if (attractionDAO != null && trip.getAttractionIds() != null) {
                for (String attractionId : trip.getAttractionIds()) {
                    attractionDAO.delete(attractionId);
                }
            }
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
     * Loads full hotels/flights/attractions from separate DAOs.
     * @return list of all trips with full entities loaded
     */
    public List<Trip> getAllTrips() {
        List<Trip> allTrips = new ArrayList<>();
        for (Trip trip : trips.values()) {
            allTrips.add(get(trip.getTripId())); // Use get() which loads full objects
        }
        return allTrips;
    }

    /**
     * Updates the status of a trip.
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
                trip.getHotelIds(),
                trip.getFlightIds(),
                trip.getAttractionIds()
        );
        updatedTrip.setHotels(trip.getHotels());
        updatedTrip.setFlights(trip.getFlights());
        updatedTrip.setAttractions(trip.getAttractions());
        return save(updatedTrip);
    }

    public List<Trip> getTripsByUserAndStatus(String username, String status) {
        List<Trip> userTrips = new ArrayList<>();
        for (Trip trip: trips.values()){
            if (trip.getOwnerUserName().equals(username) && trip.getStatus().equals(status)){
                userTrips.add(get(trip.getTripId())); // Use get() which loads full objects
            }
        }
        return userTrips;
    }

    /**
     * Migrates existing trips with inline hotels/flights/attractions to separate DAOs.
     * Should be called after DAOs are set to migrate data from trips.json.
     * This will save all trips, which triggers saving hotels/flights/attractions to separate DAOs.
     */
    public void migrateToSeparateDAOs() {
        if (hotelDAO == null && flightDAO == null && attractionDAO == null) {
            return; // No DAOs set, nothing to migrate to
        }

        // Save all trips - this will save hotels/flights/attractions to separate DAOs
        // and update trips.json to remove inline data
        save();
    }
}
