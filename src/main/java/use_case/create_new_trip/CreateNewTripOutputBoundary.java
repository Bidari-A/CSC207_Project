package use_case.create_new_trip;

public interface CreateNewTripOutputBoundary {
    void showCreateNewTripView();
    void prepareBackView();
    void presentSavedDraft(CreateNewTripOutputData outputData);
    void presentSubmittedTrip(CreateNewTripOutputData outputData);
    void presentError(String message);
}
