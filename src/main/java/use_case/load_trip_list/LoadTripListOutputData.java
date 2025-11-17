package use_case.load_trip_list;

import entity.Trip;
import java.util.List;

/**
 * Output Data for the Load Trip List Use Case.
 */
public class LoadTripListOutputData {

    private final List<Trip> trips;
    private final String username;

    public LoadTripListOutputData(List<Trip> trips, String username) {
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

