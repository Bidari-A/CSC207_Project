package interface_adapter.trip;

import use_case.load_trip_detail.LoadTripDetailInputBoundary;
import use_case.load_trip_detail.LoadTripDetailInputData;

public class TripController {
    private final LoadTripDetailInputBoundary loadTripDetailInputBoundary;

    public TripController(LoadTripDetailInputBoundary loadTripDetailInputBoundary) {
        this.loadTripDetailInputBoundary = loadTripDetailInputBoundary;
    }

    /**
     * Prepare trip view with information from the User's current Trip object.
     * @param username String representing the username of the user
     * @param prevViewName String representing the previous view the user was on
     */
    public void execute(String username, String prevViewName) {
        final LoadTripDetailInputData loadTripDetailInputData = new LoadTripDetailInputData(username, prevViewName);
        loadTripDetailInputBoundary.execute(loadTripDetailInputData);
    }

    /**
     * Prepare trip view with information from the Trip.
     * @param username String representing the username of the user
     * @param prevViewName String representing the previous view the user was on
     * @param tripId String representing the tripId value of the desired Trip
     */
    public void execute(String username, String prevViewName, String tripId) {
        final LoadTripDetailInputData loadTripDetailInputData = new LoadTripDetailInputData(username,
                prevViewName, tripId);
        loadTripDetailInputBoundary.execute(loadTripDetailInputData);
    }

    /**
     * Bring user back to previous view.
     */
    public void back() {
        loadTripDetailInputBoundary.back();
    }
}
