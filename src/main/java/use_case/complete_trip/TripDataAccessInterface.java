/***** CHANGED: Updated package so imports work everywhere *****/
package data_access;

import entity.Trip;

public interface TripDataAccessInterface {


    Trip getTrip(String tripId);


    void updateTrip(Trip trip);


    void addToHistory(String tripId);


    void clearCurrentTrip();
}