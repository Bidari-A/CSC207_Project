package use_case.login;

import entity.Trip;
import entity.User;

/**
 * The Login Interactor.
 */
public class LoginInteractor implements LoginInputBoundary {
    private final LoginUserDataAccessInterface userDataAccessObject;
    private final LoginOutputBoundary loginPresenter;

    public LoginInteractor(LoginUserDataAccessInterface userDataAccessInterface,
                           LoginOutputBoundary loginOutputBoundary) {
        this.userDataAccessObject = userDataAccessInterface;
        this.loginPresenter = loginOutputBoundary;
    }

    @Override
    public void execute(LoginInputData loginInputData) {
        final String username = loginInputData.getUsername();
        final String password = loginInputData.getPassword();
        if (!userDataAccessObject.existsByName(username)) {
            loginPresenter.prepareFailView(username + ": Account does not exist.");
        }
        else {
            final String pwd = userDataAccessObject.get(username).getPassword();
            if (!password.equals(pwd)) {
                loginPresenter.prepareFailView("Incorrect password for \"" + username + "\".");
            }
            else {

                final User user = userDataAccessObject.get(loginInputData.getUsername());

                userDataAccessObject.setCurrentUsername(username);
                String currentTripId = user.getCurrentTripId();
                
                // Handle case when user has no current trip
                String currentTripName;
                String cityName;
                String date;
                
                if (currentTripId == null || currentTripId.isEmpty()) {
                    // No current trip - set default values
                    currentTripName = "N/A";
                    cityName = "N/A";
                    date = "N/A";
                } else {
                    Trip currentTrip = userDataAccessObject.getTrip(currentTripId);
                    if (currentTrip == null) {
                        // Trip ID exists but trip not found - set default values
                        currentTripName = "N/A";
                        cityName = "N/A";
                        date = "N/A";
                    } else {
                        currentTripName = currentTrip.getTripName();
                        cityName = currentTrip.getDestination();
                        date = currentTrip.getDates();
                    }
                }

                final LoginOutputData loginOutputData = new LoginOutputData(user.getUsername(), currentTripName, cityName, date);
                loginPresenter.prepareSuccessView(loginOutputData);
            }
        }
    }

    @Override
    public void switchToSignUpView() {
        loginPresenter.switchToSignUpView();
    }
}