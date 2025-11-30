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

    @Test
    void testPresenterSuccessUpdatesViewModel() {

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

        DeleteTripOutputData outputData =
                new DeleteTripOutputData(updatedTrips, "a");

        presenter.prepareSuccessView(outputData);

        TripListState state = tripListViewModel.getState();

        assertNotNull(state);
        assertEquals("a", state.getUsername());
        assertEquals(updatedTrips, state.getTrips());
        assertNull(state.getError());

        assertEquals("trip list", viewManagerModel.getState());
    }


    @Test
    void testPresenterFailUpdatesViewModel() {

        presenter.prepareFailView("Delete failed!");

        TripListState state = tripListViewModel.getState();

        assertNotNull(state);
        assertEquals("Delete failed!", state.getError());
    }


    @Test
    void testControllerCallsDeleteInteractor() {

        final boolean[] called = {false};

        // Mock interactor
        DeleteTripInputBoundary mockInteractor = new DeleteTripInputBoundary() {
            @Override
            public void delete(DeleteTripInputData data) {
                called[0] = true;

                assertEquals("a", data.getUsername());
                assertEquals("T001", data.getTripId());
            }
        };

        interface_adapter.trip_list.TripListController controller =
                new interface_adapter.trip_list.TripListController(
                        null
                );

        controller.setDeleteTripUseCaseInteractor(mockInteractor);

        controller.deleteTrip("a", "T001");

        assertTrue(called[0], "Interactor should be called by controller");
    }
}