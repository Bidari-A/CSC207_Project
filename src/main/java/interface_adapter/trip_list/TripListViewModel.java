package interface_adapter.trip_list;

import interface_adapter.ViewModel;

/**
 * ViewModel for Trip List View.
 */
public class TripListViewModel extends ViewModel<TripListState> {

    public TripListViewModel() {
        super("trip list");
        setState(new TripListState());
    }
}