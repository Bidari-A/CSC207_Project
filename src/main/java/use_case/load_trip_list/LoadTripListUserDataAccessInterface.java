package use_case.load_trip_list;

import entity.Trip;
import java.util.List;

/**
 * Data Access Interface for loading trip list.
 */
public interface LoadTripListUserDataAccessInterface {

    /**
     * Gets all trips for a user.
     * @param username the username
     * @return list of trips for the user
     */
    List<Trip> getTrips(String username);
}
