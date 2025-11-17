package app;

import data_access.FileUserDataAccessObject;
import entity.UserFactory;
import interface_adapter.ViewManagerModel;
import interface_adapter.logged_in.LoggedInViewModel;
import interface_adapter.trip_list.TripListController;
import interface_adapter.trip_list.TripListPresenter;
import interface_adapter.trip_list.TripListViewModel;
import interface_adapter.login.LoginController;
import interface_adapter.login.LoginPresenter;
import interface_adapter.login.LoginViewModel;
import interface_adapter.create_new_trip.CreateNewTripViewModel;
import interface_adapter.logout.LogoutController;
import interface_adapter.logout.LogoutPresenter;
import interface_adapter.signup.SignupController;
import interface_adapter.signup.SignupPresenter;
import interface_adapter.signup.SignupViewModel;
import use_case.login.LoginInputBoundary;
import use_case.login.LoginInteractor;
import use_case.login.LoginOutputBoundary;
import use_case.logout.LogoutInputBoundary;
import use_case.logout.LogoutInteractor;
import use_case.logout.LogoutOutputBoundary;
import use_case.load_trip_list.LoadTripListInputBoundary;
import use_case.load_trip_list.LoadTripListInteractor;
import use_case.load_trip_list.LoadTripListOutputBoundary;
import use_case.signup.SignupInputBoundary;
import use_case.signup.SignupInteractor;
import use_case.signup.SignupOutputBoundary;
import view.*;
import view.CreateNewTripView;
import javax.swing.*;
import java.awt.*;

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
    private TripListViewModel tripListViewModel;

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

    public AppBuilder addTripListView() {
        tripListViewModel = new TripListViewModel();
        tripListView = new TripListView(tripListViewModel, viewManagerModel);
        cardPanel.add(tripListView, tripListView.getViewName());
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

        // ⭐ FIXED: add viewManagerModel here
        LoginController loginController =
                new LoginController(loginInteractor, viewManagerModel);

        loginView.setLoginController(loginController);
        return this;
    }

    public AppBuilder addTripListController() {
        final LoadTripListOutputBoundary loadTripListOutputBoundary =
                new TripListPresenter(viewManagerModel, tripListViewModel);

        final LoadTripListInputBoundary loadTripListInteractor =
                new LoadTripListInteractor(userDataAccessObject, loadTripListOutputBoundary);

        final TripListController tripListController = new TripListController(loadTripListInteractor);
        loggedInView.setTripListController(tripListController);
        tripListView.setTripListController(tripListController);
        return this;
    }



    /**
     * Adds the Logout Use Case to the application.
     * @return this builder
     */
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

    public JFrame build() {
        final JFrame application = new JFrame("User Login Example");
        application.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        application.add(cardPanel);

        viewManagerModel.setState(signupView.getViewName());
        viewManagerModel.firePropertyChange();

        return application;
    }
}