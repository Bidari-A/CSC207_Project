package use_case.delete_trip_list;

import entity.Trip;
import java.util.List;

public interface DeleteTripUserDataAccessInterface {

    /**
     * Remove one trip from the user's trip history.
     */
    boolean deleteTrip(String username, String tripName);

    /**
     * Return updated trip list after deletion.
     */
    List<Trip> getTrips(String username);
}
