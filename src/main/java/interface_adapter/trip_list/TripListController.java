package interface_adapter.trip_list;

import use_case.load_trip_list.LoadTripListInputBoundary;
import use_case.load_trip_list.LoadTripListInputData;

/**
 * The controller for the Trip List Use Case.
 */
public class TripListController {

    private final LoadTripListInputBoundary loadTripListUseCaseInteractor;

    public TripListController(LoadTripListInputBoundary loadTripListUseCaseInteractor) {
        this.loadTripListUseCaseInteractor = loadTripListUseCaseInteractor;
    }

    public void goBack() {
        loadTripListUseCaseInteractor.goBack();
    }

    /**
     * Loads trips for the given username.
     * @param username the username
     */
    public void executeLoadTrips(String username) {
        final LoadTripListInputData loadTripListInputData = new LoadTripListInputData(username);
        loadTripListUseCaseInteractor.execute(loadTripListInputData);
    }
}
