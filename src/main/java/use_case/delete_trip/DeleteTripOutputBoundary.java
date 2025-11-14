package use_case.delete_trip;

/**
 * Output boundary for the Delete Trip Use Case.
 */

public interface DeleteTripOutputBoundary {
    void prepareSuccessView(DeleteTripOutputData deleteTripOutputData);
    void prepareFailView(String error);
}
