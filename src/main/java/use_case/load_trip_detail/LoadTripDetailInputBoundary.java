package use_case.load_trip_detail;

public interface LoadTripDetailInputBoundary {
    /**
     * Load and format trip information based on input data.
     * @param loadTripDetailInputData LoadTripDetailInputData object representing the username, previous view name
     *                                 and tripId (can be Null) of the user
     */
    void execute(LoadTripDetailInputData loadTripDetailInputData);

    /**
     * Brings the user back to the previous view.
     */
    void back();
}
