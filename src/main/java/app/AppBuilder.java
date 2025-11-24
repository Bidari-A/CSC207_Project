package app;

import data_access.FileUserDataAccessObject;
import data_access.FileUserTripDataAccessObject;
import entity.UserFactory;
import interface_adapter.ViewManagerModel;
import interface_adapter.logged_in.LoggedInViewModel;
import interface_adapter.trip.TripController;
import interface_adapter.trip.TripPresenter;
import interface_adapter.trip.TripViewModel;
import interface_adapter.trip_list.TripListController;
import interface_adapter.trip_list.TripListPresenter;
import interface_adapter.trip_list.TripListViewModel;
import interface_adapter.login.LoginController;
import interface_adapter.login.LoginPresenter;
import interface_adapter.login.LoginViewModel;
import interface_adapter.create_new_trip.CreateNewTripViewModel;
import interface_adapter.create_new_trip.CreateNewTripController;
import interface_adapter.create_new_trip.CreateNewTripPresenter;

import use_case.create_new_trip.CreateNewTripInteractor;

import interface_adapter.logout.LogoutController;
import interface_adapter.logout.LogoutPresenter;

import interface_adapter.signup.SignupController;
import interface_adapter.signup.SignupPresenter;
import interface_adapter.signup.SignupViewModel;

import use_case.load_trip_detail.LoadTripDetailInputBoundary;
import use_case.load_trip_detail.LoadTripDetailInteractor;
import use_case.load_trip_detail.LoadTripDetailOutputBoundary;

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

import use_case.create_new_trip.CreateNewTripInputBoundary;
import use_case.create_new_trip.CreateNewTripOutputBoundary;

import view.*;

import javax.swing.*;
import java.awt.*;

import interface_adapter.flight_search.FlightSearchViewModel;
import interface_adapter.flight_search.FlightSearchPresenter;
import interface_adapter.flight_search.FlightSearchController;
import use_case.flight_search.FlightSearchInputBoundary;
import use_case.flight_search.FlightSearchInteractor;
import use_case.flight_search.FlightSearchOutputBoundary;
import use_case.flight_search.FlightSearchGateway;
import data_access.SerpApiFlightSearchGateway;

import interface_adapter.hotel_search.HotelSearchViewModel;
import interface_adapter.hotel_search.HotelSearchPresenter;
import interface_adapter.hotel_search.HotelSearchController;
import use_case.hotel_search.HotelSearchInputBoundary;
import use_case.hotel_search.HotelSearchInteractor;
import use_case.hotel_search.HotelSearchOutputBoundary;
import use_case.hotel_search.HotelSearchGateway;
import data_access.SerpApiHotelSearchGateway;

import use_case.delete_trip_list.DeleteTripInputBoundary;
import use_case.delete_trip_list.DeleteTripOutputBoundary;
import use_case.delete_trip_list.DeleteTripInteractor;


public class AppBuilder {
    private final JPanel cardPanel = new JPanel();
    private final CardLayout cardLayout = new CardLayout();
    final UserFactory userFactory = new UserFactory();
    final ViewManagerModel viewManagerModel = new ViewManagerModel();
    ViewManager viewManager = new ViewManager(cardPanel, cardLayout, viewManagerModel);

    final FileUserDataAccessObject userDataAccessObject =
            new FileUserDataAccessObject("users.csv", userFactory);

    final FileUserTripDataAccessObject userTripDataAccessObject =
            new FileUserTripDataAccessObject();

    private SignupView signupView;
    private SignupViewModel signupViewModel;
    private LoginViewModel loginViewModel;
    private LoggedInViewModel loggedInViewModel;
    private LoggedInView loggedInView;
    private LoginView loginView;
    private CreateNewTripViewModel createNewTripViewModel;
    private CreateNewTripView createNewTripView;

    private TripView tripView;
    private TripViewModel tripViewModel;

