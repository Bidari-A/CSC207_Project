package use_case.delete_current_trip;

public class DeleteCurrentTripInteractor {
    private final DeleteCurrentTripOutputBoundary deleteCurrentTripOutputBoundary;
    public DeleteCurrentTripInteractor(DeleteCurrentTripOutputBoundary outputBoundary) {
        this.deleteCurrentTripOutputBoundary = outputBoundary;
    }

    @Override
    public void execute(){
        DeleteCurrentTripOutputData outputData = new DeleteCurrentTripOutputData("Trip deleted successfully.");
        deleteCurrentTripOutputBoundary.prepareSuccessView(outputData);
    }
}
