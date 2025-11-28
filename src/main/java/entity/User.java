package entity;

import java.util.List;

public class User {
    private final String username;
    private final String password;

    private final String currentTripId;     // may be null
    private final List<String> tripList;    // list of trip IDs

    public User(String username, String password,
                String currentTripId, List<String> tripList) {
        this.username = username;
        this.password = password;
        this.currentTripId = currentTripId;
        this.tripList = tripList;
    }

    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getCurrentTripId() { return currentTripId; }
    public List<String> getTripList() { return tripList; }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", currentTripId='" + currentTripId + '\'' +
                ", tripList=" + tripList +
                '}';
    }
}
