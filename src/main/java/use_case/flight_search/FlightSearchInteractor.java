package use_case.flight_search;
import data_access.FileUserDataAccessObject;
import data_access.FileTripDataAccessObject;
import entity.Flight;
import entity.Trip;
import entity.User;

// Now, this interactor TAKES IN the FlightSearchInputData. So this is an input boundary instance.
public class FlightSearchInteractor implements FlightSearchInputBoundary{
    // this Interactor needs to know
    //1. The gateway to perform HTTPS request
    //2. The presenter that this class can tell it to update the state
    // Recall the presenter takes in the outputData, so it is a OutputBoundary
    private final FlightSearchGateway gateway;
    private final FlightSearchOutputBoundary presenter;


    private final FileUserDataAccessObject userDao;
    private final FileTripDataAccessObject tripDao;

    public FlightSearchInteractor(FlightSearchGateway gateway,
                                  FlightSearchOutputBoundary presenter,
                                  FileUserDataAccessObject userDao,
                                  FileTripDataAccessObject tripDao) {
        this.gateway = gateway;
        this.presenter = presenter;
        this.userDao = userDao;
        this.tripDao = tripDao;
    }

    // Remember the Controller will call this interactor through .execute!
    public void execute(FlightSearchInputData inputData){
        // STEP 1. GET the json object and STORE in 'json'
        try {
            String json = gateway.fetchRawJson(
                    inputData.getFrom(),
                    inputData.getTo(),
                    inputData.getOutboundDate(),
                    inputData.getReturnDate()
            );
            // 2. Make summary for the UI
            String summary = gateway.summarizeFirstBestFlight(json);
            // 3. Build Flight entity from JSON
            Flight bestFlight = gateway.buildFirstFlightEntity(json);
            // 4. Save to current trip in DB
            saveBestFlightToCurrentTrip(bestFlight);
            // 5. Notify presenter
            FlightSearchOutputData outputData = new FlightSearchOutputData(summary);
            presenter.prepareSuccessView(outputData);
        } catch (Exception e) {
            presenter.prepareFailView(e.getMessage());
        }
    }
    private void saveBestFlightToCurrentTrip(Flight flight) {
        if (flight == null) {
            return;
        }

        // 1. Get current username
        String username = userDao.getCurrentUsername();
        if (username == null) {
            return;
        }

        // 2. Get user and their current trip ID
        User user = userDao.getUser(username);
        if (user == null || user.getCurrentTripId() == null) {
            return;
        }

        String tripId = user.getCurrentTripId();

        // 3. Load the trip from Trip DAO
        Trip trip = tripDao.get(tripId);
        if (trip == null) {
            return;
        }

        // 4. Mutate flights list
        // If you want to append:
        // trip.getFlights().add(flight);

        // If instead you want to overwrite the previous selection, do:
        trip.getFlights().clear();
        trip.getFlights().add(flight);

        // 5. Save the updated trip
        tripDao.save();
    }




}