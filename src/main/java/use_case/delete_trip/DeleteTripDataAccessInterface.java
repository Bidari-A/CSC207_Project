package use_case.delete_trip;

public interface DeleteTripDataAccessInterface {
    boolean deleteTrip(String username, String tripName);
}
