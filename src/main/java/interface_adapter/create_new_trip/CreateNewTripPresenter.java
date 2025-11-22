package interface_adapter.create_new_trip;

import interface_adapter.ViewManagerModel;
import use_case.create_new_trip.*;

public class CreateNewTripPresenter implements CreateNewTripOutputBoundary {

    private final ViewManagerModel viewManagerModel;
    private final CreateNewTripViewModel createNewTripViewModel;

    public CreateNewTripPresenter(ViewManagerModel viewManagerModel, CreateNewTripViewModel createNewTripViewModel) {
        this.viewManagerModel = viewManagerModel;
        this.createNewTripViewModel = createNewTripViewModel;
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

    @Override
    public void presentSavedDraft(CreateNewTripOutputData data) {
        CreateNewTripState state = createNewTripViewModel.getState();
        state.setFrom(data.getFrom());
        state.setTo(data.getTo());
        state.setDate(data.getDate());
        state.setPlanText("Draft saved.");
        state.setError("");

        createNewTripViewModel.firePropertyChange();
    }

    @Override
    public void presentSubmittedTrip(CreateNewTripOutputData data) {
        CreateNewTripState state = createNewTripViewModel.getState();
        state.setFrom("");
        state.setTo("");
        state.setDate("");
        state.setPlanText("");
        state.setError("");

        viewManagerModel.setState("logged in");
        viewManagerModel.firePropertyChange();
    }

    @Override
    public void presentError(String message) {
        CreateNewTripState state = createNewTripViewModel.getState();
        state.setError(message);
        createNewTripViewModel.firePropertyChange();
    }
}
