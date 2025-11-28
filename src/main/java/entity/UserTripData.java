package entity;

/**
 * Represents the relationship between a user and a trip.
 * Used to track which trips belong to which users.
 */
public class UserTripData {
    private final String username;
    private final String tripId;

    public UserTripData(String username, String tripId) {
        this.username = username;
        this.tripId = tripId;
    }

    public String getUsername() {
        return username;
    }

    public String getTripId() {
        return tripId;
    }
}

