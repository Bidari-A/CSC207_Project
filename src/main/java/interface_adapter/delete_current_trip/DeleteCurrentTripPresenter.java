package interface_adapter.delete_current_trip;

import interface_adapter.logged_in.LoggedInState;
import interface_adapter.logged_in.LoggedInViewModel;
import use_case.delete_current_trip.DeleteCurrentTripOutputBoundary;
import use_case.delete_current_trip.DeleteCurrentTripOutputData;

import javax.swing.*;

/**
 * The Presenter for the Delete Current Trip Use Case.
 */
public class DeleteCurrentTripPresenter implements DeleteCurrentTripOutputBoundary {
    private final LoggedInViewModel loggedInViewModel;

    public DeleteCurrentTripPresenter(LoggedInViewModel loggedInViewModel) {
        this.loggedInViewModel = loggedInViewModel;
    }

    @Override
    public void prepareSuccessView(DeleteCurrentTripOutputData outputData) {
        // Update the LoggedInState to show "N/A" for all trip info
        LoggedInState state = loggedInViewModel.getState();
        state.setCurrentTripName("N/A");
        state.setCityName("N/A");
        state.setDate("N/A");
        loggedInViewModel.firePropertyChange();

        // Show success message
        JOptionPane.showMessageDialog(null, "Current trip deleted successfully. There is no current trip.");
    }

    @Override
    public void prepareFailView(String error) {
        // Show error message
        JOptionPane.showMessageDialog(null, error, "Error", JOptionPane.ERROR_MESSAGE);
    }
}

