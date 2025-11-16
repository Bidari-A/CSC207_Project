package interface_adapter.trip_list;

import interface_adapter.ViewManagerModel;

/**
 * Simple controller implementation that navigates to the trip list view.
 */

public class TripListControllerImpl implements TripListController {

    private final ViewManagerModel viewManagerModel;

    public TripListControllerImpl(ViewManagerModel viewManagerModel) {
        this.viewManagerModel = viewManagerModel;
    }

    /**
     * Navigates to the trip list view.
     * @param username the username (not used for simple navigation)
     * @param tripName the trip name (not used for simple navigation)
     */
    @Override
    public void executeDetails(String username, String tripName) {
        viewManagerModel.setState("trip list");
        viewManagerModel.firePropertyChange();
    }


    /**
     * Placeholder for delete functionality.
     * @param username the username
     * @param tripName the trip name to delete
     */
    @Override
    public void executeDelete(String username, String tripName) {
        // TODO: Implement delete functionality when needed
    }
}