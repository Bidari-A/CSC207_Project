package interface_adapter.create_new_trip;

import interface_adapter.ViewManagerModel;
import interface_adapter.logged_in.LoggedInViewModel;
import interface_adapter.trip.TripViewModel;
import use_case.create_new_trip.CreateNewTripOutputBoundary;
import use_case.create_new_trip.CreateNewTripOutputData;

public class CreateNewTripPresenter implements CreateNewTripOutputBoundary {

    private final ViewManagerModel viewManagerModel;
    private final CreateNewTripViewModel createNewTripViewModel;
    private final TripViewModel tripViewModel;
    private final LoggedInViewModel loggedInViewModel;

    public CreateNewTripPresenter(ViewManagerModel viewManagerModel, CreateNewTripViewModel createNewTripViewModel, TripViewModel tripViewModel, LoggedInViewModel loggedInViewModel) {
        this.viewManagerModel = viewManagerModel;
        this.createNewTripViewModel = createNewTripViewModel;
        this.tripViewModel = tripViewModel;
        this.loggedInViewModel = loggedInViewModel;
    }

    @Override
    public void showCreateNewTripView() {
        // Clear the form state when showing the view
        CreateNewTripState state = createNewTripViewModel.getState();
        state.setFrom("");
        state.setTo("");
        state.setDate("");
        state.setPlanText("");
        state.setError("");
        createNewTripViewModel.firePropertyChange();

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

        // Update LoggedInState with draft trip info for dashboard display
        if (loggedInViewModel != null) {
            var loggedInState = loggedInViewModel.getState();
            String tripName = data.getFrom() + " to " + data.getTo();
            loggedInState.setDraftTrip(tripName, data.getTo(), data.getDate());
            loggedInViewModel.firePropertyChange();
        }

        // Update TripViewModel only if available
        if (tripViewModel != null) {
            var tripState = tripViewModel.getState();
            tripState.setTripName(data.getFrom() + " to " + data.getTo());
            tripState.setCity(data.getTo());
            tripState.setDate(data.getDate());
            // Clear attractions, flights, and hotels for new trips (they haven't been added yet)
            tripState.setAttractions("No attractions added yet");
            tripState.setFlightDetails("No flights added yet");
            tripState.setHotelDetails("No accommodations added yet");
            // Set prevViewName to "logged in" so back button works
            tripState.setPrevViewName("logged in");
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
