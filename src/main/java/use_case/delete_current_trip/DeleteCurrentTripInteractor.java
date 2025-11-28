package use_case.delete_current_trip;

import entity.Trip;
import entity.User;

public class DeleteCurrentTripInteractor implements DeleteCurrentTripInputBoundary {
    private final DeleteCurrentTripDataAccessInterface deleteCurrentTripDataAccessInterface;
    private final DeleteCurrentTripOutputBoundary deleteCurrentTripOutputBoundary;

    public DeleteCurrentTripInteractor(DeleteCurrentTripDataAccessInterface deleteCurrentTripDataAccessInterface,
                                       DeleteCurrentTripOutputBoundary deleteCurrentTripOutputBoundary) {
        this.deleteCurrentTripDataAccessInterface = deleteCurrentTripDataAccessInterface;
        this.deleteCurrentTripOutputBoundary = deleteCurrentTripOutputBoundary;
    }

    @Override
    public void execute(DeleteCurrentTripInputData inputData) {
        String username = inputData.getUsername();
        System.out.println("DeleteCurrentTripInteractor: execute called for user " + username);

        // Get the user
        User user = deleteCurrentTripDataAccessInterface.getUser(username);
        if (user == null) {
            System.out.println("User not found: " + username);
            deleteCurrentTripOutputBoundary.prepareFailView("User not found.");
            return;
        }

        // Get the current trip ID
        String currentTripId = user.getCurrentTripId();
        System.out.println("Current trip ID: " + currentTripId);
        if (currentTripId == null || currentTripId.isEmpty()) {
            System.out.println("No current trip to delete");
            deleteCurrentTripOutputBoundary.prepareFailView("There is no current trip, unable to delete.");
            return;
        }

        // Get the trip to verify it exists
        Trip trip = deleteCurrentTripDataAccessInterface.getTrip(currentTripId);
        if (trip == null) {
            System.out.println("Trip not found: " + currentTripId);
            deleteCurrentTripOutputBoundary.prepareFailView("Trip not found.");
            return;
        }

        // Delete the trip (this will also delete associated hotels/flights/attractions)
        System.out.println("Deleting trip: " + currentTripId);
        boolean deleted = deleteCurrentTripDataAccessInterface.deleteTrip(currentTripId);
        if (!deleted) {
            System.out.println("Failed to delete trip: " + currentTripId);
            deleteCurrentTripOutputBoundary.prepareFailView("Failed to delete trip.");
            return;
        }

        // Remove trip from user's tripList
        System.out.println("Removing trip from user's trip list");
        deleteCurrentTripDataAccessInterface.removeTripFromUserTripList(username, currentTripId);

        // Clear the user's current trip
        System.out.println("Clearing user's current trip");
        deleteCurrentTripDataAccessInterface.clearUserCurrentTrip(username);

        // Prepare success view
        System.out.println("Preparing success view");
        DeleteCurrentTripOutputData outputData = new DeleteCurrentTripOutputData(username, true);
        deleteCurrentTripOutputBoundary.prepareSuccessView(outputData);
    }
}

