package use_case.load_trip_detail;

public class LoadTripDetailOutputData {
    private final String tripName;
    private final String cityName;
    private final String date;
    private final String attractions;
    private final String flightDetails;
    private final String hotelDetails;
    private final String prevViewName;

    public LoadTripDetailOutputData(String tripName, String cityName, String date,
                                    String attractions, String flightDetails, String hotelDetails, String prevViewName) {
        this.tripName = tripName;
        this.cityName = cityName;
        this.date = date;
        this.attractions = attractions;
        this.flightDetails = flightDetails;
        this.hotelDetails = hotelDetails;
        this.prevViewName = prevViewName;
    }

    public String getTripName() {
        return tripName;
    }
    public String getCityName() {
        return cityName;
    }
    public String getDate() {
        return date;
    }
    public String getAttractions() {
        return attractions;
    }
    public String getFlightDetails() {
        return flightDetails;
    }
    public String getHotelDetails() {
        return hotelDetails;
    }
    public String getPrevViewName() {
        return prevViewName;
    }

}
