package use_case.delete_current_trip;

import entity.Trip;
import entity.User;
import entity.UserFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * The Delete Current Trip Interactor.
 */
public class DeleteCurrentTripInteractor implements DeleteCurrentTripInputBoundary {
    private final DeleteCurrentTripDataAccessInterface userDataAccessObject;
    private final DeleteCurrentTripOutputBoundary deleteCurrentTripPresenter;
    private final UserFactory userFactory;

    public DeleteCurrentTripInteractor(DeleteCurrentTripDataAccessInterface userDataAccessInterface,
                                      DeleteCurrentTripOutputBoundary deleteCurrentTripOutputBoundary,
                                      UserFactory userFactory) {
        this.userDataAccessObject = userDataAccessInterface;
        this.deleteCurrentTripPresenter = deleteCurrentTripOutputBoundary;
        this.userFactory = userFactory;
    }

    @Override
    public void execute(DeleteCurrentTripInputData inputData) {
        String username = inputData.getUsername();
        User user = userDataAccessObject.getUser(username);

        if (user == null) {
            deleteCurrentTripPresenter.prepareFailView("User not found.");
            return;
        }

        String currentTripId = user.getCurrentTripId();

        // Check if there is a current trip
        if (currentTripId == null || currentTripId.isEmpty()) {
            deleteCurrentTripPresenter.prepareFailView("There is no current trip, unable to delete, please create a new trip first");
            return;
        }

        // Get the trip to verify it exists and has status "CURRENT"
        Trip currentTrip = userDataAccessObject.getTrip(currentTripId);
        if (currentTrip == null) {
            deleteCurrentTripPresenter.prepareFailView("Current trip not found.");
            return;
        }

        // Verify the trip status is "CURRENT"
        if (!"CURRENT".equals(currentTrip.getStatus())) {
            deleteCurrentTripPresenter.prepareFailView("The trip is not a current trip.");
            return;
        }

        // Delete the trip
        boolean deleted = userDataAccessObject.deleteTrip(currentTripId);
        if (!deleted) {
            deleteCurrentTripPresenter.prepareFailView("Failed to delete the trip.");
            return;
        }

        // Update user: remove currentTripId and remove tripId from tripList
        List<String> tripList = new ArrayList<>(user.getTripList());
        tripList.remove(currentTripId);
        User updatedUser = userFactory.create(username, user.getPassword(), null, tripList);
        userDataAccessObject.saveUser(updatedUser);

        // Prepare success view
        DeleteCurrentTripOutputData outputData = new DeleteCurrentTripOutputData(username);
        deleteCurrentTripPresenter.prepareSuccessView(outputData);
    }
}

