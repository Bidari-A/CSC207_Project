package use_case.delete_trip;

/**
 * The Delete Trip Interactor.
 */
public class DeleteTripInteractor implements DeleteTripInputBoundary {
    private final DeleteTripUserDataAccessInterface userDataAccessObject;
    private final DeleteTripOutputBoundary deleteTripPresenter;

    public DeleteTripInteractor(DeleteTripUserDataAccessInterface userDataAccessInterface,
                                DeleteTripOutputBoundary deleteTripOutputBoundary) {
        this.userDataAccessObject = userDataAccessInterface;
        this.deleteTripPresenter = deleteTripOutputBoundary;
    }

    @Override
    public void execute(DeleteTripInputData deleteTripInputData) {
        final String username = deleteTripInputData.getUsername();
        final String tripName = deleteTripInputData.getTripName();

        final boolean success = userDataAccessObject.deleteTrip(username, tripName);

        if (success) {
            final DeleteTripOutputData deleteTripOutputData = new DeleteTripOutputData(username, true);
            deleteTripPresenter.prepareSuccessView(deleteTripOutputData);
        } else {
            deleteTripPresenter.prepareFailView("Failed to delete trip: " + tripName);
        }
    }
}
