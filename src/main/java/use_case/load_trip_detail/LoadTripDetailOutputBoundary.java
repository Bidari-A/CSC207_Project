package use_case.load_trip_detail;

public interface LoadTripDetailOutputBoundary {
    /**
     * Tell the presenter to prepare the Trip View with the formatted information.
     * @param outputData LoadTripDetailOutputData object representing the formatted information
     */
    void prepareTripView(LoadTripDetailOutputData outputData);

    /**
     * Bring back the user to the previous view.
     */
    void back();
}
