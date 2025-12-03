package use_case.create_new_trip;

/**
 * Input data for the Create New Trip use case.
 * This class carries all information provided by the user when requesting
 * a new trip to be generated.
 */
public class CreateNewTripInputData {
  private final String from;
  private final String to;
  private final String startDate;
  private final String endDate;
  private final String currentUsername;
  /**
     * Constructs an input data object for the Create New Trip use case.
     *
     * @param from             the starting location
     * @param to               the destination location
     * @param date             the start date of the trip
     * @param endDate          the end date of the trip
     * @param currentUsername  the username of the user creating the trip
     */

  public CreateNewTripInputData(final String from, final String to,
                                  final String date, final String endDate,
                                  final String currentUsername) {
    this.from = from;
    this.to = to;
    this.startDate = date;
    this.endDate = endDate;
    this.currentUsername = currentUsername;
  }
  /**
     * Returns the starting location.
     *
     * @return the origin city or location
     */
  public String getFrom() {
    return from;
  }
  /**
     * Returns the destination location.
     *
     * @return the destination city or location
     */

  public String getTo() {
    return to;
  }

  /**
     * Returns the start date of the trip.
     *
     * @return the start date as a string
     */
  public String getStartDate() {
    return startDate;
  }

  /**
  * Returns the end date of the trip.
  *
  * @return the end date as a string
  */
  public String getEndDate() {
    return endDate;
  }

  /**
     * Returns the username of the current user.
     *
     * @return the current username
   */
  public String getCurrentUsername() {
    return currentUsername;
    }
}