package use_case.load_trip_list;

import entity.Accommodation;
import entity.Destination;
import entity.Flight;
import entity.Trip;

import java.util.ArrayList;
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
        // TODO Replace with database stuff
//        List<Trip> trips = userDataAccessObject.getTrips(username);
        // TODO TEMP
        Destination d =  new Destination("Destination", "", "", 67);
        Flight f = new Flight("Airline", "222", 5);
        Accommodation a = new Accommodation("Accommodation", "Accommodation", 1);

        List<Destination> destinations = new ArrayList<>();
        destinations.add(d);

        List<Accommodation> accommodations = new ArrayList<>();
        accommodations.add(a);

        List<Flight> flights = new ArrayList<>();
        flights.add(f);

        Trip trip = new Trip("sigma", "sss", "sss", destinations, accommodations,
                flights);
        List<Trip> trips = new ArrayList<>();
        trips.add(trip);

        //TODO DELETE ABOVE

        final LoadTripListOutputData loadTripListOutputData = new LoadTripListOutputData(trips, username);
        loadTripListPresenter.prepareSuccessView(loadTripListOutputData);

    }
    public void goBack() {
        loadTripListPresenter.prepareBackView();
    }

}

