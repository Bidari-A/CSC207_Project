package use_case.complete_current_trip;

/**
 * Output boundary for the Complete Current Trip Use Case.
 */
public interface CompleteCurrentTripOutputBoundary {
    /**
     * Prepares the success view when the current trip is successfully completed.
     * @param outputData the output data
     */
    void prepareSuccessView(CompleteCurrentTripOutputData outputData);

    /**
     * Prepares the fail view when there is no current trip to complete.
     * @param error the error message
     */
    void prepareFailView(String error);
}

