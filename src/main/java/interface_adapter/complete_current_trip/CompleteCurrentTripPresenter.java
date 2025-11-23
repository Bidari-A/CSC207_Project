package interface_adapter.complete_current_trip;

import interface_adapter.logged_in.LoggedInState;
import interface_adapter.logged_in.LoggedInViewModel;
import use_case.complete_current_trip.CompleteCurrentTripOutputBoundary;
import use_case.complete_current_trip.CompleteCurrentTripOutputData;

public class CompleteCurrentTripPresenter implements CompleteCurrentTripOutputBoundary {
    private final LoggedInViewModel loggedInViewModel;

    public CompleteCurrentTripPresenter(LoggedInViewModel loggedInViewModel) {
        this.loggedInViewModel = loggedInViewModel;
    }

    @Override
    public void presentCompletedTrip(CompleteCurrentTripOutputData outputData) {
        // Clear the draft trip from the dashboard
        LoggedInState state = loggedInViewModel.getState();
        state.clearDraft();
        loggedInViewModel.firePropertyChange();
    }

    @Override
    public void presentError(String error) {
        // For now, just clear the draft on error as well
        // In a full implementation, you might want to show an error message
        LoggedInState state = loggedInViewModel.getState();
        state.clearDraft();
        loggedInViewModel.firePropertyChange();
    }
}

