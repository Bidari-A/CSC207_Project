package interface_adapter.trip;

import interface_adapter.ViewModel;

/**
 * The View Model for the Login View.
 */
public class TripViewModel extends ViewModel<TripState> {

    // New field to track the currently loaded trip ID
    private String currentTripId;

    public TripViewModel() {
        super("trip detail");
        setState(new TripState());
    }

    /** Getter for current trip ID */
    public String getCurrentTripId() {
        return currentTripId;
    }

    /** Setter for current trip ID */
    public void setCurrentTripId(String tripId) {
        this.currentTripId = tripId;
        firePropertyChange("currentTripId");
    }

    @Override
    public void firePropertyChanged() {

    }
}
