package use_case.complete_trip;

public class CompleteTripOutputData {
    private final String tripId;
    private final String status;

    public CompleteTripOutputData(String tripId, String status) {
        this.tripId = tripId;
        this.status = status;
    }

    public String getTripId() {
        return tripId;
    }

    public String getStatus() {
        return status;
    }
}