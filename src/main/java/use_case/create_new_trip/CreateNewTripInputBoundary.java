
package use_case.create_new_trip;

public interface CreateNewTripInputBoundary {
    void execute(CreateNewTripInputData inputData);
    void prepareScreen();
    void goBack();
}