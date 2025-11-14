package interface_adapter.trip_list;

import interface_adapter.ViewManagerModel;
import use_case.trip_list.TripListOutputBoundary;
import use_case.trip_list.TripListOutputData;

/**
 * The Presenter for the Trip List Use Case.
 */
public class TripListPresenter implements TripListOutputBoundary {

    private final TripListViewModel tripListViewModel;
    private final ViewManagerModel viewManagerModel;

    public TripListPresenter(ViewManagerModel viewManagerModel,
                             TripListViewModel tripListViewModel) {
        this.viewManagerModel = viewManagerModel;
        this.tripListViewModel = tripListViewModel;
    }

    @Override
    public void prepareSuccessView(TripListOutputData response) {
        final TripListState tripListState = tripListViewModel.getState();
        tripListState.setTrips(response.getTrips());
        tripListState.setUsername(response.getUsername());
        tripListState.setError(null);
        this.tripListViewModel.firePropertyChange();
        
        // Navigate to trip list view
        this.viewManagerModel.setState(tripListViewModel.getViewName());
        this.viewManagerModel.firePropertyChange();
    }

    @Override
    public void prepareFailView(String error) {
        final TripListState tripListState = tripListViewModel.getState();
        tripListState.setError(error);
        tripListViewModel.firePropertyChange();
    }
}

