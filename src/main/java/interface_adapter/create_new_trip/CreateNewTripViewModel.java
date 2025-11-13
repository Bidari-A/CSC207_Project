package interface_adapter.create_new_trip;

import interface_adapter.ViewModel;

public class CreateNewTripViewModel extends ViewModel<CreateNewTripState> {

    public CreateNewTripViewModel() {
        super("create new trip");
        setState(new CreateNewTripState());
    }
}