package interface_adapter.create_new_trip;

import use_case.create_new_trip.CreateNewTripInputBoundary;
import use_case.create_new_trip.CreateNewTripInputData;

/**
 * The controller for the Create New Trip Use Case.
 */

public class CreateNewTripController {

    private final CreateNewTripInputBoundary createNewTripInteractor;
    private String currentUsername;


    public CreateNewTripController(CreateNewTripInputBoundary createNewTripInteractor) {
        this.createNewTripInteractor = createNewTripInteractor;
    }
    public void goBack() {
        createNewTripInteractor.goBack();
    }

    public void execute(String from, String to, String startDate,
                        String endDate) {
        String username = this.currentUsername;   // cached earlier in openForm
        CreateNewTripInputData inputData =
                new CreateNewTripInputData(from, to, startDate, endDate, username);
        createNewTripInteractor.execute(inputData);
    }

    public void openForm(String username) {
        // no more direct call to the view here
        this.currentUsername = username;
        createNewTripInteractor.prepareScreen();
    }
}
