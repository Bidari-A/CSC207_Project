package use_case.complete_current_trip;

import entity.Trip;
import entity.User;
import entity.UserFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * The Complete Current Trip Interactor.
 */
public class CompleteCurrentTripInteractor implements CompleteCurrentTripInputBoundary {
    private final CompleteCurrentTripDataAccessInterface userDataAccessObject;
    private final CompleteCurrentTripOutputBoundary completeCurrentTripPresenter;
    private final UserFactory userFactory;

    public CompleteCurrentTripInteractor(CompleteCurrentTripDataAccessInterface userDataAccessInterface,
                                         CompleteCurrentTripOutputBoundary completeCurrentTripOutputBoundary,
                                         UserFactory userFactory) {
        this.userDataAccessObject = userDataAccessInterface;
        this.completeCurrentTripPresenter = completeCurrentTripOutputBoundary;
        this.userFactory = userFactory;
    }

    @Override
    public void execute(CompleteCurrentTripInputData inputData) {
        String username = inputData.getUsername();
        User user = userDataAccessObject.getUser(username);

        if (user == null) {
            completeCurrentTripPresenter.prepareFailView("User not found.");
            return;
        }

        String currentTripId = user.getCurrentTripId();

        // Check if there is a current trip
        if (currentTripId == null || currentTripId.isEmpty()) {
            completeCurrentTripPresenter.prepareFailView("There is no current trip, unable to complete, please create a current trip first");
            return;
        }

        // Get the trip to verify it exists
        Trip currentTrip = userDataAccessObject.getTrip(currentTripId);
        if (currentTrip == null) {
            completeCurrentTripPresenter.prepareFailView("Current trip not found.");
            return;
        }

        // Update trip status to "COMPLETED"
        Trip completedTrip = new Trip(
                currentTrip.getTripId(),
                currentTrip.getTripName(),
                currentTrip.getOwnerUserName(),
                "COMPLETED",
                currentTrip.getDates(),
                currentTrip.getDestination(),
                currentTrip.getHotels(),
                currentTrip.getFlights(),
                currentTrip.getAttractions()
        );
        userDataAccessObject.saveTrip(completedTrip);

        // Update user: add tripId to tripList and clear currentTripId (set to null)
        List<String> tripList = new ArrayList<>(user.getTripList());
        // Add the completed trip to tripList if not already present
        if (!tripList.contains(currentTripId)) {
            tripList.add(currentTripId);
        }
        User updatedUser = userFactory.create(username, user.getPassword(), null, tripList);
        userDataAccessObject.saveUser(updatedUser);

        // Prepare success view
        CompleteCurrentTripOutputData outputData = new CompleteCurrentTripOutputData(username);
        completeCurrentTripPresenter.prepareSuccessView(outputData);
    }
}

