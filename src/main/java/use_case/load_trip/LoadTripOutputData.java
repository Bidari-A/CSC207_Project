package use_case.load_trip;

import entity.Trip;
import java.util.List;

/**
 * Output Data for the Load Trip List Use Case.
 */
public class LoadTripOutputData {

    private final List<Trip> trips;
    private final String username;

    public LoadTripOutputData(List<Trip> trips, String username) {
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

