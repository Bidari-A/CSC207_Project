package use_case.create_new_trip;

/**
 * Data access interface for updating user information when a new trip
 * is created. Implementations of this interface handle assigning the
 * current trip ID and adding the new trip to the user's trip history.
 */

public interface CreateNewTripUserDataAccessInterface {
  /**
     * Updates the user's trip records after a new trip is created.
     * This method must:
     * 1. Add the new trip ID to the user's trip list if not already present.
     * 2. Set the user's current trip ID to the new trip ID.
     *
     * @param username  the username whose trip list should be updated
     * @param newTripId the ID of the newly created trip
  */
  void updateUserTrips(String username, String newTripId);

  /**
     * Sets the current trip ID for the specified user.
     *
     * @param username the username whose current trip ID should be updated
     * @param tripId   the trip ID to set as current, or null to clear it
  */
  void setCurrentTripId(String username, String tripId);
}