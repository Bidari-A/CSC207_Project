package use_case.complete_trip;

import entity.Trip;

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
        Trip trip = tripDAO.getTripById(inputData.getTripId());
        if (trip == null) {
            presenter.prepareFailView("Trip not found.");
            return;
        }

        // If trip already completed
        if (trip.getStatus().equalsIgnoreCase("completed")) {
            presenter.prepareFailView("Trip is already completed.");
            return;
        }

        // Mark as completed
        trip.setStatus("completed");

        // Save the updated trip
        tripDAO.saveTrip(trip);

        // Produce output data
        CompleteTripOutputData outputData =
                new CompleteTripOutputData(trip.getTripId(), trip.getStatus());

        presenter.prepareSuccessView(outputData);
    }
}