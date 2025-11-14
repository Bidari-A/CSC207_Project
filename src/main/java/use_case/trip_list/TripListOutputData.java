package use_case.trip_list;

import entity.Trip;
import java.util.List;

/**
 * Output data for the Trip List Use Case.
 */
public class TripListOutputData {
    private final List<Trip> trips;
    private final String username;

    public TripListOutputData(List<Trip> trips, String username) {
        this.trips = trips;
        this.username = username;
    }

    public List<Trip> getTrips() {
        return trips;
    }

    public String getUsername() {
        return username;
    }
}
