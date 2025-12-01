package interface_adapter.flight_search;
import interface_adapter.ViewModel;

// We just copy and modify how the CA Login ViewModel Looks like
public class FlightSearchViewModel extends ViewModel<FlightSearchState>{
    public FlightSearchViewModel(){
        super("flight search");
        setState(new FlightSearchState());
    }

    @Override
    public void firePropertyChanged() {

    }
}
