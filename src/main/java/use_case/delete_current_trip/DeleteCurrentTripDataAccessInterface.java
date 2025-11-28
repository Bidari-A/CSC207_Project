package use_case.delete_current_trip;

import entity.Trip;
import entity.User;

public interface DeleteCurrentTripDataAccessInterface {
    User getUser(String username);
    Trip getTrip(String tripId);
    boolean deleteTrip(String tripId);
    void clearUserCurrentTrip(String username);
    void removeTripFromUserTripList(String username, String tripId);
}

