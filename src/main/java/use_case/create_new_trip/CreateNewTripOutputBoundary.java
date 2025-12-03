package use_case.create_new_trip;

/**
 * Output boundary for the Create New Trip use case.
 * This interface defines how the interactor communicates results
 * and view transitions to the presenter layer.
 */
public interface CreateNewTripOutputBoundary {

  /**
     * Prepares the screen for creating a new trip.
     * Called when the user initially opens the Create New Trip form.
     */
  void prepareScreen();

  /**
     * Prepares the view shown when navigating back from another screen
     * to the previous Create New Trip view.
     */
  void prepareBackView();

  /**
     * Presents the generated trip result after the interactor builds the
     * itinerary through the AI gateway (Gemini).
     *
     * @param outputData the data containing trip name, dates, cities,
     *                   and the generated itinerary text
     */
  void presentResult(CreateNewTripOutputData outputData);

  /*** Requests navigation from the Trip Result view back to the
  * Create New Trip view.
  */
  void presentBackToCreateNewTripView();
}
