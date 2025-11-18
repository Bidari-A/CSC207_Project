package interface_adapter.trip;

public class TripDetailsController {
    private final TripViewModel tripViewModel;

    public TripDetailsController(TripViewModel tripViewModel) {
        this.tripViewModel = tripViewModel;
    }

    public void openTripView(String tripName) {
        TripState s = tripViewModel.getState();
        s.setTripName(tripName);

        // Just an example: Fill fields based on name
        switch (tripName.toLowerCase()) {
            case "paris vacation":
            case "paris":
                s.setCity("Paris");
                s.setDate("June 12 – June 20, 2025");
                s.setAttractions("Eiffel Tower\nLouvre Museum\nSeine River Cruise");
                s.setFlightDetails("Air France AF-347\nYYZ → CDG\nDepart: 7:00 PM");
                s.setHotelDetails("Hotel Le Meurice\n5-star luxury suite");
                s.setGeminiInput("What should I pack?");
                break;

            case "tokyo":
                s.setCity("Tokyo");
                s.setDate("May 9 – May 20, 2025");
                s.setAttractions("Shibuya Crossing\nSenso-ji Temple\nSkyTree");
                s.setFlightDetails("JAL 045\nYYZ → HND");
                s.setHotelDetails("Park Hyatt Tokyo");
                s.setGeminiInput("Give me sushi recommendations.");
                break;

            default:
                s.setCity("Unknown");
                s.setDate("Unknown");
                s.setAttractions("N/A");
                s.setFlightDetails("N/A");
                s.setHotelDetails("N/A");
                s.setGeminiInput("");
        }

        tripViewModel.firePropertyChange();
    }
}