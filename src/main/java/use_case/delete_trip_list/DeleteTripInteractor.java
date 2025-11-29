package use_case.delete_trip_list;

import entity.Trip;

import java.util.List;

public class DeleteTripInteractor implements DeleteTripInputBoundary {

    private final DeleteTripUserDataAccessInterface dao;
    private final DeleteTripOutputBoundary presenter;

    public DeleteTripInteractor(DeleteTripUserDataAccessInterface dao,
                                DeleteTripOutputBoundary presenter) {
        this.dao = dao;
        this.presenter = presenter;
    }

    @Override
    public void delete(DeleteTripInputData data) {
        String username = data.getUsername();
        String tripId = data.getTripId();

        // 1. 调用 DAO 删除
        boolean success = dao.deleteTrip(username, tripId);
        if (!success) {
            presenter.prepareFailView("Failed to delete trip.");
            return;
        }

        // 2. 删除成功后重新加载该用户的 trip 列表
        List<Trip> updatedTrips = dao.getTrips(username);

        // 3. 输出结果给 Presenter
        DeleteTripOutputData outputData = new DeleteTripOutputData(updatedTrips, username);
        presenter.prepareSuccessView(outputData);
    }
}