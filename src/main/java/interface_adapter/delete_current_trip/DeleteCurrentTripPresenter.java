package interface_adapter.delete_current_trip;

import interface_adapter.ViewManagerModel;
import interface_adapter.logged_in.LoggedInState;
import interface_adapter.logged_in.LoggedInViewModel;
import use_case.delete_current_trip.DeleteCurrentTripOutputBoundary;

public class DeleteCurrentTripPresenter implements DeleteCurrentTripOutputBoundary {

    private final LoggedInViewModel loggedInViewModel;

    public DeleteCurrentTripPresenter(LoggedInViewModel loggedInViewModel) {
        this.loggedInViewModel = loggedInViewModel;
    }

    @Override
    public void presentClearedDraft() {
        LoggedInState state = loggedInViewModel.getState();
        state.clearDraft();
        loggedInViewModel.firePropertyChange();
    }
}
