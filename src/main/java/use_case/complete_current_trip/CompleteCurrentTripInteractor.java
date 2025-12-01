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
            completeCurrentTripPresenter.prepareFailView("There is no current trip to complete. Please create a new trip first.");
            return;
        }

        // Get the trip to verify it exists
        Trip currentTrip = userDataAccessObject.getTrip(currentTripId);
        if (currentTrip == null) {
            completeCurrentTripPresenter.prepareFailView("Current trip not found.");
            return;
        }

        // Verify the trip status is "CURRENT"
        if (!"CURRENT".equals(currentTrip.getStatus())) {
            completeCurrentTripPresenter.prepareFailView("The trip is not a current trip.");
            return;
        }

        // Create a new Trip with status "COMPLETED" (Trip is immutable)
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

        // Save the updated trip
        userDataAccessObject.saveTrip(completedTrip);

        // Update user: add tripId to tripList (if not already there), and set currentTripId to null
        List<String> tripList = new ArrayList<>(user.getTripList());
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

