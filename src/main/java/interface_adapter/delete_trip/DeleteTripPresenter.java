package interface_adapter.delete_trip;

import use_case.delete_trip.DeleteTripOutputBoundary;
import use_case.delete_trip.DeleteTripOutputData;

/**
 * The Presenter for the Delete Trip Use Case.
 * Note: Trip list refresh logic removed - update TripListViewModel directly after deletion.
 */
public class DeleteTripPresenter implements DeleteTripOutputBoundary {

    public DeleteTripPresenter() {
    }

    @Override
    public void prepareSuccessView(DeleteTripOutputData response) {
        // Trip list refresh removed - update TripListViewModel directly if needed
    }

    @Override
    public void prepareFailView(String error) {
        // Error handling could be added here if needed
        System.out.println("Delete trip error: " + error);
    }
}
