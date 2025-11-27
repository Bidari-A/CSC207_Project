package interface_adapter.complete_current_trip;

import use_case.complete_current_trip.CompleteCurrentTripInputBoundary;
import use_case.complete_current_trip.CompleteCurrentTripInputData;

/**
 * The controller for the Complete Current Trip Use Case.
 */
public class CompleteCurrentTripController {
    private CompleteCurrentTripInputBoundary completeCurrentTripUseCaseInteractor;

    public CompleteCurrentTripController(CompleteCurrentTripInputBoundary completeCurrentTripUseCaseInteractor) {
        this.completeCurrentTripUseCaseInteractor = completeCurrentTripUseCaseInteractor;
    }

    /**
     * Executes the Complete Current Trip Use Case.
     * @param username the username of the user completing the trip
     */
    public void execute(String username) {
        CompleteCurrentTripInputData inputData = new CompleteCurrentTripInputData(username);
        completeCurrentTripUseCaseInteractor.execute(inputData);
    }
}
