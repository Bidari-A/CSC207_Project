package use_case.complete_current_trip;

import entity.Trip;

public interface CompleteCurrentTripDataAccessInterface {
    void addTrip(String username, Trip trip);
}
