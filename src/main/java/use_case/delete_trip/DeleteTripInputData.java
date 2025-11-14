package use_case.delete_trip;

public class DeleteTripInputData {
    private final String username;
    private final String tripName;

    public DeleteTripInputData(String username, String tripName) {
        this.username = username;
        this.tripName = tripName;
    }

    public String getUsername() {
        return username;
    }

    public String getTripName() {
        return tripName;
    }
}
