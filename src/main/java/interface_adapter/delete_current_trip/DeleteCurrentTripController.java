package interface_adapter.delete_current_trip;

import use_case.delete_current_trip.DeleteCurrentTripInputBoundary;
import use_case.delete_current_trip.DeleteCurrentTripInputData;

/**
 * The controller for the Delete Current Trip Use Case.
 */
public class DeleteCurrentTripController {
    private DeleteCurrentTripInputBoundary deleteCurrentTripInteractor;

    public DeleteCurrentTripController(DeleteCurrentTripInputBoundary deleteCurrentTripInteractor) {
        this.deleteCurrentTripInteractor = deleteCurrentTripInteractor;
    }

    /**
     * Executes the Delete Current Trip Use Case.
     * @param username the username of the user whose current trip should be deleted
     */
    public void execute(String username) {
        DeleteCurrentTripInputData inputData = new DeleteCurrentTripInputData(username);
        deleteCurrentTripInteractor.execute(inputData);
    }
}

