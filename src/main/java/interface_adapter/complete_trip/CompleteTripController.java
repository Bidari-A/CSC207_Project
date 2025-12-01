package interface_adapter.complete_trip;

import use_case.complete_trip.CompleteTripInputBoundary;
import use_case.complete_trip.CompleteTripInputData;

public class CompleteTripController {

    private final CompleteTripInputBoundary interactor;

    public CompleteTripController(CompleteTripInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void execute(String username) {
        CompleteTripInputData data = new CompleteTripInputData(username);
        interactor.execute(data);
    }
}