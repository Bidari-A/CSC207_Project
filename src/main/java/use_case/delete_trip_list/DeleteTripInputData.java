package use_case.delete_trip_list;

public class DeleteTripInputData {

    private final String tripName;
    private final String username;

    public DeleteTripInputData(String tripName, String username) {
        this.tripName = tripName;
        this.username = username;
    }

    public String getTripName() { return tripName; }

    public String getUsername() { return username; }
}