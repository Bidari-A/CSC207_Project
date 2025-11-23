package data_access;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import entity.Accommodation;
import entity.Destination;
import entity.Flight;
import entity.Trip;

/**
 * Helper class to create Trip entities using data from JSON files.
 * This demonstrates how to integrate the JSON data access objects with trip creation.
 */
public class TripDataAccessHelper {
    private final FileAttractionDataAccessObject attractionDAO;
    private final FileFlightDataAccessObject flightDAO;
    private final FileHotelDataAccessObject hotelDAO;

    public TripDataAccessHelper(String attractionsPath, String flightsPath, String hotelsPath) {
        this.attractionDAO = new FileAttractionDataAccessObject(attractionsPath);
        this.flightDAO = new FileFlightDataAccessObject(flightsPath);
        this.hotelDAO = new FileHotelDataAccessObject(hotelsPath);
    }

    /**
     * Creates a trip with data from JSON files.
     * @param tripName the name of the trip
     * @param cityName the city name
     * @param date the trip date
     * @param attractionIds list of attraction IDs to include (optional)
     * @param flightIds list of flight IDs to include (optional)
     * @param hotelIds list of hotel IDs to include (optional)
     * @return a Trip object with data from JSON files
     */
    public Trip createTripFromJSON(String tripName, String cityName, String date,
                                   List<String> attractionIds, List<String> flightIds, List<String> hotelIds) {
        List<Date> tripDates = new ArrayList<>();
        try {
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
            Date tripDate = sdf.parse(date);
            tripDates.add(tripDate);
        } catch (Exception e) {
            // If parsing fails, leave dates empty
        }

        List<Destination> destinations = new ArrayList<>();
        if (attractionIds != null) {
            for (String id : attractionIds) {
                Destination dest = attractionDAO.getAttraction(id);
                if (dest != null) {
                    destinations.add(dest);
                }
            }
        }

        List<Flight> flights = new ArrayList<>();
        if (flightIds != null) {
            for (String id : flightIds) {
                Flight flight = flightDAO.getFlight(id);
                if (flight != null) {
                    flights.add(flight);
                }
            }
        }

        List<Accommodation> accommodations = new ArrayList<>();
        if (hotelIds != null) {
            for (String id : hotelIds) {
                Accommodation acc = hotelDAO.getHotel(id);
                if (acc != null) {
                    accommodations.add(acc);
                }
            }
        }

        return new Trip(tripName, cityName, tripDates, destinations, accommodations, flights);
    }

    /**
     * Gets all available attractions.
     * @return map of attraction IDs to Destination objects
     */
    public Map<String, Destination> getAllAttractions() {
        return attractionDAO.getAllAttractions();
    }

    /**
     * Gets all available flights.
     * @return map of flight IDs to Flight objects
     */
    public Map<String, Flight> getAllFlights() {
        return flightDAO.getAllFlights();
    }

    /**
     * Gets all available hotels.
     * @return map of hotel IDs to Accommodation objects
     */
    public Map<String, Accommodation> getAllHotels() {
        return hotelDAO.getAllHotels();
    }
}

