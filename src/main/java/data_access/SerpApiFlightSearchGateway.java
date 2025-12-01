package data_access;

import use_case.flight_search.FlightSearchGateway;
import entity.Flight;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

// As we have defined the interface for the FlightSearchGateway,
public class SerpApiFlightSearchGateway implements FlightSearchGateway{
    private static final String API_KEY =
            "1b10e45871e0486cd7fb938a5261325fc3a556c156f75fda42f0ad53c52c7523";
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
            return "ERROR. No search results found (invalid search / api key). Please try again.";
        }

        int start = json.indexOf('{', bestIdx);
        if (start < 0) {
            return "ERROR. No search results found (invalid search / api key). Please try again.";
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

// 1st time = outbound, 2nd time = return (if present)
        String outboundTime = extractNthStringValue(firstBest, "\"time\"", 1);
        String returnTime   = extractNthStringValue(firstBest, "\"time\"", 2);

        String departureField;
        if (returnTime == null || returnTime.isEmpty()) {
            // just in case it's a one-way flight
            departureField = outboundTime;
        } else {
            departureField = "Outbound: " + outboundTime + " | Return: " + returnTime;
        }

        String price = extractNumberValue(firstBest, "\"price\"");

        // 3. Bullet-point summary
        return "- Airline: " + airline + "\n"
                + "- Departure: " + departureField + "\n"
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

    // Helper to extract the nth string value for the same key, e.g. second "time"
    private String extractNthStringValue(String json, String key, int n) {
        int fromIndex = 0;
        for (int count = 1; count <= n; count++) {
            int idx = json.indexOf(key, fromIndex);
            if (idx < 0) {
                return "";
            }
            int firstQuote = json.indexOf('"', idx + key.length());
            int secondQuote = json.indexOf('"', firstQuote + 1);
            if (firstQuote < 0 || secondQuote < 0) {
                return "";
            }
            if (count == n) {
                return json.substring(firstQuote + 1, secondQuote);
            }
            // move search window forward for the next occurrence
            fromIndex = secondQuote + 1;
        }
        return "";
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

    @Override
    public Flight buildFirstFlightEntity(String json) {
        // 1. Locate the first "best flight" object – mirror your summarizeFirstBestFlight code

        int bestFlightsIndex = json.indexOf("\"best_flights\"");
        if (bestFlightsIndex < 0) {
            return null; // no best flights found
        }

        // Find the first { after "best_flights"
        int firstBrace = json.indexOf('{', bestFlightsIndex);
        if (firstBrace < 0) {
            return null;
        }

        int braceCount = 0;
        int i = firstBrace;
        for (; i < json.length(); i++) {
            char c = json.charAt(i);
            if (c == '{') braceCount++;
            if (c == '}') braceCount--;
            if (braceCount == 0) break;
        }

        if (braceCount != 0) {
            return null;
        }

        String firstBest = json.substring(firstBrace, i + 1);

        // 2. Extract fields from this flight JSON

        String airline = extractStringValue(firstBest, "\"airline\"");

        // 1st "time" = outbound, 2nd "time" = return (if present)
        String outboundTime = extractNthStringValue(firstBest, "\"time\"", 1);
        String returnTime   = extractNthStringValue(firstBest, "\"time\"", 2);

        String combinedDepartureTimes;
        if (returnTime == null || returnTime.isEmpty()) {
            combinedDepartureTimes = outboundTime; // one-way fallback
        } else {
            combinedDepartureTimes =
                    "Outbound: " + outboundTime + " | Return: " + returnTime;
        }

        String priceStr = extractNumberValue(firstBest, "\"price\"");
        float price = 0.0f;
        try {
            if (!priceStr.isEmpty()) {
                price = Float.parseFloat(priceStr);
            }
        } catch (NumberFormatException ignored) {
        }

        // 3. Build and return the Flight entity
        // Adjust constructor args/order to match your Flight class!
        return new Flight(airline, combinedDepartureTimes, price);
    }



    private String enc(String s) {
        return URLEncoder.encode(s, StandardCharsets.UTF_8);
    }
}


