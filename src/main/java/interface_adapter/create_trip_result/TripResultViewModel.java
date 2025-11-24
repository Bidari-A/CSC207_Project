package interface_adapter.create_trip_result;

import interface_adapter.ViewModel;

public class TripResultViewModel extends ViewModel<TripResultState> {

    private TripResultState state = new TripResultState();

    public TripResultViewModel() {
        super("trip result");
    }

    public TripResultState getState() {
        return state;
    }

    public void setState(TripResultState state) {
        this.state = state;
        firePropertyChanged();
    }

    public void setTripName(String tripName) {
        state.setTripName(tripName);
        firePropertyChanged();
    }

    public void setFrom(String from) {
        state.setFrom(from);
        firePropertyChanged();
    }

    public void setTo(String to) {
        state.setTo(to);
        firePropertyChanged();
    }

    public void setStartDate(String startDate) {
        state.setStartDate(startDate);
        firePropertyChanged();
    }

    public void setEndDate(String endDate) {
        state.setEndDate(endDate);
        firePropertyChanged();
    }

    public void setItinerary(String itinerary) {
        state.setItinerary(itinerary);
        firePropertyChanged();
    }

    // This matches the overloads you actually have in ViewModel
    public void firePropertyChanged() {
        firePropertyChange("state");
    }
}
