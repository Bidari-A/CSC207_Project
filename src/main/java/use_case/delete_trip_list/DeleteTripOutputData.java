package use_case.delete_trip_list;

import entity.Trip;
import java.util.List;

public class DeleteTripOutputData {

    private final List<Trip> updatedTrips;
    private final String username;

    public DeleteTripOutputData(List<Trip> updatedTrips, String username) {
        this.updatedTrips = updatedTrips;
        this.username = username;
    }

    public List<Trip> getUpdatedTrips() { return updatedTrips; }

    public String getUsername() { return username; }
}
