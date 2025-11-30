package use_case.delete_trip_list;

public class DeleteTripInputData {

    private final String username;
    private final String tripId;

    public DeleteTripInputData(String username, String tripId) {
        this.username = username;
        this.tripId = tripId;
    }

    public String getUsername() {
        return username;
    }

    public String getTripId() {
        return tripId;
    }
}
