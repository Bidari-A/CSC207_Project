package use_case.complete_trip;

import entity.Trip;
import use_case.complete_trip.CompleteTripOutputBoundary;
import use_case.complete_trip.CompleteTripInputData;
import use_case.complete_trip.CompleteTripOutputData;
import use_case.delete_current_trip.DeleteCurrentTripDataAccessInterface;

import data_access.FileTripDataAccessObject;
import data_access.FileUserDataAccessObject;

public class CompleteTripInteractor implements CompleteTripInputBoundary {

    private final FileUserDataAccessObject userDAO;
    private final FileTripDataAccessObject tripDAO;
    private final CompleteTripOutputBoundary presenter;

    public CompleteTripInteractor(FileUserDataAccessObject userDAO,
                                  FileTripDataAccessObject tripDAO,
                                  CompleteTripOutputBoundary presenter) {
        this.userDAO = userDAO;
        this.tripDAO = tripDAO;
        this.presenter = presenter;
    }

    @Override
    public void execute(CompleteTripInputData inputData) {
        String username = inputData.getUsername();

        try {
            // 1. Load user’s current trip ID
            String currentTripId = userDAO.getCurrentTripId(username);

            if (currentTripId == null || currentTripId.isEmpty()) {
                presenter.prepareFailView("No active trip to complete.");
                return;
            }

            // 2. Load the trip (correct method name)
            Trip trip = tripDAO.get(currentTripId);

            if (trip == null) {
                presenter.prepareFailView("Current trip not found.");
                return;
            }

            // 3. Update its status (only works if you add setStatus() to Trip)
            trip.setStatus("Completed");

            // 4. Save/update trip (correct method)
            tripDAO.saveTrip(trip);

            // 5. Move trip to user history
            userDAO.addTripToHistory(username, currentTripId);

            // 6. Clear current trip
            userDAO.clearCurrentTrip(username);

            // 7. Persist
            userDAO.save(userDAO.get(username));

            // 8. Prepare success output
            CompleteTripOutputData outputData =
                    new CompleteTripOutputData(trip.getTripId(), trip.getTripName());

            presenter.prepareSuccessView(outputData);

        } catch (Exception e) {
            presenter.prepareFailView("Failed to complete trip: " + e.getMessage());
        }
    }
}


