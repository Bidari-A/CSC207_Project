package app;

import java.awt.CardLayout;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import data_access.FileDeleteTripDataAccessObject;
import data_access.FileTripDataAccessObject;
import data_access.FileUserDataAccessObject;
import entity.Trip;
import entity.TripIdGenerator;
import entity.UserFactory;
import interface_adapter.ViewManagerModel;
import interface_adapter.create_new_trip.CreateNewTripController;
import interface_adapter.create_new_trip.CreateNewTripPresenter;
import interface_adapter.create_new_trip.CreateNewTripViewModel;
import interface_adapter.create_trip_result.TripResultViewModel;
import interface_adapter.flight_search.FlightSearchViewModel;
import interface_adapter.hotel_search.HotelSearchViewModel;
import interface_adapter.logged_in.LoggedInViewModel;
import interface_adapter.login.LoginController;
import interface_adapter.login.LoginPresenter;
import interface_adapter.login.LoginViewModel;
import interface_adapter.logout.LogoutController;
import interface_adapter.logout.LogoutPresenter;
import interface_adapter.signup.SignupController;
import interface_adapter.signup.SignupPresenter;
import interface_adapter.signup.SignupViewModel;   // NEW
import interface_adapter.trip.TripController;
import interface_adapter.trip.TripPresenter;
import interface_adapter.trip.TripViewModel;
import interface_adapter.trip_list.TripListController;
import interface_adapter.trip_list.TripListPresenter;
import interface_adapter.trip_list.TripListViewModel;
import use_case.create_new_trip.CreateNewTripInputBoundary;
import use_case.create_new_trip.CreateNewTripInteractor;
import use_case.create_new_trip.CreateNewTripOutputBoundary;
import use_case.delete_trip_list.DeleteTripInputBoundary;
import use_case.delete_trip_list.DeleteTripInteractor;
import use_case.delete_trip_list.DeleteTripOutputBoundary;
import use_case.delete_trip_list.DeleteTripUserDataAccessInterface;
import use_case.load_trip_detail.LoadTripDetailInputBoundary;
import use_case.load_trip_detail.LoadTripDetailInteractor;
import use_case.load_trip_detail.LoadTripDetailOutputBoundary;
import use_case.load_trip_list.LoadTripListInputBoundary;
import use_case.load_trip_list.LoadTripListInteractor;
import use_case.load_trip_list.LoadTripListOutputBoundary;
import use_case.login.LoginInputBoundary;
import use_case.login.LoginInteractor;
import use_case.login.LoginOutputBoundary;
import use_case.logout.LogoutInputBoundary;
import use_case.logout.LogoutInteractor;
import use_case.logout.LogoutOutputBoundary;                                  // NEW
import use_case.signup.SignupInputBoundary;
import use_case.signup.SignupInteractor;
import use_case.signup.SignupOutputBoundary;
import view.CreateNewTripView;
import view.LoggedInView;
import view.LoginView;
import view.SignupView;
import view.TripListView;
import view.TripResultView;
import view.TripView;
import view.ViewManager;



public class AppBuilder {
    private final JPanel cardPanel = new JPanel();
    private final CardLayout cardLayout = new CardLayout();
    final UserFactory userFactory = new UserFactory();
    final ViewManagerModel viewManagerModel = new ViewManagerModel();
    ViewManager viewManager = new ViewManager(cardPanel, cardLayout, viewManagerModel);

    final FileUserDataAccessObject userDataAccessObject =
            new FileUserDataAccessObject("users.json", userFactory);

    final FileTripDataAccessObject tripDataAccessObject =
            new FileTripDataAccessObject("trips.json");

    {
        // Integrate trip DAO with user DAO
        userDataAccessObject.setTripDataAccessObject(tripDataAccessObject);
    }

    {
        // Initialize ID generator from existing trips
        Collection<String> existingTripIds = new ArrayList<>();
        for (Trip trip:tripDataAccessObject.getAllTrips()){
        existingTripIds.add(trip.getTripId());
    }
        TripIdGenerator.initializeFromExistingIds(existingTripIds);
    }

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


    // NEW: Trip result VM and view
    private TripResultViewModel tripResultViewModel;
    private TripResultView tripResultView;

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

    // NEW: add TripResultView to the card panel
    public AppBuilder addTripResultView() {
        tripResultViewModel = new TripResultViewModel();
        tripResultView = new TripResultView(tripResultViewModel);
        cardPanel.add(tripResultView, tripResultView.getViewName());
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
                new LoginController(loginInteractor);

        loginView.setLoginController(loginController);
        return this;
    }

    public AppBuilder addLoadTripListUseCase() {
        final LoadTripListOutputBoundary loadTripListOutputBoundary =
                new TripListPresenter(viewManagerModel, tripListViewModel);

        final LoadTripListInputBoundary loadTripListInteractor =
                new LoadTripListInteractor(userDataAccessObject, loadTripListOutputBoundary);

        TripListController tripListController = new TripListController(loadTripListInteractor);

        tripListView.setTripListController(tripListController);
        loggedInView.setTripListController(tripListController);
        return this;
    }

    public AppBuilder addTripView(){
        tripViewModel = new TripViewModel();
        tripView = new TripView(tripViewModel);
        cardPanel.add(tripView, tripViewModel.getViewName());
        return this;
    }

    public AppBuilder addLoadTripDetailUseCase() {
        final LoadTripDetailOutputBoundary loadTripDetailOutputBoundary =
                new TripPresenter(tripViewModel, viewManagerModel);

        final LoadTripDetailInputBoundary loadTripDetailInputBoundary =
                new LoadTripDetailInteractor(userDataAccessObject, loadTripDetailOutputBoundary);

        TripController tripController = new TripController(loadTripDetailInputBoundary);

        tripView.setTripController(tripController);
        loggedInView.setTripController(tripController);
        tripListView.setTripController(tripController);
        return this;
    }

    public AppBuilder addDeleteTripUseCase() {

        DeleteTripUserDataAccessInterface deleteTripDAO =
                new FileDeleteTripDataAccessObject(tripDataAccessObject);

        DeleteTripOutputBoundary deleteTripPresenter =
                new TripListPresenter(viewManagerModel, tripListViewModel);

        DeleteTripInputBoundary deleteTripInteractor =
                new DeleteTripInteractor(deleteTripDAO, deleteTripPresenter);

        tripListView.getTripListController().setDeleteTripUseCaseInteractor(deleteTripInteractor);

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

    public AppBuilder addCreateNewTripUseCase() {

        // Ensure TripResultViewModel / View exist so presenter can use them
        if (tripResultViewModel == null) {
            tripResultViewModel = new TripResultViewModel();
            tripResultView = new TripResultView(tripResultViewModel);
            cardPanel.add(tripResultView, tripResultView.getViewName());
        }

        final CreateNewTripOutputBoundary createNewTripPresenter =
                new CreateNewTripPresenter(viewManagerModel, createNewTripViewModel, tripResultViewModel);

        final CreateNewTripInputBoundary createNewTripInteractor =
                new CreateNewTripInteractor(createNewTripPresenter);

        final CreateNewTripController controller =
                new CreateNewTripController(createNewTripInteractor);

        createNewTripView.setCreateNewTripController(controller);
        // Give the controller to LoggedInView
        loggedInView.setCreateNewTripController(controller);

        return this;
    }

    public JFrame build() {
        final JFrame application = new JFrame("Travel App");
        application.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        application.add(cardPanel);

        viewManagerModel.setState(signupView.getViewName());
        viewManagerModel.firePropertyChange();

        return application;
    }
}
