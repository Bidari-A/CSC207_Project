package use_case.complete_current_trip;

import entity.Trip;
import entity.User;

/**
 * Data access interface for the Complete Current Trip Use Case.
 */
public interface CompleteCurrentTripDataAccessInterface {
    /**
     * Gets the user by username.
     * @param username the username
     * @return the User object, or null if not found
     */
    User getUser(String username);

    /**
     * Gets the current trip for a user.
     * @param tripId the trip ID
     * @return the Trip object, or null if not found
     */
    Trip getTrip(String tripId);

    /**
     * Saves a trip (updates the trip's status).
     * @param trip the trip to save
     */
    void saveTrip(Trip trip);

    /**
     * Saves a user (updates the user's currentTripId).
     * @param user the user to save
     */
    void saveUser(User user);

    /**
     * Gets the current username.
     * @return the current username
     */
    String getCurrentUsername();
}

