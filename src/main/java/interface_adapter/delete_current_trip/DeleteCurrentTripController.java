package interface_adapter.delete_current_trip;

import use_case.delete_current_trip.DeleteCurrentTripInputBoundary;

public class DeleteCurrentTripController {
    private final DeleteCurrentTripInputBoundary interactor;
    public DeleteCurrentTripController(DeleteCurrentTripInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void delete(){
        interactor.execute();
    }
}
