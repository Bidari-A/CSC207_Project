package interface_adapter.hotel_search;

import interface_adapter.ViewModel;
import java.beans.PropertyChangeListener;

public class HotelSearchViewModel extends ViewModel<HotelSearchState> {

    public HotelSearchViewModel() {
        super("hotel search");
        setState(new HotelSearchState());
    }

    @Override
    public void setState(HotelSearchState state) {
        super.setState(state);
        firePropertyChanged();
    }

    public void firePropertyChanged() {
        super.firePropertyChange("state");
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        super.addPropertyChangeListener(listener);
    }
}