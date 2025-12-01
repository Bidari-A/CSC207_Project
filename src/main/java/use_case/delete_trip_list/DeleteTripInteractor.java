package use_case.delete_trip_list;

import entity.Trip;
import entity.User;

import java.util.ArrayList;
import java.util.List;

import use_case.delete_current_trip.DeleteCurrentTripDataAccessInterface;

public class DeleteTripInteractor implements DeleteTripInputBoundary {

    private final DeleteTripUserDataAccessInterface tripDAO;
    private final DeleteCurrentTripDataAccessInterface userDAO;
    private final DeleteTripOutputBoundary presenter;

    public DeleteTripInteractor(DeleteTripUserDataAccessInterface tripDAO,
                                DeleteCurrentTripDataAccessInterface userDAO,
                                DeleteTripOutputBoundary presenter) {
        this.tripDAO = tripDAO;
        this.userDAO = userDAO;
        this.presenter = presenter;
    }

    @Override
    public void delete(DeleteTripInputData data) {
        String username = data.getUsername();
        String tripId = data.getTripId();

        // 1. Delete trip from tripDAO
        boolean success = tripDAO.deleteTrip(username, tripId);
        if (!success) {
            presenter.prepareFailView("Failed to delete trip.");
            return;
        }

        // 2. Load user
        User user = userDAO.getUser(username);

        // 3. If this is the current trip, clear currentTripId
        if (user.getCurrentTripId() != null &&
                user.getCurrentTripId().equals(tripId)) {
            user.setCurrentTripId(null);
        }

        // 4. Remove from user's history
        user.getTripList().remove(tripId);

        // 5. Save user
        userDAO.saveUser(user);

        // 6. Call teammate’s logic: get COMPLETED trips
        List<Trip> updatedTrips = getCompletedTrips(username);

        // 7. Output
        DeleteTripOutputData outputData =
                new DeleteTripOutputData(updatedTrips, username);

        presenter.prepareSuccessView(outputData);
    }

    private List<Trip> getCompletedTrips(String username) {

        // tripDAO.getTrips(username) returns ALL trips belonging to the user
        List<Trip> allTrips = tripDAO.getTrips(username);

        List<Trip> completedTrips = new ArrayList<>();

        for (Trip trip : allTrips) {
            if (trip != null && "COMPLETED".equals(trip.getStatus())) {
                completedTrips.add(trip);
            }
        }

        return completedTrips;
    }
}