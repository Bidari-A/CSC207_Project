package interface_adapter.trip_list;

import entity.Trip;
import java.util.List;

public class TripListState {
    private List<Trip> trips = null;
    private String username = "";
    private String error = null;

    public TripListState() {}

    public TripListState(TripListState copy) {
        this.trips = copy.trips;
        this.username = copy.username;
        this.error = copy.error;
    }

    public List<Trip> getTrips() { return trips; }
    public void setTrips(List<Trip> trips) { this.trips = trips; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getError() { return error; }
    public void setError(String error) { this.error = error; }
}