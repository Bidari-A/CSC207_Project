package interface_adapter.trip_list;

import interface_adapter.ViewModel;

/**
 * The View Model for the Trip List View.
 */
public class TripListViewModel extends ViewModel<TripListState> {

    public static final String VIEW_NAME = "trip list";

    public TripListViewModel() {
        super(VIEW_NAME);
        setState(new TripListState());
    }

    /**
     * Convenience method for presenters to update the state and notify listeners.
     */
    public void updateState(TripListState newState) {
        setState(new TripListState(newState)); // deep copy
        firePropertyChange();
    }

    @Override
    public void firePropertyChanged() {

    }
}

