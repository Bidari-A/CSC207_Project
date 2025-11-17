package data_access;

import use_case.flight_search.FlightSearchGateway;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

// As we have defined the interface for the FlightSearchGateway,
public class SerpApiFlightSearchGateway implements FlightSearchGateway{
    private static final String API_KEY =
            "";
    // As the interface defines that we need implement this method called fetchRawJson
    public String fetchRawJson(String from, String to, String outboundDate, String returnDate){

        // STEP 1: LETS DEFINE THE QUERY!!!
        String url = "https://serpapi.com/search"
                + "?api_key=" + enc(API_KEY) // enc is a function that does character encoding
                + "&engine=google_flights"
                + "&hl=en&gl=us&currency=USD"
                + "&departure_id=" + enc(from)
                + "&arrival_id=" + enc(to)
                + "&outbound_date=" + enc(outboundDate)
                + "&return_date=" + enc(returnDate);

        // STEP 2: LETS make the request!!!
        try {
            // Recall builder is a design pattern we learnt :)
            HttpRequest req = HttpRequest.newBuilder()
                    .uri(URI.create(url)) // Create the URI (URL, but also with the resource locator)
                    .GET() // Use a GET request to fetch information
                    .build(); // Standard building the obj

            // Now we defined our request, we should send it
            // Define a response to store it
            HttpResponse<String> resp = HttpClient.newHttpClient().send(req, HttpResponse.BodyHandlers.ofString());
            return resp.body();
        }
        catch(Exception e){
            throw new RuntimeException(e);
        }

    }

    // Here, we define enc(ode) to encode s into UTF_8
    private String enc(String s) {
        return URLEncoder.encode(s, StandardCharsets.UTF_8);
    }
}
