package use_case.load_trip_detail;

public class LoadTripDetailInputData {

    private final String username;
    private final String prevViewName;

    public LoadTripDetailInputData(String username, String prevViewName) {
        this.username = username;
        this.prevViewName = prevViewName;
    }
    public String getUsername() {
        return username;
    }
    public String getPrevViewName() {
        return prevViewName;
    }
}
