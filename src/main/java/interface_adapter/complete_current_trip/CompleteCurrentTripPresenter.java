package interface_adapter.complete_current_trip;

import interface_adapter.logged_in.LoggedInState;
import interface_adapter.logged_in.LoggedInViewModel;
import use_case.complete_current_trip.CompleteCurrentTripOutputdata;
import use_case.complete_current_trip.CompleteCurrentTripOutputBoundary;
import use_case.complete_current_trip.CompleteCurrentTripOutputdata;

/**
 * The Presenter for the Complete Current Trip Use Case.
 */
public class CompleteCurrentTripPresenter implements CompleteCurrentTripOutputBoundary {
    private final LoggedInViewModel loggedInViewModel;

    public CompleteCurrentTripPresenter(LoggedInViewModel loggedInViewModel) {
        this.loggedInViewModel = loggedInViewModel;
    }

    @Override
    public void prepareSuccessView(CompleteCurrentTripOutputdata outputData) {
        System.out.println("CompleteCurrentTripPresenter: prepareSuccessView called");
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
        // For now, we can just log the error or handle it silently
        // The view will remain unchanged if there's an error
        System.err.println("Error completing trip: " + error);
    }
}

