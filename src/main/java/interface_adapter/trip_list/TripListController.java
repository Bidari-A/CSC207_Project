package interface_adapter.trip_list;

import use_case.load_trip_list.LoadTripListInputBoundary;
import use_case.load_trip_list.LoadTripListInputData;
import use_case.delete_trip_list.DeleteTripInputBoundary;
import use_case.delete_trip_list.DeleteTripInputData;

/**
 * The controller for the Trip List Use Case
 * + coordinates Delete Trip Use Case.
 */
public class TripListController {

    private final LoadTripListInputBoundary loadTripListUseCaseInteractor;
    private DeleteTripInputBoundary deleteTripUseCaseInteractor;

    public TripListController(LoadTripListInputBoundary loadTripListUseCaseInteractor) {
        this.loadTripListUseCaseInteractor = loadTripListUseCaseInteractor;
    }

    public void setDeleteTripUseCaseInteractor(DeleteTripInputBoundary interactor) {
        this.deleteTripUseCaseInteractor = interactor;
    }

    public void goBack() {
        loadTripListUseCaseInteractor.goBack();
    }

    public void executeLoadTrips(String username) {
        loadTripListUseCaseInteractor.execute(new LoadTripListInputData(username));
    }

    public void deleteTrip(String username, String tripId) {
        deleteTripUseCaseInteractor.delete(new DeleteTripInputData(username, tripId));
    }
}