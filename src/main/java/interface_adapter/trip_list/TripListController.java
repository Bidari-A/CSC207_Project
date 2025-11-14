package interface_adapter.trip_list;

import use_case.delete_trip.DeleteTripInputBoundary;
import use_case.delete_trip.DeleteTripInputData;

/**
 * The control for the Trip List Use Case.
 * Note: Trip list loading logic has been removed - use ViewModel directly.
 */
public class TripListController {

    private final DeleteTripInputBoundary deleteTripUseCaseInteractor;

    public TripListController(DeleteTripInputBoundary deleteTripUseCaseInteractor) {
        this.deleteTripUseCaseInteractor = deleteTripUseCaseInteractor;
    }

    /**
     * Executes the Trip List Use Case to load all trips.
     * Note: This method is now a no-op. Set trips directly in TripListViewModel.
     * @param username the username of the user
     */
    public void execute(String username) {
        // Use case logic removed - set trips directly in ViewModel
    }

    /**
     * Executes the Delete Trip Use Case.
     * @param username the username of the user
     * @param tripName the name of the trip to delete
     */
    public void executeDelete(String username, String tripName) {
        final DeleteTripInputData deleteTripInputData = new DeleteTripInputData(username, tripName);
        deleteTripUseCaseInteractor.execute(deleteTripInputData);
    }
}
