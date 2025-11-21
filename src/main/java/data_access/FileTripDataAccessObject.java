package data_access;

import entity.*;
import use_case.load_trip.TripFileDataAccessInterface;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class FileTripDataAccessObject implements TripFileDataAccessInterface {

    private final String folderPath;

    public FileTripDataAccessObject(String folderPath) {
        this.folderPath = folderPath;
    }

    @Override
    public List<Trip> getTrips(String username) {
        List<String> userTripNames = loadUserTrips(username);
        Map<String, Trip> basicTrips = loadBasicTrips();
        Map<String, List<Destination>> destGroups = loadDestinations();
        Map<String, List<Accommodation>> accomGroups = loadAccommodations();
        Map<String, List<Flight>> flightGroups = loadFlights();

        List<Trip> result = new ArrayList<>();
        for (String tripName : userTripNames) {
            Trip base = basicTrips.get(tripName);
            if (base == null) continue;

            Trip fullTrip = new Trip(
                    base.getName(),
                    base.getCityName(),
                    base.getTripDates(),
                    destGroups.getOrDefault(tripName, new ArrayList<>()),
                    accomGroups.getOrDefault(tripName, new ArrayList<>()),
                    flightGroups.getOrDefault(tripName, new ArrayList<>())
            );

            result.add(fullTrip);
        }

        return result;
    }

    // ====================== HELPERS ======================

    private List<String> loadUserTrips(String username) {
        List<String> result = new ArrayList<>();
        String path = folderPath + "/user_trips.csv";

        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                String[] parts = line.split(",", 2); // username,tripName
                if (parts.length < 2) continue;
                if (parts[0].trim().equals(username)) {
                    result.add(parts[1].trim());
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to read user_trips.csv", e);
        }

        return result;
    }

    private Map<String, Trip> loadBasicTrips() {
        Map<String, Trip> map = new HashMap<>();
        String path = folderPath + "/trips.csv";

        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                String[] parts = line.split(",", 3); // tripName, cityName, dates
                String name = parts[0].trim();
                String city = parts[1].trim();
                List<String> dates = new ArrayList<>();

                if (parts.length > 2) {
                    for (String d : parts[2].split("\\|")) {
                        dates.add(d.trim());
                    }
                }

                map.put(name, new Trip(name, city, dates,
                        new ArrayList<>(), new ArrayList<>(), new ArrayList<>()));
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to read trips.csv", e);
        }

        return map;
    }

    private Map<String, List<Destination>> loadDestinations() {
        Map<String, List<Destination>> map = new HashMap<>();
        String path = folderPath + "/destinations.csv";

        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                // Handle quoted fields and commas in description
                String[] parts = splitCsvLine(line);
                if (parts.length < 5) continue;

                String tripName = parts[0].trim();
                String name = parts[1].trim();
                String address = parts[2].trim();
                String description = parts[3].trim();
                float price = 0.0f;
                try {
                    price = Float.parseFloat(parts[4].trim());
                } catch (NumberFormatException ignored) {}

                Destination d = new Destination(name, address, description, price);
                map.computeIfAbsent(tripName, k -> new ArrayList<>()).add(d);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to read destinations.csv", e);
        }

        return map;
    }

    private Map<String, List<Accommodation>> loadAccommodations() {
        Map<String, List<Accommodation>> map = new HashMap<>();
        String path = folderPath + "/accommodations.csv";

        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                String[] parts = splitCsvLine(line);
                if (parts.length < 4) continue;

                String tripName = parts[0].trim();
                String name = parts[1].trim();
                String address = parts[2].trim();
                float price = 0.0f;
                try {
                    price = Float.parseFloat(parts[3].trim());
                } catch (NumberFormatException ignored) {}

                Accommodation a = new Accommodation(name, address, price);
                map.computeIfAbsent(tripName, k -> new ArrayList<>()).add(a);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to read accommodations.csv", e);
        }

        return map;
    }

    private Map<String, List<Flight>> loadFlights() {
        Map<String, List<Flight>> map = new HashMap<>();
        String path = folderPath + "/flights.csv";

        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                String[] parts = splitCsvLine(line);
                if (parts.length < 4) continue;

                String tripName = parts[0].trim();
                String airline = parts[1].trim();
                Date depart = new Date(Long.parseLong(parts[2].trim()));
                float price = 0.0f;
                try {
                    price = Float.parseFloat(parts[3].trim());
                } catch (NumberFormatException ignored) {}

                Flight f = new Flight(airline, depart, price);
                map.computeIfAbsent(tripName, k -> new ArrayList<>()).add(f);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to read flights.csv", e);
        }

        return map;
    }

    // ====================== SIMPLE CSV PARSER ======================
    // Handles quoted fields with commas
    private String[] splitCsvLine(String line) {
        List<String> result = new ArrayList<>();
        boolean inQuotes = false;
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == '"') {
                inQuotes = !inQuotes; // toggle
            } else if (c == ',' && !inQuotes) {
                result.add(sb.toString().trim());
                sb.setLength(0);
            } else {
                sb.append(c);
            }
        }
        result.add(sb.toString().trim()); // last field
        return result.toArray(new String[0]);
    }
}
