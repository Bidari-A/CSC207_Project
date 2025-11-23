package interface_adapter.complete_current_trip;

import use_case.complete_current_trip.CompleteCurrentTripInputBoundary;
import use_case.complete_current_trip.CompleteCurrentTripInputData;

public class CompleteCurrentTripController {
    private final CompleteCurrentTripInputBoundary interactor;

    public CompleteCurrentTripController(CompleteCurrentTripInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void execute(String username, String tripName, String city, String date) {
        CompleteCurrentTripInputData inputData =
                new CompleteCurrentTripInputData(username, tripName, city, date);
        interactor.execute(inputData);
    }
}

