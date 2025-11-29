package use_case.complete_trip;

public class CompleteTripInputData {
    private final String tripId;

    public CompleteTripInputData(String tripId) {
        this.tripId = tripId;
    }

    public String getTripId() {
        return tripId;
    }
}