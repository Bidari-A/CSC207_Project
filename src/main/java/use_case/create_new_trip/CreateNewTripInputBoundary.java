package use_case.create_new_trip;

/**
 * Input boundary for the Create New Trip use case.
 * This interface defines the methods the controller can call
 * to start the use case or trigger view related transitions.
 */
public interface CreateNewTripInputBoundary {

    /**
     * Executes the Create New Trip use case using the provided input data.
     *
     * @param inputData the data required to create a new trip
     */
    void execute(CreateNewTripInputData inputData);

    /**
     * Prepares the initial Create New Trip screen.
     * Called when the user opens the form for creating a trip.
     */
    void prepareScreen();

    /**
     * Requests navigation back to the previous screen.
     */
    void goBack();

    /**
     * Requests navigation back from the trip result screen
     * to the Create New Trip screen.
     */
    void backFromResult();
}
