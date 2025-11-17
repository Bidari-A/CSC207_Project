package use_case.load_trip_list;

/**
 * The output boundary for the Load Trip List Use Case.
 */
public interface LoadTripListOutputBoundary {
    /**
     * Prepares the success view for the Load Trip List Use Case.
     * @param outputData the output data containing trips and username
     */
    void prepareSuccessView(LoadTripListOutputData outputData);

    /**
     * Prepares the failure view for the Load Trip List Use Case.
     * @param errorMessage the explanation of the failure
     */
    void prepareFailView(String errorMessage);
    void prepareBackView();
}
