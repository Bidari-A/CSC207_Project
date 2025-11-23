package interface_adapter.create_new_trip;

import use_case.create_new_trip.CreateNewTripInputBoundary;
import use_case.create_new_trip.CreateNewTripInputData;

/**
 * The controller for the Create New Trip Use Case.
 */

public class CreateNewTripController {

    private final CreateNewTripInputBoundary createNewTripInteractor;

    public CreateNewTripController(CreateNewTripInputBoundary createNewTripInteractor) {
        this.createNewTripInteractor = createNewTripInteractor;
    }
    public void goBack() {
        createNewTripInteractor.goBack();
    }

    public void execute(String from, String to, String date) {
        CreateNewTripInputData inputData =
                new CreateNewTripInputData(from, to, date);
        createNewTripInteractor.execute(inputData);
    }

    public void openForm() {
        // no more direct call to the view here
        createNewTripInteractor.prepareScreen();
    }
}
