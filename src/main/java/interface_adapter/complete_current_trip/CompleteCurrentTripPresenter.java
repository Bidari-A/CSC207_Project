package interface_adapter.complete_current_trip;

import javax.swing.JOptionPane;

import interface_adapter.logged_in.LoggedInState;
import interface_adapter.logged_in.LoggedInViewModel;
import use_case.complete_current_trip.CompleteCurrentTripOutputBoundary;
import use_case.complete_current_trip.CompleteCurrentTripOutputData;

/**
 * The Presenter for the Complete Current Trip Use Case.
 */
public class CompleteCurrentTripPresenter implements CompleteCurrentTripOutputBoundary {
    private final LoggedInViewModel loggedInViewModel;

    public CompleteCurrentTripPresenter(LoggedInViewModel loggedInViewModel) {
        this.loggedInViewModel = loggedInViewModel;
    }

    @Override
    public void prepareSuccessView(CompleteCurrentTripOutputData outputData) {
        // Update the LoggedInState to show "N/A" for all trip info
        LoggedInState state = loggedInViewModel.getState();
        state.setCurrentTripName("N/A");
        state.setCityName("N/A");
        state.setDate("N/A");
        loggedInViewModel.firePropertyChange();

        // Show success message
        JOptionPane.showMessageDialog(null, "Current trip completed successfully.");
    }

    @Override
    public void prepareFailView(String error) {
        // Show error message
        JOptionPane.showMessageDialog(null, error, "Error", JOptionPane.ERROR_MESSAGE);
    }
}

