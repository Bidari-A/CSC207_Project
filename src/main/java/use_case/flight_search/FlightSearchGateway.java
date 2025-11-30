package use_case.flight_search;
import entity.Flight;
//This gateway interface defines that it has such fetchRawJson format.
public interface FlightSearchGateway {
    String fetchRawJson(String from, String to, String outboundDate, String returnDate);

    String summarizeFirstBestFlight(String json);


    // NEW: build a Flight entity for the first/best option
    Flight buildFirstFlightEntity(String json);
}