package data_access;

import use_case.hotel_search.HotelSearchGateway;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

// As we have defined the interface for the HotelSearchGateway,
public class SerpApiHotelSearchGateway implements HotelSearchGateway {

    private static final String API_KEY =
            "";
    // As the interface defines that we need implement this method called fetchRawJson
    // Query, means the search keyword. For example, Bali, YYZ, NYC.
    @Override
    public String fetchRawJson(String query, String checkInDate, String checkOutDate) {
        // STEP 1: LETS DEFINE THE QUERY!!!
        String url = "https://serpapi.com/search"
                + "?api_key=" + enc(API_KEY)
                + "&engine=google_hotels"
                + "&hl=en"
                + "&gl=us"
                + "&currency=USD"
                + "&adults=1"
                + "&q=" + enc(query)
                + "&check_in_date=" + enc(checkInDate)
                + "&check_out_date=" + enc(checkOutDate);
        // STEP 2: LETS make the request!!!
        try {
            HttpRequest req = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            HttpResponse<String> resp =
                    HttpClient.newHttpClient().send(req, HttpResponse.BodyHandlers.ofString());

            return resp.body(); // full hotel JSON

            // Here, we define enc(ode) to encode s into UTF_8
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    // Returns a bullet-point summary for the first hotel result (ads[0])
    public String summarizeFirstHotel(String json) {
        // 1. Extract parameters from "search_parameters"
        String checkInDate = extractStringValue(json, "\"check_in_date\"");
        String checkOutDate = extractStringValue(json, "\"check_out_date\"");
        String adults = extractNumberValue(json, "\"adults\"");
        String query = extractStringValue(json, "\"q\"");  // e.g. "Bali Resorts"

        // 2. Grab first object in "ads" (first hotel result)
        int adsIdx = json.indexOf("\"ads\"");
        if (adsIdx < 0) {
            return "No ads found.";
        }

        int start = json.indexOf('{', adsIdx);
        if (start < 0) {
            return "No hotel object found.";
        }

        int depth = 0;
        int i = start;
        for (; i < json.length(); i++) {
            char c = json.charAt(i);
            if (c == '{') depth++;
            else if (c == '}') {
                depth--;
                if (depth == 0) {
                    i++; // include final brace
                    break;
                }
            }
        }

        String firstHotel = json.substring(start, i);

        // 3. Extract fields from the hotel object
        String name = extractStringValue(firstHotel, "\"name\"");
        String price = extractStringValue(firstHotel, "\"price\"");
        String rating = extractNumberValue(firstHotel, "\"overall_rating\"");
        String reviews = extractNumberValue(firstHotel, "\"reviews\"");

        // 4. Bullet-point summary
        return "- Hotel: " + name + "\n"
                + "- Query: " + query + "\n"
                + "- Check-in: " + checkInDate + "\n"
                + "- Check-out: " + checkOutDate + "\n"
                + "- Adults: " + adults + "\n"
                + "- Rating: " + rating + "/5 (" + reviews + " reviews)\n"
                + "- Price: " + price;
    }


// Reuse same helpers as in flight DAO;
// if you want, you can duplicate them here for simplicity.

    private String extractStringValue(String json, String key) {
        int idx = json.indexOf(key);
        if (idx < 0) return "";
        int firstQuote = json.indexOf('"', idx + key.length());
        int secondQuote = json.indexOf('"', firstQuote + 1);
        if (firstQuote < 0 || secondQuote < 0) return "";
        return json.substring(firstQuote + 1, secondQuote);
    }

    private String extractNumberValue(String json, String key) {
        int idx = json.indexOf(key);
        if (idx < 0) return "";
        int colon = json.indexOf(':', idx);
        int i = colon + 1;

        while (i < json.length() &&
                !Character.isDigit(json.charAt(i)) &&
                json.charAt(i) != '-') i++;

        int start = i;

        while (i < json.length() &&
                (Character.isDigit(json.charAt(i)) || json.charAt(i) == '.')) i++;

        if (start >= json.length()) return "";
        return json.substring(start, i);
    }


    private String enc(String s) {
        return URLEncoder.encode(s, StandardCharsets.UTF_8);
    }
}
