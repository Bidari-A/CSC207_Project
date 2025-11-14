package interface_adapter.trip_list;

import interface_adapter.ViewManagerModel;
import entity.Trip;
import java.util.List;

/**
 * The Presenter for the Trip List Use Case.
 * Note: Use case interfaces removed - use helper methods to update ViewModel directly.
 */
public class TripListPresenter {

    private final TripListViewModel tripListViewModel;
    private final ViewManagerModel viewManagerModel;

    public TripListPresenter(ViewManagerModel viewManagerModel,
                             TripListViewModel tripListViewModel) {
        this.viewManagerModel = viewManagerModel;
        this.tripListViewModel = tripListViewModel;
    }

    /**
     * Updates the trip list view with trips.
     * @param trips the list of trips to display
     * @param username the username
     */
    public void prepareSuccessView(List<Trip> trips, String username) {
        final TripListState tripListState = tripListViewModel.getState();
        tripListState.setTrips(trips);
        tripListState.setUsername(username);
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
    public void prepareFailView(String error) {
        final TripListState tripListState = tripListViewModel.getState();
        tripListState.setError(error);
        tripListViewModel.firePropertyChange();
    }
}

