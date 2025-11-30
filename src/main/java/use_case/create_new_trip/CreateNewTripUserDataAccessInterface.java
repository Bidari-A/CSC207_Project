package use_case.create_new_trip;
import entity.Trip;
import entity.User;

public interface CreateNewTripUserDataAccessInterface {
    /**
     * After a new trip is created, update the given user so that:
     *  - currentTripId = newTripId
     *  - tripList contains newTripId (append if not already there)
     */
    void updateUserTrips(String username, String newTripId);
    void setCurrentTripId(String username, String tripId);
}
