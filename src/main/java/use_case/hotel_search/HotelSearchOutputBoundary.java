package use_case.hotel_search;

public interface HotelSearchOutputBoundary {
    void prepareSuccessView(HotelSearchOutputData outputData);
    void prepareFailView(String errorMessage);
}
