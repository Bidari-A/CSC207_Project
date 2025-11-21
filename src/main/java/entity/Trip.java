package entity;

import java.util.List;
import java.util.Objects;

public class Trip {

    private final String name;
    private final String cityName;
    private final List<String> tripDates; // now strings
    private final List<Destination> destinations;
    private final List<Accommodation> accommodations;
    private final List<Flight> flights;

    public Trip(String name, String cityName, List<String> tripDates,
                List<Destination> destinations,
                List<Accommodation> accommodations,
                List<Flight> flights) {
        this.name = name;
        this.cityName = cityName;
        this.tripDates = tripDates;
        this.destinations = destinations;
        this.accommodations = accommodations;
        this.flights = flights;
    }

    // Getters
    public String getName() { return name; }
    public String getCityName() { return cityName; }
    public List<String> getTripDates() { return tripDates; }
    public List<Destination> getDestinations() { return destinations; }
    public List<Accommodation> getAccommodations() { return accommodations; }
    public List<Flight> getFlights() { return flights; }

    @Override
    public String toString() {
        return "Trip{" +
                "name='" + name + '\'' +
                ", cityName='" + cityName + '\'' +
                ", tripDates=" + tripDates +
                ", destinations=" + destinations +
                ", accommodations=" + accommodations +
                ", flights=" + flights +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Trip)) return false;
        Trip trip = (Trip) o;
        return Objects.equals(name, trip.name) &&
                Objects.equals(cityName, trip.cityName) &&
                Objects.equals(tripDates, trip.tripDates) &&
                Objects.equals(destinations, trip.destinations) &&
                Objects.equals(accommodations, trip.accommodations) &&
                Objects.equals(flights, trip.flights);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, cityName, tripDates, destinations, accommodations, flights);
    }
}
