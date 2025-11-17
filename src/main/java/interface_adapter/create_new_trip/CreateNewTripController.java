package interface_adapter.create_new_trip;

import use_case.create_new_trip.CreateNewTripInputBoundary;
import use_case.create_new_trip.CreateNewTripInputData;
import interface_adapter.ViewManagerModel;


/**
 * The controller for the Create New Trip Use Case.
 */
public class CreateNewTripController {

    private final CreateNewTripInputBoundary createNewTripInteractor;
    private final ViewManagerModel viewManagerModel;


    public CreateNewTripController(ViewManagerModel viewManagerModel) {
        this.viewManagerModel = viewManagerModel;
        //this.createNewTripInteractor = createNewTripInteractor;
        this.createNewTripInteractor = null; // will set later when you implement the use case
    }

    /**
     * Executes the Create New Trip Use Case.
     * @param from the starting location
     * @param to the destination
     * @param date the date of the trip
     */

    public void execute(String from, String to, String date) {
        if (createNewTripInteractor == null) {
            System.out.println("CreateNewTripInteractor not set yet");
            return;
        }

        final CreateNewTripInputData inputData =
                new CreateNewTripInputData(from, to, date);

        createNewTripInteractor.execute(inputData);
    }




    public void openForm() {
        viewManagerModel.setState("create new trip");
        viewManagerModel.firePropertyChange();
    }

}