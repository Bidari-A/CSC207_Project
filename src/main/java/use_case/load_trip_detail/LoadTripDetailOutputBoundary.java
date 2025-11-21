package use_case.load_trip_detail;

public interface LoadTripDetailOutputBoundary {
    void prepareTripView(LoadTripDetailOutputData outputData);
    void back();
}
