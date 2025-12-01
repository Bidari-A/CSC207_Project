package interface_adapter.complete_current_trip;

import use_case.complete_current_trip.CompleteCurrentTripInputBoundary;
import use_case.complete_current_trip.CompleteCurrentTripInputData;

/**
 * The controller for the Complete Current Trip Use Case.
 */
public class CompleteCurrentTripController {
    private CompleteCurrentTripInputBoundary completeCurrentTripInteractor;

    public CompleteCurrentTripController(CompleteCurrentTripInputBoundary completeCurrentTripInteractor) {
        this.completeCurrentTripInteractor = completeCurrentTripInteractor;
    }

    /**
     * Executes the Complete Current Trip Use Case.
     * @param username the username of the user whose current trip should be completed
     */
    public void execute(String username) {
        CompleteCurrentTripInputData inputData = new CompleteCurrentTripInputData(username);
        completeCurrentTripInteractor.execute(inputData);
    }
}

