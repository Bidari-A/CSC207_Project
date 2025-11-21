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
            "2d01bcb72f977a8492841e90758de3c8ad79e19b307df2979d99f39d1127fb40";
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
    public String summarizeFirstBestFlight(String json) {
        // 1. Grab first object in "best_flights"
        int bestIdx = json.indexOf("\"best_flights\"");
        if (bestIdx < 0) {
            return "No best_flights found.";
        }

        int start = json.indexOf('{', bestIdx);
        if (start < 0) {
            return "No flight object found.";
        }

        int depth = 0;
        int i = start;
        for (; i < json.length(); i++) {
            char c = json.charAt(i);
            if (c == '{') depth++;
            else if (c == '}') {
                depth--;
                if (depth == 0) {
                    i++; // include closing brace
                    break;
                }
            }
        }

        String firstBest = json.substring(start, i);

        // 2. Extract essentials
        String airline = extractStringValue(firstBest, "\"airline\"");
        String departureTime = extractStringValue(firstBest, "\"time\""); // first time = first departure
        String price = extractNumberValue(firstBest, "\"price\"");

        // 3. Bullet-point summary
        return "- Airline: " + airline + "\n"
                + "- Departure: " + departureTime + "\n"
                + "- Price: $" + price + " USD";
    }

    // Helper to extract a string value like "name": "Puri Bali Hotel"
    private String extractStringValue(String json, String key) {
        int idx = json.indexOf(key);
        if (idx < 0) return "";
        int firstQuote = json.indexOf('"', idx + key.length());
        int secondQuote = json.indexOf('"', firstQuote + 1);
        if (firstQuote < 0 || secondQuote < 0) return "";
        return json.substring(firstQuote + 1, secondQuote);
    }

    // Helper to extract a number after a key, e.g. "price": 824
    private String extractNumberValue(String json, String key) {
        int idx = json.indexOf(key);
        if (idx < 0) return "";
        int colon = json.indexOf(':', idx);
        int i = colon + 1;
        while (i < json.length() && !Character.isDigit(json.charAt(i))) i++;
        int start = i;
        while (i < json.length() && (Character.isDigit(json.charAt(i)) || json.charAt(i) == '.')) i++;
        return json.substring(start, i);
    }
    // Here, we define enc(ode) to encode s into UTF_8
    private String enc(String s) {
        return URLEncoder.encode(s, StandardCharsets.UTF_8);
    }
}


