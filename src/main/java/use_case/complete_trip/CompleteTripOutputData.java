package use_case.complete_trip;

public class CompleteTripOutputData {
    private final String tripId;
    private final String tripName;

    public CompleteTripOutputData(String tripId, String tripName) {
        this.tripId = tripId;
        this.tripName = tripName;
    }

    public String getTripId() {
        return tripId;
    }

    public String getTripName() {
        return tripName;
    }
}