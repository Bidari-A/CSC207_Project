package use_case.trip_list;

import entity.Trip;
import java.util.List;

/**
 * The Trip List Interactor.
 */

public class TripListInteractor implements TripListInputBoundary {
    private final TripListUserDataAccessInterface userDataAccessObject;
    private final TripListOutputBoundary tripListPresenter;

    public TripListInteractor(TripListUserDataAccessInterface userDataAccessInterface,
                              TripListOutputBoundary tripListOutputBoundary) {
        this.userDataAccessObject = userDataAccessInterface;
        this.tripListPresenter = tripListOutputBoundary;
    }

    @Override
    public void execute(TripListInputData tripListInputData) {
        final String username = tripListInputData.getUsername();
        final List<Trip> trips = userDataAccessObject.getTrips(username);

        final TripListOutputData tripListOutputData = new TripListOutputData(trips, username);
        tripListPresenter.prepareSuccessView(tripListOutputData);
    }
}
