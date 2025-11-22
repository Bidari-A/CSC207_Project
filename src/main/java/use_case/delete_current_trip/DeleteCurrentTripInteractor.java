package use_case.delete_current_trip;

public class DeleteCurrentTripInteractor implements DeleteCurrentTripInputBoundary {

    private final DeleteCurrentTripOutputBoundary presenter;

    public DeleteCurrentTripInteractor(DeleteCurrentTripOutputBoundary presenter) {
        this.presenter = presenter;
    }

    @Override
    public void deleteDraft(){
        presenter.presentClearedDraft();
    }
}
