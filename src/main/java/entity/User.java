package entity;

import java.util.List;

public class User {
    private  String username;
    private  String password;

    private  String currentTripId;     // may be null
    private  List<String> tripList;    // list of trip IDs

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
    public void setUsername(String username) { this.username = username; }
    public void setPassword(String password) { this.password = password; }
    public void setCurrentTripId(String currentTripId) { this.currentTripId = currentTripId; }
    public void setTripList(List<String> tripList) { this.tripList = tripList; }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", currentTripId='" + currentTripId + '\'' +
                ", tripList=" + tripList +
                '}';
    }
}
