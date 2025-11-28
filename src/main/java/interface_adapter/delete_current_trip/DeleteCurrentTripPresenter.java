package interface_adapter.delete_current_trip;

import javax.swing.JOptionPane;

import interface_adapter.logged_in.LoggedInState;
import interface_adapter.logged_in.LoggedInViewModel;
import use_case.delete_current_trip.DeleteCurrentTripOutputBoundary;
import use_case.delete_current_trip.DeleteCurrentTripOutputData;

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
        System.out.println("DeleteCurrentTripPresenter: prepareSuccessView called");
        // Clear the current trip info from LoggedInState and set placeholder values
        final LoggedInState loggedInState = loggedInViewModel.getState();

        // Create a new state object to ensure property change is detected
        LoggedInState newState = new LoggedInState();
        newState.setUsername(loggedInState.getUsername());
        newState.setPassword(loggedInState.getPassword());
        // Set placeholder values instead of empty strings
        newState.setCurrentTripName("No current trip");
        newState.setCityName("N/A");
        newState.setDate("N/A");

        System.out.println("Setting new state with placeholder trip info");
        loggedInViewModel.setState(newState);
        loggedInViewModel.firePropertyChange();
        System.out.println("Property change fired");
    }

    @Override
    public void prepareFailView(String error) {
        System.err.println("Error deleting trip: " + error);
        // Show warning dialog to user
        JOptionPane.showMessageDialog(
                null,
                error,
                "Delete Trip Error",
                JOptionPane.WARNING_MESSAGE
        );
    }
}

