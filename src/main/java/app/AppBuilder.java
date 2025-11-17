package app;

import data_access.FileUserDataAccessObject;
import entity.UserFactory;
import interface_adapter.ViewManagerModel;
import interface_adapter.logged_in.ChangePasswordController;
import interface_adapter.logged_in.ChangePasswordPresenter;
import interface_adapter.logged_in.LoggedInViewModel;
import interface_adapter.login.LoginController;
import interface_adapter.login.LoginPresenter;
import interface_adapter.login.LoginViewModel;
import interface_adapter.create_new_trip.CreateNewTripViewModel;
import interface_adapter.logout.LogoutController;
import interface_adapter.logout.LogoutPresenter;
import interface_adapter.signup.SignupController;
import interface_adapter.signup.SignupPresenter;
import interface_adapter.signup.SignupViewModel;
import use_case.change_password.ChangePasswordInputBoundary;
import use_case.change_password.ChangePasswordInteractor;
import use_case.change_password.ChangePasswordOutputBoundary;
import use_case.login.LoginInputBoundary;
import use_case.login.LoginInteractor;
import use_case.login.LoginOutputBoundary;
import use_case.logout.LogoutInputBoundary;
import use_case.logout.LogoutInteractor;
import use_case.logout.LogoutOutputBoundary;
import use_case.signup.SignupInputBoundary;
import use_case.signup.SignupInteractor;
import use_case.signup.SignupOutputBoundary;
import view.*;
import view.CreateNewTripView;
import javax.swing.*;
import java.awt.*;

// For flights
import interface_adapter.flight_search.FlightSearchViewModel;
import interface_adapter.flight_search.FlightSearchPresenter;
import interface_adapter.flight_search.FlightSearchController;
import use_case.flight_search.FlightSearchInputBoundary;
import use_case.flight_search.FlightSearchInteractor;
import use_case.flight_search.FlightSearchOutputBoundary;
import use_case.flight_search.FlightSearchGateway;
import data_access.SerpApiFlightSearchGateway;


public class AppBuilder {
    private final JPanel cardPanel = new JPanel();
    private final CardLayout cardLayout = new CardLayout();
    final UserFactory userFactory = new UserFactory();
    final ViewManagerModel viewManagerModel = new ViewManagerModel();
    ViewManager viewManager = new ViewManager(cardPanel, cardLayout, viewManagerModel);

    final FileUserDataAccessObject userDataAccessObject =
            new FileUserDataAccessObject("users.csv", userFactory);

    private SignupView signupView;
    private SignupViewModel signupViewModel;
    private LoginViewModel loginViewModel;
    private LoggedInViewModel loggedInViewModel;
    private LoggedInView loggedInView;
    private LoginView loginView;
    private CreateNewTripViewModel createNewTripViewModel;
    private CreateNewTripView createNewTripView;
    private TripView tripView;
    private TripListView tripListView;
    private FlightSearchViewModel flightSearchViewModel;


    public AppBuilder() {
        cardPanel.setLayout(cardLayout);
    }

    public AppBuilder addSignupView() {
        signupViewModel = new SignupViewModel();
        signupView = new SignupView(signupViewModel);
        cardPanel.add(signupView, signupView.getViewName());
        return this;
    }

    public AppBuilder addLoginView() {
        loginViewModel = new LoginViewModel();
        loginView = new LoginView(loginViewModel);
        cardPanel.add(loginView, loginView.getViewName());
        return this;
    }

    public AppBuilder addLoggedInView() {
        loggedInViewModel = new LoggedInViewModel();
        loggedInView = new LoggedInView(loggedInViewModel);
        cardPanel.add(loggedInView, loggedInView.getViewName());
        return this;
    }

    public AppBuilder addSignupUseCase() {
        final SignupOutputBoundary signupOutputBoundary =
                new SignupPresenter(viewManagerModel, signupViewModel, loginViewModel);

        final SignupInputBoundary userSignupInteractor =
                new SignupInteractor(userDataAccessObject, signupOutputBoundary, userFactory);

        SignupController controller = new SignupController(userSignupInteractor);
        signupView.setSignupController(controller);
        return this;
    }

    public AppBuilder addLoginUseCase() {
        final LoginOutputBoundary loginOutputBoundary =
                new LoginPresenter(viewManagerModel, loggedInViewModel, loginViewModel);

        final LoginInputBoundary loginInteractor =
                new LoginInteractor(userDataAccessObject, loginOutputBoundary);

        LoginController loginController =
                new LoginController(loginInteractor, viewManagerModel);

        loginView.setLoginController(loginController);
        return this;
    }

    public AppBuilder addLogoutUseCase() {
        final LogoutOutputBoundary logoutOutputBoundary =
                new LogoutPresenter(viewManagerModel, loggedInViewModel, loginViewModel);

        final LogoutInputBoundary logoutInteractor =
                new LogoutInteractor(userDataAccessObject, logoutOutputBoundary);

        final LogoutController logoutController = new LogoutController(logoutInteractor);
        loggedInView.setLogoutController(logoutController);
        return this;
    }

    public AppBuilder addCreateNewTripView() {
        createNewTripViewModel = new CreateNewTripViewModel();
        createNewTripView = new CreateNewTripView(createNewTripViewModel);
        cardPanel.add(createNewTripView, createNewTripView.getViewName());
        return this;
    }

    public AppBuilder addFlightSearchUseCase() {
        // 1. ViewModel
        flightSearchViewModel = new FlightSearchViewModel();
        // 2. Presenter
        FlightSearchOutputBoundary flightSearchOutputBoundary =
                new FlightSearchPresenter(flightSearchViewModel);
        // 3. Gateway (calls SerpAPI)
        FlightSearchGateway flightSearchGateway = new SerpApiFlightSearchGateway();
        // 4. Interactor
        FlightSearchInputBoundary flightSearchInteractor =
                new FlightSearchInteractor(flightSearchGateway, flightSearchOutputBoundary);
        // 5. Controller
        FlightSearchController flightSearchController =
                new FlightSearchController(flightSearchInteractor);

        // 6. Inject into LoggedInView (like you do for logout)
        loggedInView.setFlightSearchViewModel(flightSearchViewModel);
        loggedInView.setFlightSearchController(flightSearchController);

        return this;
    }

    public JFrame build() {
        final JFrame application = new JFrame("User Login Example");
        application.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        application.add(cardPanel);

        viewManagerModel.setState(signupView.getViewName());
        viewManagerModel.firePropertyChange();

        return application;
    }
}