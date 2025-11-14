package use_case.trip_list;

import entity.Trip;
import java.util.List;

public interface TripListUserDataAccessInterface {
    List<Trip> getTrips(String username);
    String getCurrentUserName();
}
