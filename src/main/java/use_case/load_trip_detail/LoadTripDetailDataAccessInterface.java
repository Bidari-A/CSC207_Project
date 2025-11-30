package use_case.load_trip_detail;

import entity.Trip;
import entity.User;

public interface LoadTripDetailDataAccessInterface {
    /**
     * Gets a trip by its ID.
     * @param tripId the trip ID
     * @return the Trip object, or null if not found
     */
    Trip getTrip(String tripId);

    /**
     * Gets a user object by a String: username.
     * @param username the String: username
     * @return the User object
     */
    User get(String username);
}
