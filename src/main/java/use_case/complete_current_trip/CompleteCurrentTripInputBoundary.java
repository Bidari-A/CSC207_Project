package use_case.complete_current_trip;

/**
 * Input boundary for the Complete Current Trip Use Case.
 */
public interface CompleteCurrentTripInputBoundary {
    /**
     * Executes the complete current trip use case.
     * @param inputData the input data containing the username
     */
    void execute(CompleteCurrentTripInputData inputData);
}

