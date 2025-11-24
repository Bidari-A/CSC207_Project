package interface_adapter.trip_list;

import interface_adapter.ViewManagerModel;

import use_case.load_trip_list.LoadTripListOutputBoundary;
import use_case.load_trip_list.LoadTripListOutputData;

import use_case.delete_trip_list.DeleteTripOutputBoundary;
import use_case.delete_trip_list.DeleteTripOutputData;

/**
 * The Presenter for the Trip List Use Cases.
 */
public class TripListPresenter
        implements LoadTripListOutputBoundary, DeleteTripOutputBoundary {

    private final TripListViewModel tripListViewModel;
    private final ViewManagerModel viewManagerModel;

    public TripListPresenter(ViewManagerModel viewManagerModel,
                             TripListViewModel tripListViewModel) {
        this.viewManagerModel = viewManagerModel;
        this.tripListViewModel = tripListViewModel;
    }

    @Override
    public void prepareSuccessView(LoadTripListOutputData outputData) {
        TripListState state = tripListViewModel.getState();
        state.setTrips(outputData.getTrips());
        state.setUsername(outputData.getUsername());
        state.setError(null);
        tripListViewModel.firePropertyChange();

        viewManagerModel.setState(tripListViewModel.getViewName());
        viewManagerModel.firePropertyChange();
    }

    @Override
    public void prepareFailView(String errorMessage) {
        TripListState state = tripListViewModel.getState();
        state.setError(errorMessage);
        tripListViewModel.firePropertyChange();
    }

    @Override
    public void prepareBackView() {
        viewManagerModel.setState("logged in");
        viewManagerModel.firePropertyChange();
    }

    @Override
    public void prepareSuccessView(DeleteTripOutputData outputData) {
        TripListState state = tripListViewModel.getState();
        state.setTrips(outputData.getUpdatedTrips());
        state.setUsername(outputData.getUsername());
        state.setError(null);
        tripListViewModel.firePropertyChange();
    }
}