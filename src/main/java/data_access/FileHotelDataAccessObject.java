package data_access;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import entity.Accommodation;

/**
 * Data Access Object for reading hotels from JSON file.
 */
public class FileHotelDataAccessObject {
    private final Map<String, Accommodation> hotels;
    private final File jsonFile;

    public FileHotelDataAccessObject(String jsonPath) {
        this.jsonFile = new File(jsonPath);
        this.hotels = new HashMap<>();
        loadHotels();
    }

    private void loadHotels() {
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
                JSONObject hotelData = jsonObject.getJSONObject(key);
                String name = hotelData.getString("name");
                Accommodation accommodation = new Accommodation(name);
                hotels.put(key, accommodation);
            }
        } catch (IOException | JSONException e) {
            throw new RuntimeException("Error loading hotels from JSON: " + e.getMessage(), e);
        }
    }

    /**
     * Gets a hotel by its ID.
     * @param id the hotel ID
     * @return the Accommodation object, or null if not found
     */
    public Accommodation getHotel(String id) {
        return hotels.get(id);
    }

    /**
     * Gets all hotels.
     * @return a map of hotel IDs to Accommodation objects
     */
    public Map<String, Accommodation> getAllHotels() {
        return new HashMap<>(hotels);
    }

    /**
     * Gets a hotel by name (searches through all hotels).
     * @param name the hotel name
     * @return the Accommodation object, or null if not found
     */
    public Accommodation getHotelByName(String name) {
        for (Accommodation acc : hotels.values()) {
            if (acc.getName().equals(name)) {
                return acc;
            }
        }
        return null;
    }
}
