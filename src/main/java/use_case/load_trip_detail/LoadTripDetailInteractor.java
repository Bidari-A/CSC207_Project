package use_case.load_trip_detail;

import interface_adapter.logged_in.LoggedInState;
import interface_adapter.logged_in.LoggedInViewModel;

public class LoadTripDetailInteractor implements LoadTripDetailInputBoundary {
    private final LoadTripDetailDataAccessInterface loadTripDetailDataAccessInterface;
    private final LoadTripDetailOutputBoundary loadTripDetailOutputBoundary;
    private final LoggedInViewModel loggedInViewModel;

    public LoadTripDetailInteractor(LoadTripDetailDataAccessInterface loadTripDetailDataAccessInterface,
                                    LoadTripDetailOutputBoundary loadTripDetailOutputBoundary,
                                    LoggedInViewModel loggedInViewModel) {
        this.loadTripDetailDataAccessInterface = loadTripDetailDataAccessInterface;
        this.loadTripDetailOutputBoundary = loadTripDetailOutputBoundary;
        this.loggedInViewModel = loggedInViewModel;
    }

    @Override
    public void execute(LoadTripDetailInputData loadTripDetailInputData) {
        final String prevViewName = loadTripDetailInputData.getPrevViewName();
        final String requestedTripName = loadTripDetailInputData.getTripName();

        String tripName = "";
        String city = "";
        String date = "";
        String attractions = "";
        String flightDetails = "";
        String hotelDetails = "";

        // If a specific trip name is requested, load it from the trip list
        if (requestedTripName != null && !requestedTripName.isEmpty()) {
            entity.Trip trip = loadTripDetailDataAccessInterface.getTripByName(
                    loadTripDetailInputData.getUsername(), requestedTripName);
            if (trip != null) {
                tripName = trip.getName();
                city = trip.getCityName();
                // Format dates
                if (trip.getTripDates() != null && !trip.getTripDates().isEmpty()) {
                    java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("MM/dd/yyyy");
                    date = sdf.format(trip.getTripDates().get(0));
                } else {
                    date = "Date not set";
                }
                // Format destinations as attractions
                if (trip.getDestinations() != null && !trip.getDestinations().isEmpty()) {
                    StringBuilder sb = new StringBuilder();
                    for (entity.Destination dest : trip.getDestinations()) {
                        sb.append(dest.toString()).append("\n");
                    }
                    attractions = sb.toString().trim();
                } else {
                    attractions = "No attractions added yet";
                }
                // Format flights
                if (trip.getFlights() != null && !trip.getFlights().isEmpty()) {
                    StringBuilder sb = new StringBuilder();
                    for (entity.Flight flight : trip.getFlights()) {
                        sb.append(flight.toString()).append("\n");
                    }
                    flightDetails = sb.toString().trim();
                } else {
                    flightDetails = "No flights added yet";
                }
                // Format accommodations
                if (trip.getAccommodations() != null && !trip.getAccommodations().isEmpty()) {
                    StringBuilder sb = new StringBuilder();
                    for (entity.Accommodation acc : trip.getAccommodations()) {
                        sb.append(acc.toString()).append("\n");
                    }
                    hotelDetails = sb.toString().trim();
                } else {
                    hotelDetails = "No accommodations added yet";
                }
            } else {
                // Trip not found, use placeholder
                tripName = requestedTripName;
                city = "Unknown";
                date = "Date not available";
                attractions = "Trip details not available";
                flightDetails = "Flight details not available";
                hotelDetails = "Hotel details not available";
            }
        } else {
            // Get current trip info from LoggedInState (draft trip)
            LoggedInState state = loggedInViewModel.getState();
            if (state.hasDraft()) {
                tripName = state.getDraftTripName();
                city = state.getDraftTripCity();
                date = state.getDraftTripDate();
                // Show empty fields for draft trips - they haven't been completed yet
                attractions = "No attractions added yet";
                flightDetails = "No flights added yet";
                hotelDetails = "No accommodations added yet";
            } else {
                // No draft trip exists - show empty/placeholder
                tripName = "";
                city = "";
                date = "";
                attractions = "No trip selected";
                flightDetails = "No trip selected";
                hotelDetails = "No trip selected";
            }
        }

        final LoadTripDetailOutputData loadTripDetailOutputData = new LoadTripDetailOutputData(
                tripName, city, date, attractions, flightDetails, hotelDetails, prevViewName);

        loadTripDetailOutputBoundary.prepareTripView(loadTripDetailOutputData);

    }

    @Override
    public void back() {
        loadTripDetailOutputBoundary.back();
    }

}
