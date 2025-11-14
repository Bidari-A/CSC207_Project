package interface_adapter.trip_list;

import use_case.trip_list.TripListInputBoundary;
import use_case.trip_list.TripListInputData;
import use_case.delete_trip.DeleteTripInputBoundary;
import use_case.delete_trip.DeleteTripInputData;

/**
 * The control for the Trip List Use Case.
 */
public class TripListController {

    private final TripListInputBoundary tripListUseCaseInteractor;
    private final DeleteTripInputBoundary deleteTripUseCaseInteractor;

    public TripListController(TripListInputBoundary tripListUseCaseInteractor,
                              DeleteTripInputBoundary deleteTripUseCaseInteractor) {
        this.tripListUseCaseInteractor = tripListUseCaseInteractor;
        this.deleteTripUseCaseInteractor = deleteTripUseCaseInteractor;
    }

    /**
     * Executes the Trip List Use Case to load all trips.
     * @param username the username of the user
     */
    public void execute(String username) {
        final TripListInputData tripListInputData = new TripListInputData(username);
        tripListUseCaseInteractor.execute(tripListInputData);
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
