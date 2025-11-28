package entity;

import java.util.List;

public class Trip {
    private final String tripId;
    private final String tripName;
    private final String ownerUserName;
    private final String status;
    private final String dates;
    private final String destination; // Main destination location (from "To" field)
    private final List<String> hotelIds; // List of hotel IDs
    private final List<String> flightIds; // List of flight IDs
    private final List<String> attractionIds; // List of attraction IDs
    // Transient fields for convenience (loaded from DAOs)
    private transient List<Accommodation> hotels;
    private transient List<Flight> flights;
    private transient List<Destination> attractions;

    public Trip(String tripId, String tripName, String ownerUserName, String status,
                String dates, String destination,
                List<String> hotelIds, List<String> flightIds, List<String> attractionIds) {
        this.tripId = tripId;
        this.tripName = tripName;
        this.ownerUserName = ownerUserName;
        this.status = status;
        this.dates = dates;
        this.destination = destination;
        this.hotelIds = hotelIds;
        this.flightIds = flightIds;
        this.attractionIds = attractionIds;
    }

    // Getters for IDs
    public String getTripId() { return this.tripId; }
    public String getTripName() { return this.tripName; }
    public String getOwnerUserName() { return this.ownerUserName; }
    public String getStatus() { return this.status; }
    public String getDates() { return this.dates; }
    public String getDestination() { return this.destination; }
    public List<String> getHotelIds() { return this.hotelIds; }
    public List<String> getFlightIds() { return this.flightIds; }
    public List<String> getAttractionIds() { return this.attractionIds; }
    
    // Getters for full objects (loaded from DAOs)
    public List<Accommodation> getHotels() { return this.hotels; }
    public List<Flight> getFlights() { return this.flights; }
    public List<Destination> getAttractions() { return this.attractions; }
    
    // Setters for full objects (set by DAOs when loading)
    public void setHotels(List<Accommodation> hotels) { this.hotels = hotels; }
    public void setFlights(List<Flight> flights) { this.flights = flights; }
    public void setAttractions(List<Destination> attractions) { this.attractions = attractions; }
}
