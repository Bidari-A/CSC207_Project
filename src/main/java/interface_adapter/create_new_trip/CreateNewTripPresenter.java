package interface_adapter.create_new_trip;

import interface_adapter.ViewManagerModel;
import use_case.create_new_trip.CreateNewTripOutputBoundary;
import interface_adapter.create_new_trip.CreateNewTripViewModel;

public class CreateNewTripPresenter implements CreateNewTripOutputBoundary {

    private final ViewManagerModel viewManagerModel;
    private final CreateNewTripViewModel createNewTripViewModel;

    public CreateNewTripPresenter(ViewManagerModel viewManagerModel, CreateNewTripViewModel createNewTripViewModel) {
        this.viewManagerModel = viewManagerModel;
        this.createNewTripViewModel = createNewTripViewModel;
    }

    @Override
    public void prepareScreen() {
        // This string must match CreateNewTripView.getViewName()


        // 1. Reset state
        createNewTripViewModel.setState(new CreateNewTripState());


        // 2. Switch view
        viewManagerModel.setState(createNewTripViewModel.getViewName());
        viewManagerModel.firePropertyChange();

    }

    /**
     * NEW: Navigate back to the logged-in view.
     */

    @Override
    public void prepareBackView() {
        viewManagerModel.setState("logged in");
        viewManagerModel.firePropertyChange();
    }

}
