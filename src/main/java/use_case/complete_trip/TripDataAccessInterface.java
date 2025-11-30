package use_case.complete_trip;

import entity.Trip;

public interface TripDataAccessInterface {
    Trip getTripById(String tripId);
    void saveCTrip(Trip trip);
}