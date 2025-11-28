package data_access;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.json.JSONTokener;

import entity.Flight;
import entity.FlightIdGenerator;

/**
 * DAO for flight data implemented using a JSON file.
 * Stores flights with IDs as keys.
 */
public class FileFlightDataAccessObject {
    private final File jsonFile;
    private final Map<String, Flight> flights = new HashMap<>();

    /**
     * Construct this DAO for saving to and reading from a local JSON file.
     * @param jsonPath the path of the JSON file to save to
     */
    public FileFlightDataAccessObject(String jsonPath) {
        this.jsonFile = new File(jsonPath);

        if (jsonFile.exists() && jsonFile.length() > 0) {
            load();
        } else {
            save();
        }
    }

    /**
     * Loads flights from the JSON file.
     */
    private void load() {
        try (FileReader reader = new FileReader(jsonFile)) {
            JSONObject jsonObject = new JSONObject(new JSONTokener(reader));

            List<String> flightIds = new ArrayList<>();
            for (String flightId : jsonObject.keySet()) {
                flightIds.add(flightId);
                JSONObject flightJson = jsonObject.getJSONObject(flightId);
                String airlineName = flightJson.has("airlineName") ? flightJson.getString("airlineName") : "";
                String departureTimes = flightJson.has("departureTimes") ? flightJson.getString("departureTimes") : "";
                float price = flightJson.has("price") ? (float) flightJson.getDouble("price") : 0.0f;
                flights.put(flightId, new Flight(flightId, airlineName, departureTimes, price));
            }

            // Initialize ID generator
            FlightIdGenerator.initializeFromExistingIds(flightIds);
        } catch (IOException ex) {
            throw new RuntimeException("Error loading flights from JSON file", ex);
        }
    }

    /**
     * Saves flights to the JSON file.
     */
    private void save() {
        try (FileWriter writer = new FileWriter(jsonFile)) {
            JSONObject jsonObject = new JSONObject();

            for (Map.Entry<String, Flight> entry : flights.entrySet()) {
                String flightId = entry.getKey();
                Flight flight = entry.getValue();

                JSONObject flightJson = new JSONObject();
                flightJson.put("flightId", flightId);
                flightJson.put("airlineName", flight.getAirlineName());
                flightJson.put("departureTimes", flight.getDepartureTimes());
                flightJson.put("price", flight.getPrice());

                jsonObject.put(flightId, flightJson);
            }

            writer.write(jsonObject.toString(2));
        } catch (IOException ex) {
            throw new RuntimeException("Error saving flights to JSON file", ex);
        }
    }

    /**
     * Gets a flight by ID.
     * @param flightId the flight ID
     * @return the Flight object, or null if not found
     */
    public Flight get(String flightId) {
        return flights.get(flightId);
    }

    /**
     * Gets flights by their IDs.
     * @param flightIds list of flight IDs
     * @return list of Flight objects
     */
    public List<Flight> getByIds(List<String> flightIds) {
        List<Flight> result = new ArrayList<>();
        for (String flightId : flightIds) {
            Flight flight = flights.get(flightId);
            if (flight != null) {
                result.add(flight);
            }
        }
        return result;
    }

    /**
     * Saves a flight. Generates ID if not provided.
     * @param flight the flight to save
     * @return the saved flight with ID
     */
    public Flight save(Flight flight) {
        String flightId = flight.getFlightId();
        if (flightId == null || flightId.isEmpty()) {
            flightId = FlightIdGenerator.nextId();
            flight = new Flight(flightId, flight.getAirlineName(), flight.getDepartureTimes(), flight.getPrice());
        }
        flights.put(flightId, flight);
        save();
        return flight;
    }

    /**
     * Saves multiple flights.
     * @param flights list of flights to save
     * @return list of saved flights with IDs
     */
    public List<Flight> saveAll(List<Flight> flights) {
        List<Flight> saved = new ArrayList<>();
        for (Flight flight : flights) {
            saved.add(save(flight));
        }
        return saved;
    }

    /**
     * Deletes a flight by ID.
     * @param flightId the flight ID
     */
    public void delete(String flightId) {
        flights.remove(flightId);
        save();
    }

    /**
     * Gets all flights.
     * @return list of all flights
     */
    public List<Flight> getAll() {
        return new ArrayList<>(flights.values());
    }
}
