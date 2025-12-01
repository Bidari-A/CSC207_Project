package use_case.complete_trip;

import data_access.TripDataAccessInterface;
import entity.Trip;

/**
 * Interactor for completing a trip.
 */
public class CompleteTripInteractor implements CompleteTripInputBoundary {

    private final TripDataAccessInterface tripDAO;
    private final CompleteTripOutputBoundary presenter;

    public CompleteTripInteractor(TripDataAccessInterface tripDAO,
                                  CompleteTripOutputBoundary presenter) {
        this.tripDAO = tripDAO;
        this.presenter = presenter;
    }

    @Override
    public void execute(CompleteTripInputData inputData) {

        String tripId = inputData.getTripId();

        try {
            /***** ADDED: LOAD TRIP FROM DATABASE *****/
            Trip trip = tripDAO.getTrip(tripId);
            if (trip == null) {
                presenter.prepareFailView("Trip not found.");
                return;
            }

            /***** ADDED: SET STATUS = COMPLETED *****/
            trip.setStatus("COMPLETED");

            /***** ADDED: UPDATE TRIP IN DATABASE *****/
            tripDAO.updateTrip(trip);

            /***** ADDED: ADD TRIP TO HISTORY *****/
            tripDAO.addToHistory(tripId);

            /***** ADDED: CLEAR CURRENT TRIP *****/
            tripDAO.clearCurrentTrip();

            /***** ADDED: SEND SUCCESS VIEW *****/
            presenter.prepareSuccessView(new CompleteTripOutputData(tripId));

        } catch (Exception e) {
            presenter.prepareFailView("Failed to complete trip.");
        }
    }
}