    private TripListView tripListView;
    private TripListViewModel tripListViewModel;
    private FlightSearchViewModel flightSearchViewModel;
    private HotelSearchViewModel hotelSearchViewModel;


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
        tripListView = new TripListView(tripListViewModel);
        cardPanel.add(tripListView, tripListView.getViewName());
        return this;
    }

    public AppBuilder addCreateNewTripView() {
        createNewTripViewModel = new CreateNewTripViewModel();
        createNewTripView = new CreateNewTripView(createNewTripViewModel);
        cardPanel.add(createNewTripView, createNewTripView.getViewName());
        return this;
    }

    public AppBuilder addSignupUseCase() {
        SignupOutputBoundary signupOutputBoundary =
                new SignupPresenter(viewManagerModel, signupViewModel, loginViewModel);

        SignupInputBoundary userSignupInteractor =
                new SignupInteractor(userDataAccessObject, signupOutputBoundary, userFactory);

        SignupController controller = new SignupController(userSignupInteractor);
        signupView.setSignupController(controller);
        return this;
    }

    public AppBuilder addLoginUseCase() {
        LoginOutputBoundary loginOutputBoundary =
                new LoginPresenter(viewManagerModel, loggedInViewModel, loginViewModel);

        LoginInputBoundary loginInteractor =
                new LoginInteractor(userDataAccessObject, loginOutputBoundary);

        LoginController loginController =
                new LoginController(loginInteractor);

        loginView.setLoginController(loginController);
        return this;
    }

    public AppBuilder addLoadTripListUseCase() {
        LoadTripListOutputBoundary loadOutput =
                new TripListPresenter(viewManagerModel, tripListViewModel);

        LoadTripListInputBoundary loadInteractor =
                new LoadTripListInteractor(null, loadOutput);

        DeleteTripOutputBoundary deleteOutput =
                new TripListPresenter(viewManagerModel, tripListViewModel);

        DeleteTripInputBoundary deleteInteractor =
                new DeleteTripInteractor(null, deleteOutput);

        TripListController controller =
                new TripListController(loadInteractor, deleteInteractor);

        tripListView.setTripListController(controller);
        loggedInView.setTripListController(controller);

        return this;
    }

    public AppBuilder addTripView(){
        tripViewModel = new TripViewModel();
        tripView = new TripView(tripViewModel);
        cardPanel.add(tripView, tripViewModel.getViewName());
        return this;
    }

    public AppBuilder addLoadTripDetailUseCase() {
        LoadTripDetailOutputBoundary loadTripDetailOutputBoundary =
                new TripPresenter(tripViewModel, viewManagerModel);

        LoadTripDetailInputBoundary loadTripDetailInputBoundary =
                new LoadTripDetailInteractor(userDataAccessObject, loadTripDetailOutputBoundary);

        TripController tripController = new TripController(loadTripDetailInputBoundary);

        tripView.setTripController(tripController);
        loggedInView.setTripController(tripController);
        tripListView.setTripController(tripController);
        return this;
    }

    public AppBuilder addLogoutUseCase() {
        LogoutOutputBoundary logoutOutputBoundary =
                new LogoutPresenter(viewManagerModel, loggedInViewModel, loginViewModel);

        LogoutInputBoundary logoutInteractor =
                new LogoutInteractor(userDataAccessObject, logoutOutputBoundary);

        LogoutController logoutController = new LogoutController(logoutInteractor);
        loggedInView.setLogoutController(logoutController);
        return this;
    }

    public AppBuilder addCreateNewTripUseCase() {
        CreateNewTripOutputBoundary presenter =
                new CreateNewTripPresenter(viewManagerModel);

        CreateNewTripInputBoundary interactor =
                new CreateNewTripInteractor(presenter);

        CreateNewTripController controller =
                new CreateNewTripController(interactor, presenter);

        createNewTripView.setCreateNewTripController(controller);
        loggedInView.setCreateNewTripController(controller);

        return this;
    }

    public AppBuilder addFlightSearchUseCase() {
        flightSearchViewModel = new FlightSearchViewModel();

        FlightSearchOutputBoundary outputBoundary =
                new FlightSearchPresenter(flightSearchViewModel);

        FlightSearchGateway gateway = new SerpApiFlightSearchGateway();

        FlightSearchInputBoundary interactor =
                new FlightSearchInteractor(gateway, outputBoundary);

        FlightSearchController controller =
                new FlightSearchController(interactor);

        loggedInView.setFlightSearchViewModel(flightSearchViewModel);
        loggedInView.setFlightSearchController(controller);

        return this;
    }

    public AppBuilder addHotelSearchUseCase() {
        hotelSearchViewModel = new HotelSearchViewModel();

        HotelSearchOutputBoundary outputBoundary =
                new HotelSearchPresenter(hotelSearchViewModel);

        HotelSearchGateway gateway = new SerpApiHotelSearchGateway();

        HotelSearchInputBoundary interactor =
                new HotelSearchInteractor(gateway, outputBoundary);

        HotelSearchController controller =
                new HotelSearchController(interactor);

        loggedInView.setHotelSearchViewModel(hotelSearchViewModel);
        loggedInView.setHotelSearchController(controller);

        return this;
    }

    public JFrame build() {
        JFrame application = new JFrame("User Login Example");
        application.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        application.add(cardPanel);

        viewManagerModel.setState(signupView.getViewName());
        viewManagerModel.firePropertyChange();

        return application;
    }
}