package interface_adapter.complete_trip;

import interface_adapter.logged_in.LoggedInViewModel;
import interface_adapter.logged_in.LoggedInState;     /***** ADDED *****/
import use_case.complete_trip.CompleteTripOutputBoundary;
import use_case.complete_trip.CompleteTripOutputData;

public class CompleteTripPresenter implements CompleteTripOutputBoundary {

    private final LoggedInViewModel loggedInViewModel;

    public CompleteTripPresenter(LoggedInViewModel loggedInViewModel) {
        this.loggedInViewModel = loggedInViewModel;
    }

    @Override
    public void prepareSuccessView(CompleteTripOutputData data) {

        /***** Retrieve current state from ViewModel *****/
        LoggedInState state = loggedInViewModel.getState();

        /***** Update state fields instead of ViewModel setters *****/
        state.setTripStatus("COMPLETED");
        state.setLastCompletedTripId(data.getTripId());
        state.setErrorMessage(null);

        /***** Push updated state back into ViewModel — triggers propertyChange *****/
        loggedInViewModel.setState(state);
    }

    @Override
    public void prepareFailView(String error) {

        /***** Retrieve state *****/
        LoggedInState state = loggedInViewModel.getState();

        /*****  Update state instead of using ViewModel setters *****/
        state.setTripStatus("ERROR");
        state.setErrorMessage(error);

        /***** Push updated state back into ViewModel *****/
        loggedInViewModel.setState(state);
    }
}