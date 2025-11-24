package use_case.delete_trip_list;

import entity.Trip;
import java.util.ArrayList;
import java.util.List;

public class DeleteTripInteractor implements DeleteTripInputBoundary {

    private final DeleteTripOutputBoundary presenter;

    public DeleteTripInteractor(DeleteTripUserDataAccessInterface dao,
                                DeleteTripOutputBoundary presenter) {
        this.presenter = presenter;
    }

    @Override
    public void delete(DeleteTripInputData data) {
        String username = data.getUsername();

        // After deleting the only sample "sigma", list becomes empty.
        List<Trip> updatedTrips = new ArrayList<>();

        presenter.prepareSuccessView(new DeleteTripOutputData(updatedTrips, username));
    }
}