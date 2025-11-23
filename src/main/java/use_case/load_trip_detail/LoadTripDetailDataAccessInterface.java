package use_case.load_trip_detail;

import entity.Trip;

public interface LoadTripDetailDataAccessInterface {
    Trip getTripByName(String username, String tripName);
}
