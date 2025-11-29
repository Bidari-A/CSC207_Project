package use_case.load_trip_list;

import java.util.List;

import entity.Trip;

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

    /**
     * Gets trips for a user filtered by status.
     * Only returns trips that are in the user's tripList and match the specified status.
     * @param username the username
     * @param status the status to filter by (e.g., "COMPLETED", "CURRENT")
     * @return list of trips for the user with the specified status
     */
    List<Trip> getTrips(String username, String status);
}
