package data_access;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import entity.Destination;

/**
 * Data Access Object for reading attractions from JSON file.
 */
public class FileAttractionDataAccessObject {
    private final Map<String, Destination> attractions;
    private final File jsonFile;

    public FileAttractionDataAccessObject(String jsonPath) {
        this.jsonFile = new File(jsonPath);
        this.attractions = new HashMap<>();
        loadAttractions();
    }

    private void loadAttractions() {
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
                JSONObject attractionData = jsonObject.getJSONObject(key);
                String name = attractionData.getString("name");
                Destination destination = new Destination(name);
                attractions.put(key, destination);
            }
        } catch (IOException | JSONException e) {
            throw new RuntimeException("Error loading attractions from JSON: " + e.getMessage(), e);
        }
    }

    /**
     * Gets an attraction by its ID.
     * @param id the attraction ID
     * @return the Destination object, or null if not found
     */
    public Destination getAttraction(String id) {
        return attractions.get(id);
    }

    /**
     * Gets all attractions.
     * @return a map of attraction IDs to Destination objects
     */
    public Map<String, Destination> getAllAttractions() {
        return new HashMap<>(attractions);
    }

    /**
     * Gets an attraction by name (searches through all attractions).
     * @param name the attraction name
     * @return the Destination object, or null if not found
     */
    public Destination getAttractionByName(String name) {
        for (Destination dest : attractions.values()) {
            if (dest.getName().equals(name)) {
                return dest;
            }
        }
        return null;
    }
}
