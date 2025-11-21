package use_case.load_trip;

/**
 * The output boundary for the Load Trip List Use Case.
 */
public interface LoadTripOutputBoundary {
    /**
     * Prepares the success view for the Load Trip Use Case.
     * @param outputData the output data containing trips and username
     */
    void prepareSuccessView(LoadTripOutputData outputData);

    /**
     * Prepares the failure view for the Load Trip List Use Case.
     * @param errorMessage the explanation of the failure
     */
    void prepareFailView(String errorMessage);
    void prepareBackView();
}
