package interface_adapter.trip_list;

import use_case.load_trip_list.LoadTripListInputBoundary;
import use_case.load_trip_list.LoadTripListInputData;

import use_case.delete_trip_list.DeleteTripInputBoundary;
import use_case.delete_trip_list.DeleteTripInputData;

/**
 * Controller for the Trip List View.
 */
public class TripListController {

    private final LoadTripListInputBoundary loadTripListUseCaseInteractor;

    private final DeleteTripInputBoundary deleteTripUseCaseInteractor;

    public TripListController(LoadTripListInputBoundary loadTripListUseCaseInteractor,
                              DeleteTripInputBoundary deleteTripUseCaseInteractor) {
        this.loadTripListUseCaseInteractor = loadTripListUseCaseInteractor;
        this.deleteTripUseCaseInteractor = deleteTripUseCaseInteractor;
    }

    public void goBack() {
        loadTripListUseCaseInteractor.goBack();
    }

    public void executeLoadTrips(String username) {
        loadTripListUseCaseInteractor.execute(new LoadTripListInputData(username));
    }

    public void deleteTrip(String tripName, String username) {
        deleteTripUseCaseInteractor.delete(new DeleteTripInputData(tripName, username));
    }
}