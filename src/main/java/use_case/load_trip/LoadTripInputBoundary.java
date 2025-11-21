package use_case.load_trip;

/**
 * Input Boundary for loading trips.
 */
public interface LoadTripInputBoundary {

    /**
     * Executes the load trip list use case.
     * @param loadTripInputData the input data containing username
     */
    void execute(use_case.load_trip.LoadTripInputData loadTripInputData);
    void goBack();
}
