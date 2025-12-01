package entity;

import java.util.List;

public class Trip {
    private final String tripId;
    private final String tripName;
    private final String ownerUserName;
    private String status;
    private final String dates;
    private final String destination; // Main destination location (from "To" field)
    private final List<Accommodation> hotels;
    private final List<Flight> flights;
    private final List<Destination> attractions; // List of Destination objects

    public Trip(String tripId, String tripName, String ownerUserName, String status,
                String dates, String destination,
                List<Accommodation> hotels, List<Flight> flights, List<Destination> attractions) {
        this.tripId = tripId;
        this.tripName = tripName;
        this.ownerUserName = ownerUserName;
        this.status = status;
        this.dates = dates;
        this.destination = destination;
        this.hotels = hotels;
        this.flights = flights;
        this.attractions = attractions;
    }

    // Getters
    public String getTripId() { return this.tripId; }
    public String getTripName() { return this.tripName; }
    public String getOwnerUserName() { return this.ownerUserName; }
    public String getStatus() { return this.status; }
    public String getDates() { return this.dates; }
    public String getDestination() { return this.destination; }
    public List<Accommodation> getHotels() { return this.hotels; }
    public List<Flight> getFlights() { return this.flights; }
    public List<Destination> getAttractions() { return this.attractions; }

    public void setStatus(String status) {
        this.status = status;
    }
}
