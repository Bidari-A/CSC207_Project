package use_case.delete_trip_list;

import entity.Trip;
import interface_adapter.ViewManagerModel;
import interface_adapter.trip_list.TripListPresenter;
import interface_adapter.trip_list.TripListState;
import interface_adapter.trip_list.TripListViewModel;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class DeleteTripListTest {

    private TripListViewModel tripListViewModel;
    private ViewManagerModel viewManagerModel;
    private TripListPresenter presenter;

    @BeforeEach
    void setup() {
        tripListViewModel = new TripListViewModel();
        viewManagerModel = new ViewManagerModel();
        presenter = new TripListPresenter(viewManagerModel, tripListViewModel);
    }


    // ============================================================
    // 1) 测试成功删除后 Presenter 是否正确更新 ViewModel
    // ============================================================
    @Test
    void testPresenterSuccessUpdatesViewModel() {

        // 模拟删除后的剩余 Trip 列表
        List<Trip> updatedTrips = new ArrayList<>();
        updatedTrips.add(new Trip(
                "T100",
                "TripToTokyo",
                "a",
                "CURRENT",
                "2025-01-01 to 2025-01-05",
                "Tokyo",
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>()
        ));

        // Create outputData
        DeleteTripOutputData outputData =
                new DeleteTripOutputData(updatedTrips, "a");

        // 调用 presenter
        presenter.prepareSuccessView(outputData);

        TripListState state = tripListViewModel.getState();

        assertNotNull(state);
        assertEquals("a", state.getUsername());
        assertEquals(updatedTrips, state.getTrips());
        assertNull(state.getError());

        // 额外检查：viewManagerModel 应该跳回同一个 view (trip list)
        assertEquals("trip list", viewManagerModel.getState());
    }


    // ============================================================
    // 2) 测试删除失败时 Presenter 是否设置 error
    // ============================================================
    @Test
    void testPresenterFailUpdatesViewModel() {

        presenter.prepareFailView("Delete failed!");

        TripListState state = tripListViewModel.getState();

        assertNotNull(state);
        assertEquals("Delete failed!", state.getError());
    }


    // ============================================================
    // 3) 测试 TripListController 是否会调用 interactor.delete()
    //    ⭐ 你使用 TripListController，所以 test 应该测试它
    // ============================================================
    @Test
    void testControllerCallsDeleteInteractor() {

        final boolean[] called = {false};

        // Mock interactor
        DeleteTripInputBoundary mockInteractor = new DeleteTripInputBoundary() {
            @Override
            public void delete(DeleteTripInputData data) {
                called[0] = true;

                // 检查参数是否传对
                assertEquals("a", data.getUsername());
                assertEquals("T001", data.getTripId());
            }
        };

        // 使用 TripListController（你项目里确实使用这个 controller）
        interface_adapter.trip_list.TripListController controller =
                new interface_adapter.trip_list.TripListController(
                        // 构造需要 LoadTripListInputBoundary，但我们不会用它
                        null
                );

        // 给 controller 注入 deleteTrip 用到的 interactor
        controller.setDeleteTripUseCaseInteractor(mockInteractor);

        // 调用 delete
        controller.deleteTrip("a", "T001");

        // 验证 interactor 是否被调用
        assertTrue(called[0], "Interactor should be called by controller");
    }
}