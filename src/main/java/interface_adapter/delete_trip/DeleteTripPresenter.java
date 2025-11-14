package interface_adapter.delete_trip;

import use_case.delete_trip.DeleteTripOutputBoundary;
import use_case.delete_trip.DeleteTripOutputData;
import use_case.trip_list.TripListInputBoundary;
import use_case.trip_list.TripListInputData;

/**
 * The Presenter for the Delete Trip Use Case.
 */
public class DeleteTripPresenter implements DeleteTripOutputBoundary {

    private final TripListInputBoundary tripListInputBoundary;

    public DeleteTripPresenter(TripListInputBoundary tripListInputBoundary) {
        this.tripListInputBoundary = tripListInputBoundary;
    }

    @Override
    public void prepareSuccessView(DeleteTripOutputData response) {
        // Refresh the trip list after successful deletion
        TripListInputData tripListInputData = new TripListInputData(response.getUsername());
        tripListInputBoundary.execute(tripListInputData);
    }

    @Override
    public void prepareFailView(String error) {
        // Error handling could be added here if needed
        System.out.println("Delete trip error: " + error);
    }
}
