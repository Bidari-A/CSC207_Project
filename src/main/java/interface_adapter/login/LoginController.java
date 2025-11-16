package interface_adapter.login;

import interface_adapter.ViewManagerModel;
import use_case.login.LoginInputBoundary;
import use_case.login.LoginInputData;

/**
 * The controller for the Login Use Case.
 */
public class LoginController {

    private final LoginInputBoundary loginUseCaseInteractor;
    private final ViewManagerModel viewManagerModel;

    public LoginController(LoginInputBoundary loginUseCaseInteractor,
                           ViewManagerModel viewManagerModel) {
        this.loginUseCaseInteractor = loginUseCaseInteractor;
        this.viewManagerModel = viewManagerModel;
    }

    public void execute(String username, String password) {
        final LoginInputData loginInputData = new LoginInputData(username, password);
        loginUseCaseInteractor.execute(loginInputData);
    }

    public void goToSignUp() {
        viewManagerModel.setState("sign up");   // ✔ 正确
        viewManagerModel.firePropertyChange();  // ✔ 正确
    }
}