package interface_adapter.trip;

/**
 * State for the Trip View.
 * Holds the data that the TripView displays.
 */
public class TripState{

    private String tripName = "";
    private String city = "";
    private String date = "";
    private String attractions = "";
    private String flightDetails = "";
    private String hotelDetails = "";
    private String geminiInput = "";

    private String prevViewName = "";

    public TripState() {}

    public TripState(TripState copy) {
        this.tripName = copy.tripName;
        this.city = copy.city;
        this.date = copy.date;
        this.attractions = copy.attractions;
        this.flightDetails = copy.flightDetails;
        this.hotelDetails = copy.hotelDetails;
        this.geminiInput = copy.geminiInput;
    }

    @Override
    public TripState clone() {
        return new TripState(this);
    }

    public String getTripName() { return tripName; }
    public void setTripName(String tripName) { this.tripName = tripName; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getAttractions() { return attractions; }
    public void setAttractions(String attractions) { this.attractions = attractions; }

    public String getFlightDetails() { return flightDetails; }
    public void setFlightDetails(String flightDetails) { this.flightDetails = flightDetails; }

    public String getHotelDetails() { return hotelDetails; }
    public void setHotelDetails(String hotelDetails) { this.hotelDetails = hotelDetails; }

    public String getGeminiInput() { return geminiInput; }
    public void setGeminiInput(String geminiInput) { this.geminiInput = geminiInput; }

    public String getPrevViewName() { return prevViewName; }
    public void setPrevViewName(String prevViewName) { this.prevViewName = prevViewName; }
}
