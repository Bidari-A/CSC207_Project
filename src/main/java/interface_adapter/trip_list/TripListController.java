package interface_adapter.trip_list;

/**
 * The control for the Trip List Use Case.
 * Note: Trip list loading and delete logic have been removed - use ViewModel directly.
 */
public class TripListController {

    public TripListController() {
    }

    /**
     * Executes the Trip List Use Case to load all trips.
     * Note: This method is now a no-op. Set trips directly in TripListViewModel.
     * @param username the username of the user
     */
    public void execute(String username) {
        // Use case logic removed - set trips directly in ViewModel
    }

    /**
     * Executes the Delete Trip Use Case.
     * Note: This method is now a no-op. Delete trips directly from ViewModel if needed.
     * @param username the username of the user
     * @param tripName the name of the trip to delete
     */
    public void executeDelete(String username, String tripName) {
        // Delete trip logic removed - update ViewModel directly if needed
    }
}
