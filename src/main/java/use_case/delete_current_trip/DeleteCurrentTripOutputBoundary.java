package use_case.delete_current_trip;

/**
 * Output boundary for the Delete Current Trip Use Case.
 */
public interface DeleteCurrentTripOutputBoundary {
    /**
     * Prepares the success view when the current trip is successfully deleted.
     * @param outputData the output data
     */
    void prepareSuccessView(DeleteCurrentTripOutputData outputData);

    /**
     * Prepares the fail view when there is no current trip to delete.
     * @param error the error message
     */
    void prepareFailView(String error);
}

