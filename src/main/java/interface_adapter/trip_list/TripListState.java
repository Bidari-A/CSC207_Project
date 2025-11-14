package interface_adapter.trip_list;

import entity.Trip;
import java.util.List;

public class TripListState {
    private List<Trip> trips = null;
    private String username = "";
    private String error = null;

    public TripListState(TripListState copy) {
        trips = copy.trips;
        username = copy.username;
        error = copy.error;
    }
    public TripListState() {

    }
    public List<Trip> getTrips() {
        return trips;
    }
    public void setTrips(List<Trip> trips) {
        this.trips = trips;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getError() {
        return error;
    }
    public void setError(String error) {
        this.error = error;
    }
}
