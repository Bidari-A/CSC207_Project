package interface_adapter.trip;

import use_case.load_trip_detail.LoadTripDetailInputBoundary;
import use_case.load_trip_detail.LoadTripDetailInputData;

public class TripController {
    private final LoadTripDetailInputBoundary loadTripDetailInputBoundary;

    public TripController(LoadTripDetailInputBoundary loadTripDetailInputBoundary) {
        this.loadTripDetailInputBoundary = loadTripDetailInputBoundary;
    }

    public void execute(String username, String prevViewName) {
        final LoadTripDetailInputData loadTripDetailInputData = new LoadTripDetailInputData(username, prevViewName);
        loadTripDetailInputBoundary.execute(loadTripDetailInputData);
    }
    public void back() {
        loadTripDetailInputBoundary.back();
    }
}
