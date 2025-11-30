package data_access;

import entity.Trip;
import use_case.delete_trip_list.DeleteTripUserDataAccessInterface;

import java.util.List;

/**
 * Adapter that lets DeleteTrip use case use FileTripDataAccessObject
 * WITHOUT modifying the original trip DAO.
 */
public class FileDeleteTripDataAccessObject implements DeleteTripUserDataAccessInterface {

    private final FileTripDataAccessObject tripDAO;


    public FileDeleteTripDataAccessObject(FileTripDataAccessObject tripDAO) {
        this.tripDAO = tripDAO;
    }

    @Override
    public boolean deleteTrip(String username, String tripId) {
        return tripDAO.delete(tripId);
    }

    @Override
    public List<Trip> getTrips(String username) {
        return tripDAO.getTripsByUser(username);
    }
}
