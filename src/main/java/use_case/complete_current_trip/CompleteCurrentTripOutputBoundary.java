package use_case.complete_current_trip;

public interface CompleteCurrentTripOutputBoundary {
    void prepareSuccessView(CompleteCurrentTripOutputdata outputData);
    void prepareFailView(String error);
}
