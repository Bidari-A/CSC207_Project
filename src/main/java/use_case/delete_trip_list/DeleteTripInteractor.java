package use_case.delete_trip_list;

import entity.Trip;

import java.util.List;

public class DeleteTripInteractor implements DeleteTripInputBoundary {

    private final DeleteTripUserDataAccessInterface dao;
    private final DeleteTripOutputBoundary presenter;

    public DeleteTripInteractor(DeleteTripUserDataAccessInterface dao,
                                DeleteTripOutputBoundary presenter) {
        this.dao = dao;
        this.presenter = presenter;
    }

    @Override
    public void delete(DeleteTripInputData data) {
        String username = data.getUsername();
        String tripId = data.getTripId();

        boolean success = dao.deleteTrip(username, tripId);
        if (!success) {
            presenter.prepareFailView("Failed to delete trip.");
            return;
        }

        List<Trip> updatedTrips = dao.getTrips(username);

        DeleteTripOutputData outputData = new DeleteTripOutputData(updatedTrips, username);
        presenter.prepareSuccessView(outputData);
    }
}