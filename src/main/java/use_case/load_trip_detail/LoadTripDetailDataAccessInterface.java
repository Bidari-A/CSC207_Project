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

    User get(String username);
}
