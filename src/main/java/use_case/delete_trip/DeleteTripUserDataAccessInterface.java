package use_case.delete_trip;

/**
 * Data access interface for the Delete Trip Use Case.
 */
public interface DeleteTripUserDataAccessInterface {
    boolean deleteTrip(String username, String tripName);
    String getCurrentUsername();
}
