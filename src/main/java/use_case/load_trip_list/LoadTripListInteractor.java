package use_case.load_trip_list;

import entity.Trip;
import java.util.List;

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

        if (username == null || username.isEmpty()) {
            loadTripListPresenter.prepareFailView("Username cannot be empty.");
            return;
        }

        try {
            List<Trip> trips = userDataAccessObject.getTrips(username);
            final LoadTripListOutputData loadTripListOutputData = new LoadTripListOutputData(trips, username);
            loadTripListPresenter.prepareSuccessView(loadTripListOutputData);
        } catch (Exception e) {
            loadTripListPresenter.prepareFailView("Error loading trips: " + e.getMessage());
        }
    }
    public void goBack() {
        loadTripListPresenter.prepareBackView();
    }

}

