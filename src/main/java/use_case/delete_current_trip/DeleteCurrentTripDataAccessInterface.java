package use_case.delete_current_trip;

import entity.Trip;
import entity.User;

/**
 * Data access interface for the Delete Current Trip Use Case.
 */
public interface DeleteCurrentTripDataAccessInterface {
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
     * Deletes a trip by ID.
     * @param tripId the trip ID to delete
     * @return true if the trip was deleted, false if not found
     */
    boolean deleteTrip(String tripId);

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

