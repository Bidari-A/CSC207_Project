package use_case.complete_current_trip;

import entity.Trip;
import entity.User;

public interface CompleteCurrentTripDataAccessInterface {
    User getUser(String username);
    Trip getTrip(String tripId);
    void updateTripStatus(Trip trip, String newStatus);
    void clearUserCurrentTrip(String username);
}