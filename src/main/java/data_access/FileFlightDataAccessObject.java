package data_access;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import entity.Flight;

/**
 * Data Access Object for reading flights from JSON file.
 */
public class FileFlightDataAccessObject {
    private final Map<String, Flight> flights;
    private final File jsonFile;

    public FileFlightDataAccessObject(String jsonPath) {
        this.jsonFile = new File(jsonPath);
        this.flights = new HashMap<>();
        loadFlights();
    }

    private void loadFlights() {
        if (!jsonFile.exists() || jsonFile.length() == 0) {
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(jsonFile))) {
            StringBuilder jsonContent = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonContent.append(line);
            }

            JSONObject jsonObject = new JSONObject(jsonContent.toString());

            for (String key : jsonObject.keySet()) {
                JSONObject flightData = jsonObject.getJSONObject(key);
                String airline = flightData.getString("airline");
                String departure = flightData.getString("departure");
                String arrival = flightData.getString("arrival");
                String date = flightData.getString("date");
                Flight flight = new Flight(airline, departure, arrival, date);
                flights.put(key, flight);
            }
        } catch (IOException | JSONException e) {
            throw new RuntimeException("Error loading flights from JSON: " + e.getMessage(), e);
        }
    }

    /**
     * Gets a flight by its ID.
     * @param id the flight ID
     * @return the Flight object, or null if not found
     */
    public Flight getFlight(String id) {
        return flights.get(id);
    }

    /**
     * Gets all flights.
     * @return a map of flight IDs to Flight objects
     */
    public Map<String, Flight> getAllFlights() {
        return new HashMap<>(flights);
    }

    /**
     * Gets flights by airline name.
     * @param airlineName the airline name
     * @return a map of flight IDs to Flight objects matching the airline
     */
    public Map<String, Flight> getFlightsByAirline(String airlineName) {
        Map<String, Flight> result = new HashMap<>();
        for (Map.Entry<String, Flight> entry : flights.entrySet()) {
            if (entry.getValue().getAirlineName().equals(airlineName)) {
                result.put(entry.getKey(), entry.getValue());
            }
        }
        return result;
    }
}
