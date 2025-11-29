package use_case.create_new_trip;

import entity.Trip;

/**
 * Data access interface for creating and saving a new Trip.
 * Implemented by the Trip DAO in the data_access layer.
 */
public interface CreateNewTripTripDataAccessInterface {

    /**
     * Saves the given Trip to persistent storage.
     *
     * @param trip the Trip to save
     */
    // void saveTrip(Trip trip);
    Trip saveTrip(Trip trip);

}
