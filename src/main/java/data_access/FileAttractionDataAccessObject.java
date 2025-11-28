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

import entity.Destination;
import entity.AttractionIdGenerator;

/**
 * DAO for attraction (Destination) data implemented using a JSON file.
 * Stores attractions with IDs as keys.
 */
public class FileAttractionDataAccessObject {
    private final File jsonFile;
    private final Map<String, Destination> attractions = new HashMap<>();

    /**
     * Construct this DAO for saving to and reading from a local JSON file.
     * @param jsonPath the path of the JSON file to save to
     */
    public FileAttractionDataAccessObject(String jsonPath) {
        this.jsonFile = new File(jsonPath);

        if (jsonFile.exists() && jsonFile.length() > 0) {
            load();
        } else {
            save();
        }
    }

    /**
     * Loads attractions from the JSON file.
     */
    private void load() {
        try (FileReader reader = new FileReader(jsonFile)) {
            JSONObject jsonObject = new JSONObject(new JSONTokener(reader));

            List<String> attractionIds = new ArrayList<>();
            for (String attractionId : jsonObject.keySet()) {
                attractionIds.add(attractionId);
                JSONObject attractionJson = jsonObject.getJSONObject(attractionId);
                String name = attractionJson.has("name") ? attractionJson.getString("name") : "";
                String address = attractionJson.has("address") ? attractionJson.getString("address") : "";
                String description = attractionJson.has("description") ? attractionJson.getString("description") : "";
                float price = attractionJson.has("price") ? (float) attractionJson.getDouble("price") : 0.0f;
                attractions.put(attractionId, new Destination(attractionId, name, address, description, price));
            }

            // Initialize ID generator
            AttractionIdGenerator.initializeFromExistingIds(attractionIds);
        } catch (IOException ex) {
            throw new RuntimeException("Error loading attractions from JSON file", ex);
        }
    }

    /**
     * Saves attractions to the JSON file.
     */
    private void save() {
        try (FileWriter writer = new FileWriter(jsonFile)) {
            JSONObject jsonObject = new JSONObject();

            for (Map.Entry<String, Destination> entry : attractions.entrySet()) {
                String attractionId = entry.getKey();
                Destination attraction = entry.getValue();

                JSONObject attractionJson = new JSONObject();
                attractionJson.put("attractionId", attractionId);
                attractionJson.put("name", attraction.getName());
                attractionJson.put("address", attraction.getAddress());
                attractionJson.put("description", attraction.getDescription());
                attractionJson.put("price", attraction.getPrice());

                jsonObject.put(attractionId, attractionJson);
            }

            writer.write(jsonObject.toString(2));
        } catch (IOException ex) {
            throw new RuntimeException("Error saving attractions to JSON file", ex);
        }
    }

    /**
     * Gets an attraction by ID.
     * @param attractionId the attraction ID
     * @return the Destination object, or null if not found
     */
    public Destination get(String attractionId) {
        return attractions.get(attractionId);
    }

    /**
     * Gets attractions by their IDs.
     * @param attractionIds list of attraction IDs
     * @return list of Destination objects
     */
    public List<Destination> getByIds(List<String> attractionIds) {
        List<Destination> result = new ArrayList<>();
        for (String attractionId : attractionIds) {
            Destination attraction = attractions.get(attractionId);
            if (attraction != null) {
                result.add(attraction);
            }
        }
        return result;
    }

    /**
     * Saves an attraction. Generates ID if not provided.
     * @param attraction the attraction to save
     * @return the saved attraction with ID
     */
    public Destination save(Destination attraction) {
        String attractionId = attraction.getAttractionId();
        if (attractionId == null || attractionId.isEmpty()) {
            attractionId = AttractionIdGenerator.nextId();
            attraction = new Destination(attractionId, attraction.getName(), attraction.getAddress(),
                    attraction.getDescription(), attraction.getPrice());
        }
        attractions.put(attractionId, attraction);
        save();
        return attraction;
    }

    /**
     * Saves multiple attractions.
     * @param attractions list of attractions to save
     * @return list of saved attractions with IDs
     */
    public List<Destination> saveAll(List<Destination> attractions) {
        List<Destination> saved = new ArrayList<>();
        for (Destination attraction : attractions) {
            saved.add(save(attraction));
        }
        return saved;
    }

    /**
     * Deletes an attraction by ID.
     * @param attractionId the attraction ID
     */
    public void delete(String attractionId) {
        attractions.remove(attractionId);
        save();
    }

    /**
     * Gets all attractions.
     * @return list of all attractions
     */
    public List<Destination> getAll() {
        return new ArrayList<>(attractions.values());
    }
}
