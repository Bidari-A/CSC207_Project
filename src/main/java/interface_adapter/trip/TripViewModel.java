package interface_adapter.trip;

import interface_adapter.ViewModel;

/**
 * The View Model for the Login View.
 */
public class TripViewModel extends ViewModel<TripState> {

    public TripViewModel() {
        super("trip detail");
        setState(new TripState());
    }

}
