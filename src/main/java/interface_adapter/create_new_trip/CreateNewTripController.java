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

    /**
     * Executes the Create New Trip Use Case.
     * @param from the starting location
     * @param to the destination
     * @param date the date of the trip
     */
    public void execute(String from, String to, String date) {
        final CreateNewTripInputData inputData =
                new CreateNewTripInputData(from, to, date);

        createNewTripInteractor.execute(inputData);
    }
}