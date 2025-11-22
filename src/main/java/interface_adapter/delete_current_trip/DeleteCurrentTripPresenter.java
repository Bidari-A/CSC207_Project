package interface_adapter.delete_current_trip;

import interface_adapter.ViewManagerModel;
import interface_adapter.ViewModel;
import interface_adapter.logged_in.LoggedInState;
import interface_adapter.logged_in.LoggedInViewModel;
import use_case.delete_current_trip.DeleteCurrentTripInteractor;
import use_case.delete_current_trip.DeleteCurrentTripOutputBoundary;
import use_case.delete_current_trip.DeleteCurrentTripOutputData;
import view.ViewManager;

public class DeleteCurrentTripPresenter implements DeleteCurrentTripOutputBoundary {
    private final DeleteCurrentTripViewModel deleteCurrentTripViewModel;
    private final ViewManagerModel viewManagerModel;
    private final LoggedInViewModel loggedInViewModel;

    public DeleteCurrentTripPresenter(
            DeleteCurrentTripViewModel deleteCurrentTripViewModel,
            ViewManagerModel viewManagerModel,
            LoggedInViewModel loggedInViewModel) {
        this.deleteCurrentTripViewModel = deleteCurrentTripViewModel;
        this.viewManagerModel = viewManagerModel;
        this.loggedInViewModel = loggedInViewModel;
    }

    @Override
    public void prepareSuccessView(DeleteCurrentTripOutputData deleteCurrentTripOutputData) {
        // 1. Clear the dashboard (remove last created trip)
        LoggedInState state = loggedInViewModel.getState();
        state.setLastCreatedTrip(null);
        loggedInViewModel.setState(state);
        loggedInViewModel.firePropertyChanged();

        // 2. Switch back to dashboard
        viewManagerModel.setActiveView(loggedInViewModel.getViewName());
        viewManagerModel.firePropertyChanged();
    }
}
