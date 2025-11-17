package interface_adapter.trip_list;

import interface_adapter.ViewManagerModel;
import use_case.load_trip_list.LoadTripListOutputBoundary;
import use_case.load_trip_list.LoadTripListOutputData;

/**
 * The Presenter for the Trip List Use Case.
 */
public class TripListPresenter implements LoadTripListOutputBoundary {

    private final TripListViewModel tripListViewModel;
    private final ViewManagerModel viewManagerModel;

    public TripListPresenter(ViewManagerModel viewManagerModel,
                             TripListViewModel tripListViewModel) {
        this.viewManagerModel = viewManagerModel;
        this.tripListViewModel = tripListViewModel;
    }

    /**
     * Updates the trip list view with trips.
     * @param outputData the output data containing trips and username
     */
    @Override
    public void prepareSuccessView(LoadTripListOutputData outputData) {
        final TripListState tripListState = tripListViewModel.getState();
        tripListState.setTrips(outputData.getTrips());
        tripListState.setUsername(outputData.getUsername());
        tripListState.setError(null);
        this.tripListViewModel.firePropertyChange();

        // Navigate to trip list view
        this.viewManagerModel.setState(tripListViewModel.getViewName());
        this.viewManagerModel.firePropertyChange();
    }

    /**
     * Displays an error in the trip list view.
     * @param error the error message
     */
    @Override
    public void prepareFailView(String error) {
        final TripListState tripListState = tripListViewModel.getState();
        tripListState.setError(error);
        tripListViewModel.firePropertyChange();
    }
}

