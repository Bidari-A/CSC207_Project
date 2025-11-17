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

    /**
     * Loads trips for the given username.
     * @param username the username
     */
    public void executeLoadTrips(String username) {
        final LoadTripListInputData loadTripListInputData = new LoadTripListInputData(username);
        loadTripListUseCaseInteractor.execute(loadTripListInputData);
    }

    /**
     * Navigates to trip details view.
     * @param username the username
     */
    public void executeDetails(String username) {
        // TODO: Implement details functionality when needed
        // This will navigate to a specific trip's detail view
    }

    /**
     * Deletes a trip for the given username.
     * @param username the username
     */
    public void executeDelete(String username) {
        // TODO: Implement delete functionality when needed
        // This will delete a trip and reload the trip list
    }
}
