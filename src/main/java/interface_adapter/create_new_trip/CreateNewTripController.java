package interface_adapter.create_new_trip;

import use_case.create_new_trip.CreateNewTripInputBoundary;
import use_case.create_new_trip.CreateNewTripInputData;
import use_case.create_new_trip.CreateNewTripOutputBoundary;

/**
 * The controller for the Create New Trip Use Case.
 */
public class CreateNewTripController {

    private final CreateNewTripInputBoundary createNewTripInteractor;
    private final CreateNewTripOutputBoundary createNewTripPresenter;

    public CreateNewTripController(CreateNewTripInputBoundary createNewTripInteractor,
                                   CreateNewTripOutputBoundary createNewTripPresenter) {
        this.createNewTripInteractor = createNewTripInteractor;
        this.createNewTripPresenter = createNewTripPresenter;
    }

    /**
     * Executes the Create New Trip Use Case.
     * Called when the user is on the Create New Trip screen
     * and clicks the "Create Trip" button.
     *
     * @param from the starting location
     * @param to   the destination
     * @param date the date of the trip
     */
    public void execute(String from, String to, String date) {
        final CreateNewTripInputData inputData =
                new CreateNewTripInputData(from, to, date);

        createNewTripInteractor.execute(inputData);
    }

    /**
     * Opens the Create New Trip form.
     * Called from the dashboard (LoggedInView) when the user
     * clicks the "New Trip" button.
     */
    public void openForm() {
        // this previously directly alerting presenter. now it alerts the interactor.
        // createNewTripPresenter.showCreateNewTripView();
        createNewTripInteractor.prepareScreen();

    }
}
