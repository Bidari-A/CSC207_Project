package use_case.load_trip_list;

/**
 * Input Boundary for loading trip list.
 */
public interface LoadTripListInputBoundary {

    /**
     * Executes the load trip list use case.
     * @param loadTripListInputData the input data containing username
     */
    void execute(LoadTripListInputData loadTripListInputData);
    void goBack();
}
