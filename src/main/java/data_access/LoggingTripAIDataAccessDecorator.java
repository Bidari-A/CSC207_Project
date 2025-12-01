package data_access;

import use_case.create_new_trip.TripAIDataAccessInterface;

/**
 * A decorator for {@link TripAIDataAccessInterface} that logs prompts and
 * responses for trip plan generation.
 *
 * This class wraps another {@code TripAIDataAccessInterface} implementation
 * and adds logging around calls to {@link #generateTripPlan(String)} without
 * changing the behavior of the underlying gateway.
 */
public class LoggingTripAIDataAccessDecorator implements TripAIDataAccessInterface {

    /**
     * The underlying data access object that performs the real AI call.
     */
    private final TripAIDataAccessInterface delegate;

    /**
     * Creates a new logging decorator that wraps the given data access object.
     *
     * @param delegate the concrete {@link TripAIDataAccessInterface}
     *                 implementation to wrap
     */
    public LoggingTripAIDataAccessDecorator(TripAIDataAccessInterface delegate) {
        // Store the wrapped implementation so that this decorator can
        // delegate the actual work to it.
        this.delegate = delegate;
    }

    /**
     * Logs the prompt that is sent to the AI gateway and logs that a response
     * was received, while delegating the real work to the wrapped
     * {@code delegate}.
     *
     * @param prompt the plain text prompt that describes the trip and
     *               requested itinerary format
     * @return the itinerary text returned by the wrapped AI gateway
     */
    @Override
    public String generateTripPlan(String prompt) {
        // Log the request prompt before calling the real gateway
        System.out.println("[AI LOG] Sending trip plan prompt to Gemini! ");

        // Delegate the actual AI call to the wrapped implementation
        String itinerary = delegate.generateTripPlan(prompt);

        // Log that a response was received
        System.out.println("[AI LOG] Successfully received itinerary text from Gemini.");

        // Optionally, you could log a short preview of the itinerary here
        // System.out.println("[AI LOG] Itinerary preview:
        // " + itinerary.substring(0, Math.min(200, itinerary.length())));

        // Return the response unchanged
        return itinerary;
    }
}

