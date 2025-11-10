package entity;

import java.util.Date;
import java.util.List;

public class Trip {
    private final String name;
    private final String cityName;
    private final List<Date> tripDates;
    private final List<Destination> destinations;
    private final List<Accommodation> accommodations;
    private final List<Flight> flights;

    public Trip(String name, String cityName, List<Date> tripDates, List<Destination> destinations, List<Accommodation> accommodations, List<Flight> flights) {
        this.name = name;
        this.cityName = cityName;
        this.tripDates = tripDates;
        this.destinations = destinations;
        this.accommodations = accommodations;
        this.flights = flights;
    }

    // TODO: Implement Methods

}
