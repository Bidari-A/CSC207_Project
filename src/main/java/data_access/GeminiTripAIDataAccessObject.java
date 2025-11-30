package data_access;

import use_case.create_new_trip.TripAIDataAccessInterface;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * Implementation of the TripAIDataAccessInterface that will
 * the Google Gemini API.
 * For testing, it temporarily just returns a placeholder string.
 */
public class GeminiTripAIDataAccessObject implements TripAIDataAccessInterface {

    private final String apiKey;

    public GeminiTripAIDataAccessObject(String apiKey) {
        this.apiKey = apiKey;
    }


    @Override
    public String generateTripPlan(String prompt) {
        try {
            String endpoint =
                    "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key="
                            + apiKey;

            String requestBody = """
                {
                  "contents": [
                    {
                      "parts": [
                        { "text": "%s" }
                      ]
                    }
                  ]
                }
                """.formatted(prompt);

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(endpoint))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            return parseGeminiResponse(response.body());

        } catch (Exception e) {
            e.printStackTrace();
            return "Error calling Gemini API: " + e.getMessage();
        }
    }

    private String parseGeminiResponse(String responseBody) {
        // Gemini returns a nested JSON structure.
        // For now, extract the first text field using a simple search.
        // Later we can make it robust with Gson or Jackson.

        // Example response contains something like:
        // "text": "your generated content..."

        int index = responseBody.indexOf("\"text\":");
        if (index == -1) {
            return responseBody;
        }

        int start = responseBody.indexOf("\"", index + 7) + 1;
        int end = responseBody.indexOf("\"", start);

        if (start == -1 || end == -1) {
            return responseBody;
        }

        String text = responseBody.substring(start, end);
        // turn "\n" into real newlines
        text = text.replace("\\n", System.lineSeparator());
        return text;
    }




}
