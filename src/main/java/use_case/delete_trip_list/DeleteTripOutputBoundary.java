package use_case.delete_trip_list;

public interface DeleteTripOutputBoundary {
    void prepareSuccessView(DeleteTripOutputData data);
    void prepareFailView(String errorMessage);
}
