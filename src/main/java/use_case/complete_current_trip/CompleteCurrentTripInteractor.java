package use_case.complete_current_trip;

import entity.Trip;
import entity.User;

public class CompleteCurrentTripInteractor implements CompleteCurrentTripInputBoundary {
    private final CompleteCurrentTripDataAccessInterface completeCurrentTripDataAccessInterface;
    private final CompleteCurrentTripOutputBoundary completeCurrentTripOutputBoundary;

    public CompleteCurrentTripInteractor(CompleteCurrentTripDataAccessInterface completeCurrentTripDataAccessInterface,
                                         CompleteCurrentTripOutputBoundary completeCurrentTripOutputBoundary) {
        this.completeCurrentTripDataAccessInterface = completeCurrentTripDataAccessInterface;
        this.completeCurrentTripOutputBoundary = completeCurrentTripOutputBoundary;
    }

    @Override
    public void execute(CompleteCurrentTripInputData inputData) {
        String username = inputData.getUsername();
        System.out.println("CompleteCurrentTripInteractor: execute called for user " + username);

        // Get the user
        User user = completeCurrentTripDataAccessInterface.getUser(username);
        if (user == null) {
            System.out.println("User not found: " + username);
            completeCurrentTripOutputBoundary.prepareFailView("User not found.");
            return;
        }

        // Get the current trip ID
        String currentTripId = user.getCurrentTripId();
        System.out.println("Current trip ID: " + currentTripId);
        if (currentTripId == null || currentTripId.isEmpty()) {
            System.out.println("No current trip to complete");
            completeCurrentTripOutputBoundary.prepareFailView("No current trip to complete.");
            return;
        }

        // Get the trip
        Trip trip = completeCurrentTripDataAccessInterface.getTrip(currentTripId);
        if (trip == null) {
            System.out.println("Trip not found: " + currentTripId);
            completeCurrentTripOutputBoundary.prepareFailView("Trip not found.");
            return;
        }

        System.out.println("Updating trip status to COMPLETED");
        // Update trip status to COMPLETED
        completeCurrentTripDataAccessInterface.updateTripStatus(trip, "COMPLETED");

        System.out.println("Clearing user's current trip");
        // Clear the user's current trip
        completeCurrentTripDataAccessInterface.clearUserCurrentTrip(username);

        // Prepare success view
        System.out.println("Preparing success view");
        CompleteCurrentTripOutputdata outputData = new CompleteCurrentTripOutputdata(username, true);
        completeCurrentTripOutputBoundary.prepareSuccessView(outputData);
    }
}
