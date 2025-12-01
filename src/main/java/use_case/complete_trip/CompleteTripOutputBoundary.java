package use_case.complete_trip;

public interface CompleteTripOutputBoundary {
    void prepareSuccessView(CompleteTripOutputData outputData);
    void prepareFailView(String errorMessage);
}