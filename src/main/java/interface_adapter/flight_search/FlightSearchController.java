package interface_adapter.flight_search;


import use_case.flight_search.FlightSearchInputBoundary;
import use_case.flight_search.FlightSearchInputData;
public class FlightSearchController {

    private final FlightSearchInputBoundary inputBoundary;
    public FlightSearchController(FlightSearchInputBoundary inputBoundary) {
        this.inputBoundary = inputBoundary;
    }

    // BASICALLY we just conform to the InputBoundary, and we bundle the data into a InputData object
    // Then we perform execute on the inputBoudnary which is going to be the UseCaseInteractor here
    public void execute(String from, String to, String outboundDate, String returnDate){
        FlightSearchInputData data =
                new FlightSearchInputData(from, to, outboundDate, returnDate);
        inputBoundary.execute(data);
    }
}
