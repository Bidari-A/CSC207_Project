package interface_adapter.trip_list;

import interface_adapter.trip.TripController;
import use_case.delete_trip.DeleteTripDataAccessInterface;
import use_case.load_trip_list.LoadTripListInputBoundary;
import use_case.load_trip_list.LoadTripListInputData;

/**
 * The controller for the Trip List Use Case.
 */
public class TripListController {

    private final LoadTripListInputBoundary loadTripListUseCaseInteractor;
    private TripController tripController;
    private DeleteTripDataAccessInterface deleteTripDataAccessObject;

    public TripListController(LoadTripListInputBoundary loadTripListUseCaseInteractor) {
        this.loadTripListUseCaseInteractor = loadTripListUseCaseInteractor;
    }

    public void setDeleteTripDataAccessObject(DeleteTripDataAccessInterface deleteTripDataAccessObject) {
        this.deleteTripDataAccessObject = deleteTripDataAccessObject;
    }

    public void setTripController(TripController tripController) {
        this.tripController = tripController;
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

    /**
     * Navigates to trip details view.
     * @param username the username
     * @param tripName the name of the trip to view
     */
    public void executeDetails(String username, String tripName) {
        if (tripController != null) {
            // Navigate to trip detail view with "trip list" as the previous view and the trip name
            tripController.execute(username, "trip list", tripName);
        }
    }

    /**
     * Deletes a trip for the given username.
     * @param username the username
     * @param tripName the name of the trip to delete
     */
    public void executeDelete(String username, String tripName) {
        // Delete the trip using the data access object
        if (deleteTripDataAccessObject != null) {
            deleteTripDataAccessObject.deleteTrip(username, tripName);
        }
        // Reload the trip list after deletion
        executeLoadTrips(username);
    }
}
