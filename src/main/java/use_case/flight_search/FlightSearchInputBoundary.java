package use_case.flight_search;

// Notes: so, we should make this InputBoundary Interface, so the class taking it, which should be
// the FlightSearchUseCaseInteractor, should have the 'execute' method.
public interface FlightSearchInputBoundary {
    void execute(FlightSearchInputData input);
}
