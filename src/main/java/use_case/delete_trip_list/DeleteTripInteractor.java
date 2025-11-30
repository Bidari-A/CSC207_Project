package use_case.delete_trip_list;

import entity.Trip;
import entity.User;

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

        boolean success = tripDAO.deleteTrip(username, tripId);
        if (!success) {
            presenter.prepareFailView("Failed to delete trip.");
            return;
        }

        User user = userDAO.getUser(username);

        if (user.getCurrentTripId() != null &&
                user.getCurrentTripId().equals(tripId)) {
            user.setCurrentTripId(null);
        }

        user.getTripList().remove(tripId);

        userDAO.saveUser(user);

        List<Trip> updatedTrips = tripDAO.getTrips(username);

        DeleteTripOutputData outputData =
                new DeleteTripOutputData(updatedTrips, username);

        presenter.prepareSuccessView(outputData);
    }
}