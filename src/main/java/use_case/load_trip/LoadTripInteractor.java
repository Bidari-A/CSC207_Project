package use_case.load_trip;

import entity.Trip;
import java.util.List;

/**
 * Interactor for loading trips.
 * Implements the Input Boundary.
 */
public class LoadTripInteractor implements LoadTripInputBoundary {

    private final TripFileDataAccessInterface tripDataAccess;
    private final LoadTripOutputBoundary outputBoundary;

    /**
     * Constructor.
     * @param tripDataAccess the DAO interface for loading trips
     * @param outputBoundary the presenter (output boundary) for the use case
     */
    public LoadTripInteractor(TripFileDataAccessInterface tripDataAccess,
                              LoadTripOutputBoundary outputBoundary) {
        this.tripDataAccess = tripDataAccess;
        this.outputBoundary = outputBoundary;
    }

    /**
     * Executes the load trip use case.
     * @param loadTripInputData input data containing the username
     */
    @Override
    public void execute(LoadTripInputData loadTripInputData) {
        String username = loadTripInputData.getUsername();
        try {
            // 1. Fetch trips from DAO
            List<Trip> trips = tripDataAccess.getTrips(username);

            // 2. Send to presenter/output boundary
            LoadTripOutputData outputData = new LoadTripOutputData(trips, username);
            outputBoundary.prepareSuccessView(outputData);
        } catch (Exception e) {
            // 3. If any error occurs, show failure view
            outputBoundary.prepareFailView("Failed to load trips: " + e.getMessage());
        }
    }

    /**
     * Go back action (delegated to presenter/output boundary)
     */
    @Override
    public void goBack() {
        outputBoundary.prepareBackView();
    }
}
