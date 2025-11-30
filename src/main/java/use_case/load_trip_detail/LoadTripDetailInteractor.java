package use_case.load_trip_detail;

import java.util.List;
import java.util.stream.Collectors;

import entity.*;

public class LoadTripDetailInteractor implements LoadTripDetailInputBoundary {
    private final LoadTripDetailDataAccessInterface loadTripDetailDataAccessInterface;
    private final LoadTripDetailOutputBoundary loadTripDetailOutputBoundary;

    public LoadTripDetailInteractor(LoadTripDetailDataAccessInterface loadTripDetailDataAccessInterface,
                                    LoadTripDetailOutputBoundary loadTripDetailOutputBoundary) {
        this.loadTripDetailDataAccessInterface = loadTripDetailDataAccessInterface;
        this.loadTripDetailOutputBoundary = loadTripDetailOutputBoundary;
    }

    @Override
    public void execute(LoadTripDetailInputData loadTripDetailInputData) {
        final String prevViewName = loadTripDetailInputData.getPrevViewName();
        String tripId = loadTripDetailInputData.getTripId();
        final String username = loadTripDetailInputData.getUsername();

        if (tripId == null) {
            final User user = loadTripDetailDataAccessInterface.get(username);
            tripId = user.getCurrentTripId();
        }
        // Load trip from database using trip ID
        final Trip trip = loadTripDetailDataAccessInterface.getTrip(tripId);

        if (trip != null) {
            // Format trip data for display

            // Format attractions (Destination objects)
            String attractions = formatAttractions(trip.getAttractions());
            if (attractions.isEmpty()) {
                attractions = "No attractions selected";
            }

            // Format flights
            String flightDetails = formatFlights(trip.getFlights());
            if (flightDetails.isEmpty()) {
                flightDetails = "No flights selected";
            }

            // Format hotels
            String hotelDetails = formatHotels(trip.getHotels());
            if (hotelDetails.isEmpty()) {
                hotelDetails = "No hotels selected";
            }

            final String tripName = trip.getTripName();
            // Use the destination field from the trip entity
            String cityName = trip.getDestination();
            if (cityName == null || cityName.isEmpty()) {
                // Fallback to extracting from trip name if destination is not set
                cityName = extractCityName(tripName);
            }

            final String dates = trip.getDates();
            final LoadTripDetailOutputData loadTripDetailOutputData = new LoadTripDetailOutputData(
                    tripName, cityName, dates,
                    attractions, flightDetails, hotelDetails, prevViewName);

            loadTripDetailOutputBoundary.prepareTripView(loadTripDetailOutputData);
        }
        else {
            // Trip not found - show empty/default data
            final LoadTripDetailOutputData loadTripDetailOutputData = new LoadTripDetailOutputData(
                    "Trip not found", "", "",
                    "Trip not found", "Trip not found", "Trip not found", prevViewName);
            loadTripDetailOutputBoundary.prepareTripView(loadTripDetailOutputData);
        }
    }

    /**
     * Formats a list of flights into a display string.
     * @param flights a list of Flight objects
     * @return a string with formatted flights
     */
    private String formatFlights(List<Flight> flights) {
        return flights.stream()
                .map(flight -> {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Airline: ").append(flight.getAirlineName());

                    final String times = flight.getDepartureTimes();
                    if (times != null && !times.isEmpty()) {
                        sb.append("\nDeparture: ").append(times);
                    }

                    if (flight.getPrice() > 0) {
                        sb.append("\nPrice: $")
                                .append(String.format("%.2f", flight.getPrice()))
                                .append(" USD");
                    }

                    return sb.toString();
                })
                .collect(Collectors.joining("\n\n"));
    }

    /**
     * Formats a list of hotels into a display string.
     * @param hotels a list of Accommodation objects
     * @return a String with formatted hotels
     */
    private String formatHotels(List<Accommodation> hotels) {
        return hotels.stream()
                .map(hotel -> {
                    final StringBuilder sb = new StringBuilder();

                    // Name is always shown
                    sb.append("Name: ").append(hotel.getName());

                    // Only show address if present (yours is often "")
                    final String address = hotel.getAddress();
                    if (address != null && !address.isEmpty()) {
                        sb.append("\nAddress: ").append(address);
                    }

                    // Only show price if > 0
                    if (hotel.getPrice() > 0) {
                        sb.append("\nPrice: $")
                                .append(String.format("%.2f", hotel.getPrice()))
                                .append(" USD");
                    }

                    return sb.toString();
                })
                .collect(Collectors.joining("\n\n"));
    }

    /**
     * Formats a list of attractions (Destination objects) into a display string.
     * @param attractions a list of Destination objects
     * @return a String with formatted attractions
     */
    private String formatAttractions(List<Destination> attractions) {
        return attractions.stream()
                .map(Destination::getName)
                .collect(Collectors.joining("\n\n"));
    }

    /**
     * Extracts city name from trip name (simple heuristic).
     * @param tripName a String: tripName of the Trip object
     * @return a String with the city name
     */
    private String extractCityName(String tripName) {
        // Try to extract city from trip name like "trip to attr10" or "Summer in Tokyo"
        if (tripName.contains(" to ")) {
            final String[] parts = tripName.split(" to ");
            return parts[1].trim();
        }
        if (tripName.contains(" in ")) {
            final String[] parts = tripName.split(" in ");
            return parts[1].trim();
        }
        // Default: return first word or whole name
        return tripName.split(" ")[0];
    }

    @Override
    public void back() {
        loadTripDetailOutputBoundary.back();
    }

}
