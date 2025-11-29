package use_case.delete_trip_list;

public class DeleteTripInputData {

    private final String tripId;
    private final String username;

    public DeleteTripInputData(String tripId, String username) {
        this.tripId = tripId;
        this.username = username;
    }

    public String getTripId() { return tripId; }

    public String getUsername() { return username; }
}
