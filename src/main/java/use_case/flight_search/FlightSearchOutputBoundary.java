package use_case.flight_search;

public interface FlightSearchOutputBoundary {
    // The presenter, should either take in the outputData to prepareSuccessView
    // OR, should prepare FAIL view

    void prepareSuccessView(FlightSearchOutputData outputData);
    void prepareFailView(String errorMessage);

}
