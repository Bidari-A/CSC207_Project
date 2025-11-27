package use_case.flight_search;

//This gateway interface defines that it has such fetchRawJson format.
public interface FlightSearchGateway {
    String fetchRawJson(String from, String to, String outboundDate, String returnDate);

    String summarizeFirstBestFlight(String json);
}
