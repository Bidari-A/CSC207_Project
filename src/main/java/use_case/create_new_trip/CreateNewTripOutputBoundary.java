package use_case.create_new_trip;

public interface CreateNewTripOutputBoundary {
    // Called when user opens the Create New Trip form
    void prepareScreen();
    void prepareBackView();

    // called when the use case has generated a trip result via gemini
    void presentResult(CreateNewTripOutputData outputData);
}
