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

import entity.Accommodation;
import entity.HotelIdGenerator;

/**
 * DAO for hotel (Accommodation) data implemented using a JSON file.
 * Stores hotels with IDs as keys.
 */
public class FileHotelDataAccessObject {
    private final File jsonFile;
    private final Map<String, Accommodation> hotels = new HashMap<>();

    /**
     * Construct this DAO for saving to and reading from a local JSON file.
     * @param jsonPath the path of the JSON file to save to
     */
    public FileHotelDataAccessObject(String jsonPath) {
        this.jsonFile = new File(jsonPath);

        if (jsonFile.exists() && jsonFile.length() > 0) {
            load();
        } else {
            save();
        }
    }

    /**
     * Loads hotels from the JSON file.
     */
    private void load() {
        try (FileReader reader = new FileReader(jsonFile)) {
            JSONObject jsonObject = new JSONObject(new JSONTokener(reader));

            List<String> hotelIds = new ArrayList<>();
            for (String hotelId : jsonObject.keySet()) {
                hotelIds.add(hotelId);
                JSONObject hotelJson = jsonObject.getJSONObject(hotelId);
                String name = hotelJson.has("name") ? hotelJson.getString("name") : "";
                String address = hotelJson.has("address") ? hotelJson.getString("address") : "";
                float price = hotelJson.has("price") ? (float) hotelJson.getDouble("price") : 0.0f;
                hotels.put(hotelId, new Accommodation(hotelId, name, address, price));
            }

            // Initialize ID generator
            HotelIdGenerator.initializeFromExistingIds(hotelIds);
        } catch (IOException ex) {
            throw new RuntimeException("Error loading hotels from JSON file", ex);
        }
    }

    /**
     * Saves hotels to the JSON file.
     */
    private void save() {
        try (FileWriter writer = new FileWriter(jsonFile)) {
            JSONObject jsonObject = new JSONObject();

            for (Map.Entry<String, Accommodation> entry : hotels.entrySet()) {
                String hotelId = entry.getKey();
                Accommodation hotel = entry.getValue();

                JSONObject hotelJson = new JSONObject();
                hotelJson.put("hotelId", hotelId);
                hotelJson.put("name", hotel.getName());
                hotelJson.put("address", hotel.getAddress());
                hotelJson.put("price", hotel.getPrice());

                jsonObject.put(hotelId, hotelJson);
            }

            writer.write(jsonObject.toString(2));
        } catch (IOException ex) {
            throw new RuntimeException("Error saving hotels to JSON file", ex);
        }
    }

    /**
     * Gets a hotel by ID.
     * @param hotelId the hotel ID
     * @return the Accommodation object, or null if not found
     */
    public Accommodation get(String hotelId) {
        return hotels.get(hotelId);
    }

    /**
     * Gets hotels by their IDs.
     * @param hotelIds list of hotel IDs
     * @return list of Accommodation objects
     */
    public List<Accommodation> getByIds(List<String> hotelIds) {
        List<Accommodation> result = new ArrayList<>();
        for (String hotelId : hotelIds) {
            Accommodation hotel = hotels.get(hotelId);
            if (hotel != null) {
                result.add(hotel);
            }
        }
        return result;
    }

    /**
     * Saves a hotel. Generates ID if not provided.
     * @param hotel the hotel to save
     * @return the saved hotel with ID
     */
    public Accommodation save(Accommodation hotel) {
        String hotelId = hotel.getHotelId();
        if (hotelId == null || hotelId.isEmpty()) {
            hotelId = HotelIdGenerator.nextId();
            hotel = new Accommodation(hotelId, hotel.getName(), hotel.getAddress(), hotel.getPrice());
        }
        hotels.put(hotelId, hotel);
        save();
        return hotel;
    }

    /**
     * Saves multiple hotels.
     * @param hotels list of hotels to save
     * @return list of saved hotels with IDs
     */
    public List<Accommodation> saveAll(List<Accommodation> hotels) {
        List<Accommodation> saved = new ArrayList<>();
        for (Accommodation hotel : hotels) {
            saved.add(save(hotel));
        }
        return saved;
    }

    /**
     * Deletes a hotel by ID.
     * @param hotelId the hotel ID
     */
    public void delete(String hotelId) {
        hotels.remove(hotelId);
        save();
    }

    /**
     * Gets all hotels.
     * @return list of all hotels
     */
    public List<Accommodation> getAll() {
        return new ArrayList<>(hotels.values());
    }
}
