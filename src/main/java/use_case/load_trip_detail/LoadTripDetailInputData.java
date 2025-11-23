package use_case.load_trip_detail;

public class LoadTripDetailInputData {

    private final String username;
    private final String prevViewName;
    private final String tripName; // Optional: if null, load from draft

    public LoadTripDetailInputData(String username, String prevViewName) {
        this.username = username;
        this.prevViewName = prevViewName;
        this.tripName = null;
    }

    public LoadTripDetailInputData(String username, String prevViewName, String tripName) {
        this.username = username;
        this.prevViewName = prevViewName;
        this.tripName = tripName;
    }

    public String getUsername() {
        return username;
    }
    public String getPrevViewName() {
        return prevViewName;
    }
    public String getTripName() {
        return tripName;
    }
}
