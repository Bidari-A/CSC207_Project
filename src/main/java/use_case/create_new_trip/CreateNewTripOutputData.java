package use_case.create_new_trip;

/**
 * Output data for the Create New Trip use case.
 * This class carries all information needed by the presenter to display
 * the generated itinerary and trip overview to the user.
 */
public class CreateNewTripOutputData {

    private final String tripName;
    private final String from;
    private final String to;
    private final String startDate;
    private final String endDate;
    private final String itinerary;

  /**
  * Constructs an object containing all output information for the
  * Create New Trip use case.
  *
  * @param tripName  the generated name of the trip
  * @param from      the starting location provided by the user
  * @param to        the destination location provided by the user
  * @param startDate the start date of the trip
  * @param endDate   the end date of the trip
  * @param itinerary the full AI generated itinerary text
  */
  public CreateNewTripOutputData(String tripName,
                                   String from,
                                   String to,
                                   String startDate,
                                   String endDate,
                                   String itinerary) {
    this.tripName = tripName;
    this.from = from;
    this.to = to;
    this.startDate = startDate;
    this.endDate = endDate;
    this.itinerary = itinerary;
  }

  /**
     * Returns the name of the trip.
     *
     * @return trip name
     */
  public String getTripName() {
    return tripName;
  }

  /**
     * Returns the origin location of the trip.
     *
     * @return origin location
     */
  public String getFrom() {
    return from;
  }

  /**
     * Returns the destination location of the trip.
     *
     * @return destination location
     */
  public String getTo() {
    return to;
  }

  /**
     * Returns the start date of the trip.
     *
     * @return start date
     */
  public String getStartDate() {
    return startDate;
  }

  /**
     * Returns the end date of the trip.
     *
     * @return end date
  */
  public String getEndDate() {
    return endDate;
  }

  /**
  * Returns the full AI generated itinerary.
     *
     * @return itinerary text
     */
  public String getItinerary() {
    return itinerary;
    }
}
