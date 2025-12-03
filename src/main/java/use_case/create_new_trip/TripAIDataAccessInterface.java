package use_case.create_new_trip;

/**
 * Data access interface for generating a trip plan using Google Gemini service.
 */
public interface TripAIDataAccessInterface {

  /**
     * Generates a trip plan (or suggestions) based on the given prompt.
     *
     * @param prompt the text prompt that describes the user’s trip request
     * @return the AI generated trip plan as plain text in a string
  */
  String generateTripPlan(String prompt);
}