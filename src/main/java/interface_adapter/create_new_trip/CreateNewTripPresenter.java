package interface_adapter.create_new_trip;

import interface_adapter.ViewManagerModel;
import use_case.create_new_trip.*;
import interface_adapter.trip.*;

import javax.swing.*;

public class CreateNewTripPresenter implements CreateNewTripOutputBoundary {

    private final ViewManagerModel viewManagerModel;
    private final CreateNewTripViewModel createNewTripViewModel;
    private final TripViewModel tripViewModel;

    public CreateNewTripPresenter(ViewManagerModel viewManagerModel, CreateNewTripViewModel createNewTripViewModel, TripViewModel tripViewModel) {
        this.viewManagerModel = viewManagerModel;
        this.createNewTripViewModel = createNewTripViewModel;
        this.tripViewModel = tripViewModel;
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
        // Update CreateNewTripViewModel
        createNewTripViewModel.setState(createNewTripViewModel.getState());

        // Update TripViewModel only if available
        if (tripViewModel != null) {
            var tripState = tripViewModel.getState();
            tripState.setTripName(data.getFrom() + " to " + data.getTo());
            tripState.setCity(data.getTo());
            tripState.setDate(data.getDate());
            tripViewModel.firePropertyChange();
        }

        // Optionally switch view if TripViewModel exists
        if (tripViewModel != null && viewManagerModel != null) {
            viewManagerModel.setState(tripViewModel.getViewName());
            viewManagerModel.firePropertyChange();
        }
    }


    @Override
    public void presentError(String message) {
        CreateNewTripState state = createNewTripViewModel.getState();
        state.setError(message);
        createNewTripViewModel.firePropertyChange();
    }
}
