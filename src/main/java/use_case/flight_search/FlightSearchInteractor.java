package use_case.flight_search;

// Now, this interactor TAKES IN the FlightSearchInputData. So this is an input boundary instance.
public class FlightSearchInteractor implements FlightSearchInputBoundary{
    // this Interactor needs to know
    //1. The gateway to perform HTTPS request
    //2. The presenter that this class can tell it to update the state
    // Recall the presenter takes in the outputData, so it is a OutputBoundary
    private final FlightSearchGateway gateway;
    private final FlightSearchOutputBoundary presenter;

    public FlightSearchInteractor(FlightSearchGateway gateway, FlightSearchOutputBoundary presenter){
        this.gateway = gateway;
        this.presenter = presenter;
    }

    // Remember the Controller will call this interactor through .execute!
    public void execute(FlightSearchInputData inputData){
        // STEP 1. GET the json object and STORE in 'json'
        String json = gateway.fetchRawJson(
                inputData.getFrom(),
                inputData.getTo(),
                inputData.getOutboundDate(),
                inputData.getReturnDate()
        );
        // OR,
        // String json = gateway.fetchRawJson(...);
        // String summary = ((SerpApiFlightSearchGateway) gateway)
        //        .summarizeFirstBestFlight(json);


        // STEP 2. What we want to output to the user! Stored in summary.
        String bestFlightsJson = gateway.summarizeFirstBestFlight(json);

        String summary = bestFlightsJson;  // put raw JSON into summary
        FlightSearchOutputData out = new FlightSearchOutputData(summary);
        presenter.prepareSuccessView(out);
    }
    private String extractFirstBestFlight(String json) {
        int bestIdx = json.indexOf("\"best_flights\"");
        if (bestIdx < 0) {
            return json; // fallback: whole JSON
        }

        // find first '{' after "best_flights" – start of first object
        int start = json.indexOf('{', bestIdx);
        if (start < 0) {
            return json;
        }

        int depth = 0;
        int i = start;
        for (; i < json.length(); i++) {
            char c = json.charAt(i);
            if (c == '{') {
                depth++;
            } else if (c == '}') {
                depth--;
                if (depth == 0) {
                    i++; // include closing brace
                    break;
                }
            }
        }
        return json.substring(start, i);
    }
}