package use_case;

import interface_adapter.ViewManagerModel;
import interface_adapter.trip_list.TripListPresenter;
import interface_adapter.trip_list.TripListState;
import interface_adapter.trip_list.TripListViewModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import use_case.delete_trip_list.DeleteTripOutputData;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class DeleteTripListTest {

    private TripListViewModel tripListViewModel;
    private ViewManagerModel viewManagerModel;
    private TripListPresenter presenter;

    @BeforeEach
    void setup() {
        tripListViewModel = new TripListViewModel();

        // ⭐ Mock ViewManagerModel（避免 NPE）
        viewManagerModel = mock(ViewManagerModel.class);
        doNothing().when(viewManagerModel).setState(any());
        doNothing().when(viewManagerModel).firePropertyChange();

        presenter = new TripListPresenter(viewManagerModel, tripListViewModel);
    }

    /**
     * Test: presenter updates ViewModel on success
     */
    @Test
    void testPresenterSuccessUpdatesViewModel() {

        List<entity.Trip> trips = new ArrayList<>();
        DeleteTripOutputData data = new DeleteTripOutputData(trips, "T002");

        presenter.prepareSuccessView(data);

        TripListState state = tripListViewModel.getState();

        assertEquals(trips, state.getTrips());
        assertEquals("T002", state.getUsername());
        assertNull(state.getError());

        verify(viewManagerModel, times(1)).setState("trip list");
        verify(viewManagerModel, times(1)).firePropertyChange();
    }

    /**
     * Test: presenter fail case sets error message
     */
    @Test
    void testPresenterFailUpdate() {
        presenter.prepareFailView("Failed");

        TripListState state = tripListViewModel.getState();
        assertEquals("Failed", state.getError());
    }
}