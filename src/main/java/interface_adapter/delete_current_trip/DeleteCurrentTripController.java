package interface_adapter.delete_current_trip;

import use_case.delete_current_trip.DeleteCurrentTripInputBoundary;
import use_case.delete_current_trip.DeleteCurrentTripInputData;

/**
 * The controller for the Delete Current Trip Use Case.
 */
public class DeleteCurrentTripController {
    private final DeleteCurrentTripInputBoundary deleteCurrentTripUseCaseInteractor;

    public DeleteCurrentTripController(DeleteCurrentTripInputBoundary deleteCurrentTripUseCaseInteractor) {
        this.deleteCurrentTripUseCaseInteractor = deleteCurrentTripUseCaseInteractor;
    }

    /**
     * Executes the Delete Current Trip Use Case.
     * @param username the username of the user deleting the trip
     */
    public void execute(String username) {
        DeleteCurrentTripInputData inputData = new DeleteCurrentTripInputData(username);
        deleteCurrentTripUseCaseInteractor.execute(inputData);
    }
}

