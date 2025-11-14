package use_case.trip_list;

public interface TripListOutputBoundary {
    void prepareSuccessView(TripListOutputData tripListOutputData);
    void prepareFailView(String error);
}
