package use_case.load_trip_detail;

import java.util.List;
import java.util.stream.Collectors;

import entity.Accommodation;
import entity.Destination;
import entity.Flight;
import entity.Trip;

public class LoadTripDetailInteractor implements LoadTripDetailInputBoundary {
    private final LoadTripDetailDataAccessInterface loadTripDetailDataAccessInterface;
    private final LoadTripDetailOutputBoundary loadTripDetailOutputBoundary;

    public LoadTripDetailInteractor(LoadTripDetailDataAccessInterface loadTripDetailDataAccessInterface, LoadTripDetailOutputBoundary loadTripDetailOutputBoundary) {
        this.loadTripDetailDataAccessInterface = loadTripDetailDataAccessInterface;
        this.loadTripDetailOutputBoundary = loadTripDetailOutputBoundary;
    }

    @Override
    public void execute(LoadTripDetailInputData loadTripDetailInputData) {
        final String prevViewName = loadTripDetailInputData.getPrevViewName();
        String tripId = loadTripDetailInputData.getTripId();

        if (tripId != null && !tripId.isEmpty()) {
            // Load trip from database using trip ID
            Trip trip = loadTripDetailDataAccessInterface.getTrip(tripId);

            if (trip != null) {
                // Format trip data for display
                String tripName = trip.getTripName();
                String dates = trip.getDates();

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

                // Use the destination field from the trip entity
                String cityName = trip.getDestination();
                if (cityName == null || cityName.isEmpty()) {
                    // Fallback to extracting from trip name if destination is not set
                    cityName = extractCityName(tripName);
                }

                final LoadTripDetailOutputData loadTripDetailOutputData = new LoadTripDetailOutputData(
                        tripName, cityName, dates,
                        attractions, flightDetails, hotelDetails, prevViewName);

                loadTripDetailOutputBoundary.prepareTripView(loadTripDetailOutputData);
            } else {
                // Trip not found - show empty/default data
                final LoadTripDetailOutputData loadTripDetailOutputData = new LoadTripDetailOutputData(
                        "Trip not found", "", "",
                        "Trip not found", "Trip not found", "Trip not found", prevViewName);
                loadTripDetailOutputBoundary.prepareTripView(loadTripDetailOutputData);
            }
        } else {
            // No trip ID provided - show empty/default data
            final LoadTripDetailOutputData loadTripDetailOutputData = new LoadTripDetailOutputData(
                    "No trip selected", "", "",
                    "No trip ID provided", "No trip ID provided", "No trip ID provided", prevViewName);
            loadTripDetailOutputBoundary.prepareTripView(loadTripDetailOutputData);
        }
    }

    /**
     * Formats a list of flights into a display string.
     */
    private String formatFlights(List<Flight> flights) {
        return flights.stream()
                .map(flight -> flight.getAirlineName() + "\n" + flight.getDepartureTimes())
                .collect(Collectors.joining("\n\n"));
    }

    /**
     * Formats a list of hotels into a display string.
     */
    private String formatHotels(List<Accommodation> hotels) {
        return hotels.stream()
                .map(hotel -> hotel.getName() + "\n" + hotel.getAddress() +
                        (hotel.getPrice() > 0 ? "\n$" + hotel.getPrice() : ""))
                .collect(Collectors.joining("\n\n"));
    }

    /**
     * Formats a list of attractions (Destination objects) into a display string.
     */
    private String formatAttractions(List<Destination> attractions) {
        return attractions.stream()
                .map(attraction -> attraction.getName() + "\n" +
                        attraction.getAddress() + "\n" +
                        attraction.getDescription() +
                        (attraction.getPrice() > 0 ? "\nPrice: $" + String.format("%.2f", attraction.getPrice()) : ""))
                .collect(Collectors.joining("\n\n"));
    }

    /**
     * Extracts city name from trip name (simple heuristic).
     */
    private String extractCityName(String tripName) {
        // Try to extract city from trip name like "trip to attr10" or "Summer in Tokyo"
        if (tripName.contains(" to ")) {
            String[] parts = tripName.split(" to ");
            if (parts.length > 1) {
                return parts[1].trim();
            }
        }
        if (tripName.contains(" in ")) {
            String[] parts = tripName.split(" in ");
            if (parts.length > 1) {
                return parts[1].trim();
            }
        }
        // Default: return first word or whole name
        return tripName.split(" ")[0];
    }

    @Override
    public void back() {
        loadTripDetailOutputBoundary.back();
    }

}