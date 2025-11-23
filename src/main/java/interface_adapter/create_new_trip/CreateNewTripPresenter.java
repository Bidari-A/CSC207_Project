package interface_adapter.create_new_trip;

import interface_adapter.ViewManagerModel;
import use_case.create_new_trip.CreateNewTripOutputBoundary;

public class CreateNewTripPresenter implements CreateNewTripOutputBoundary {

    private final ViewManagerModel viewManagerModel;

    public CreateNewTripPresenter(ViewManagerModel viewManagerModel) {
        this.viewManagerModel = viewManagerModel;
    }

    @Override
    public void showCreateNewTripView() {
        // This string must match CreateNewTripView.getViewName()
        viewManagerModel.setState("create new trip");
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
