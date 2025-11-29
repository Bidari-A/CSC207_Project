package use_case.delete_current_trip;

/**
 * Input boundary for the Delete Current Trip Use Case.
 */
public interface DeleteCurrentTripInputBoundary {
    /**
     * Executes the delete current trip use case.
     * @param inputData the input data containing the username
     */
    void execute(DeleteCurrentTripInputData inputData);
}

