package use_case.load_trip_list;

import java.util.List;

import entity.Trip;

/**
 * The Load Trip List Interactor.
 */
public class LoadTripListInteractor implements LoadTripListInputBoundary {
    private final LoadTripListUserDataAccessInterface userDataAccessObject;
    private final LoadTripListOutputBoundary loadTripListPresenter;

    public LoadTripListInteractor(LoadTripListUserDataAccessInterface userDataAccessInterface,
                                  LoadTripListOutputBoundary loadTripListOutputBoundary) {
        this.userDataAccessObject = userDataAccessInterface;
        this.loadTripListPresenter = loadTripListOutputBoundary;
    }

    @Override
    public void execute(LoadTripListInputData loadTripListInputData) {
        final String username = loadTripListInputData.getUsername();
        // Load trips from database
        List<Trip> trips = userDataAccessObject.getTrips(username);

        final LoadTripListOutputData loadTripListOutputData = new LoadTripListOutputData(trips, username);
        loadTripListPresenter.prepareSuccessView(loadTripListOutputData);
    }

    @Override
    public void goBack() {
        loadTripListPresenter.prepareBackView();
    }

}

