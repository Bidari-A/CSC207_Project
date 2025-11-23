package use_case.complete_current_trip;

public interface CompleteCurrentTripOutputBoundary {
    void presentCompletedTrip(CompleteCurrentTripOutputData outputData);
    void presentError(String error);
}
