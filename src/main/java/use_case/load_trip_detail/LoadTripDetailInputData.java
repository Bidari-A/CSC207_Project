package use_case.load_trip_detail;

public class LoadTripDetailInputData {

    private final String username;
    private final String prevViewName;
    private String tripId;

    public LoadTripDetailInputData(String username, String prevViewName) {
        this.username = username;
        this.prevViewName = prevViewName;
        this.tripId = null;
    }

    public LoadTripDetailInputData(String username, String prevViewName, String tripId) {
        this(username, prevViewName);
        this.tripId = tripId;
    }

    public String getUsername() {
        return username;
    }

    public String getPrevViewName() {
        return prevViewName;
    }

    public String getTripId() {
        return tripId;
    }
}
