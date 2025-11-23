package use_case.complete_current_trip;

public class CompleteCurrentTripInputData {
    private final String username;
    private final String tripName;
    private final String city;
    private final String date;

    public CompleteCurrentTripInputData(String username, String tripName, String city, String date) {
        this.username = username;
        this.tripName = tripName;
        this.city = city;
        this.date = date;
    }

    public String getUsername() {
        return username;
    }

    public String getTripName() {
        return tripName;
    }

    public String getCity() {
        return city;
    }

    public String getDate() {
        return date;
    }
}
