package interface_adapter.complete_trip;

import interface_adapter.logged_in.LoggedInState;
import interface_adapter.logged_in.LoggedInViewModel;
import use_case.complete_trip.CompleteTripOutputBoundary;
import use_case.complete_trip.CompleteTripOutputData;

/**
 * Presenter for the Complete Trip use case.
 * Updates the LoggedInViewModel and fires property changes so the dashboard refreshes.
 */
public class CompleteTripPresenter implements CompleteTripOutputBoundary {

    private final LoggedInViewModel loggedInViewModel;

    public CompleteTripPresenter(LoggedInViewModel loggedInViewModel) {
        this.loggedInViewModel = loggedInViewModel;
    }

    @Override
    public void prepareSuccessView(CompleteTripOutputData outputData) {
        // Get the current state
        LoggedInState state = loggedInViewModel.getState();

        // We clear the current trip fields from the dashboard
        state.setCurrentTripName("");
        state.setCityName("");
        state.setDate("");

        // Optional: If you track the "lastCompletedTripName", update it here
        state.setLastCompletedTripName(outputData.getTripName());

        // Push updated state into the ViewModel
        loggedInViewModel.setState(state);

        // Notify UI
        loggedInViewModel.firePropertyChange("state");
    }

    @Override
    public void prepareFailView(String errorMessage) {
        LoggedInState state = loggedInViewModel.getState();
        state.setErrorMessage(errorMessage);

        loggedInViewModel.setState(state);
        loggedInViewModel.firePropertyChange("state");
    }
}