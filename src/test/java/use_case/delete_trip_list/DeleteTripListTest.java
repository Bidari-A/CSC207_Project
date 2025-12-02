package use_case.delete_trip_list;

import entity.Trip;
import entity.User;
import interface_adapter.ViewManagerModel;
import interface_adapter.trip_list.TripListPresenter;
import interface_adapter.trip_list.TripListState;
import interface_adapter.trip_list.TripListViewModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import use_case.delete_current_trip.DeleteCurrentTripDataAccessInterface;

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

    // ======================= Presenter Tests =======================

    @Test
    void testPresenterSuccessUpdatesViewModel() {
        List<Trip> updatedTrips = new ArrayList<>();
        updatedTrips.add(new Trip(
                "T100", "TripToTokyo", "a", "CURRENT",
                "2025", "Tokyo",
                new ArrayList<>(), new ArrayList<>(), new ArrayList<>()
        ));

        DeleteTripOutputData outputData =
                new DeleteTripOutputData(updatedTrips, "a");

        presenter.prepareSuccessView(outputData);

        TripListState state = tripListViewModel.getState();
        assertEquals("a", state.getUsername());
        assertEquals(updatedTrips, state.getTrips());
        assertNull(state.getError());
        assertEquals("trip list", viewManagerModel.getState());
    }

    @Test
    void testPresenterFailUpdatesViewModel() {
        presenter.prepareFailView("Delete failed!");
        TripListState state = tripListViewModel.getState();
        assertEquals("Delete failed!", state.getError());
    }

    // ======================= Controller Tests =======================

    @Test
    void testControllerCallsDeleteInteractor() {
        final boolean[] called = {false};

        DeleteTripInputBoundary mockInteractor = new DeleteTripInputBoundary() {
            @Override
            public void delete(DeleteTripInputData data) {
                called[0] = true;
                assertEquals("a", data.getUsername());
                assertEquals("T001", data.getTripId());
            }
        };

        interface_adapter.trip_list.TripListController controller =
                new interface_adapter.trip_list.TripListController(null);
        controller.setDeleteTripUseCaseInteractor(mockInteractor);

        controller.deleteTrip("a", "T001");

        assertTrue(called[0]);
    }

    // ======================= Interactor Tests (100% Lines + Branches) =======================

    @Test
    void testInteractorDeleteSuccessNotCurrentTrip() {

        DeleteTripUserDataAccessInterface tripDAO = new DeleteTripUserDataAccessInterface() {
            @Override
            public boolean deleteTrip(String username, String tripName) {
                return true;
            }

            @Override
            public List<Trip> getTrips(String username) {
                List<Trip> list = new ArrayList<>();
                list.add(new Trip("t1", "PastTrip", "a", "COMPLETED",
                        "2024", "Tokyo",
                        new ArrayList<>(), new ArrayList<>(), new ArrayList<>()));
                return list;
            }
        };

        DeleteCurrentTripDataAccessInterface userDAO = new DeleteCurrentTripDataAccessInterface() {

            private User user = new User("a", "pass", "currentTrip", new ArrayList<>());

            @Override
            public User getUser(String username) { return user; }

            @Override public Trip getTrip(String tripId) { return null; }
            @Override public boolean deleteTrip(String tripId) { return false; }

            @Override
            public void saveUser(User user) { this.user = user; }

            @Override
            public String getCurrentUsername() { return "a"; }
        };

        final boolean[] successCalled = {false};

        DeleteTripOutputBoundary presenter = new DeleteTripOutputBoundary() {
            @Override
            public void prepareSuccessView(DeleteTripOutputData data) {
                successCalled[0] = true;
                assertEquals("a", data.getUsername());
                assertEquals(1, data.getUpdatedTrips().size());
            }

            @Override
            public void prepareFailView(String error) {
                fail("Should not fail");
            }
        };

        DeleteTripInteractor interactor =
                new DeleteTripInteractor(tripDAO, userDAO, presenter);

        interactor.delete(new DeleteTripInputData("a", "tripX"));

        assertTrue(successCalled[0]);
        assertEquals("currentTrip", userDAO.getUser("a").getCurrentTripId());
    }


    @Test
    void testInteractorDeleteSuccessCurrentTrip() {

        DeleteTripUserDataAccessInterface tripDAO = new DeleteTripUserDataAccessInterface() {
            @Override
            public boolean deleteTrip(String username, String tripName) { return true; }

            @Override
            public List<Trip> getTrips(String username) {
                List<Trip> list = new ArrayList<>();
                list.add(new Trip("t2", "PastTrip2", "a", "COMPLETED",
                        "2024", "Seoul",
                        new ArrayList<>(), new ArrayList<>(), new ArrayList<>()));
                return list;
            }
        };

        DeleteCurrentTripDataAccessInterface userDAO = new DeleteCurrentTripDataAccessInterface() {

            private User user = new User("a", "pass", "tripX", new ArrayList<>());

            @Override public User getUser(String username) { return user; }

            @Override public Trip getTrip(String tripId) { return null; }
            @Override public boolean deleteTrip(String tripId) { return false; }

            @Override public void saveUser(User user) { this.user = user; }

            @Override public String getCurrentUsername() { return "a"; }
        };

        final boolean[] successCalled = {false};

        DeleteTripOutputBoundary presenter = new DeleteTripOutputBoundary() {
            @Override public void prepareSuccessView(DeleteTripOutputData data) {
                successCalled[0] = true;
            }

            @Override public void prepareFailView(String error) {}
        };

        DeleteTripInteractor interactor =
                new DeleteTripInteractor(tripDAO, userDAO, presenter);

        interactor.delete(new DeleteTripInputData("a", "tripX"));

        assertTrue(successCalled[0]);
        assertNull(userDAO.getUser("a").getCurrentTripId());
    }

    @Test
    void testInteractorDeleteFails() {

        DeleteTripUserDataAccessInterface tripDAO = new DeleteTripUserDataAccessInterface() {
            @Override public boolean deleteTrip(String username, String tripName) {
                return false;
            }

            @Override public List<Trip> getTrips(String username) {
                return new ArrayList<>();
            }
        };

        DeleteCurrentTripDataAccessInterface userDAO = new DeleteCurrentTripDataAccessInterface() {
            @Override public User getUser(String username) {
                return new User("a", "pass", "trip1", new ArrayList<>());
            }

            @Override public Trip getTrip(String tripId) { return null; }
            @Override public boolean deleteTrip(String tripId) { return false; }
            @Override public void saveUser(User user) {}
            @Override public String getCurrentUsername() { return "a"; }
        };

        final boolean[] failCalled = {false};

        DeleteTripOutputBoundary presenter = new DeleteTripOutputBoundary() {
            @Override public void prepareSuccessView(DeleteTripOutputData data) {
                fail("Should not succeed");
            }

            @Override public void prepareFailView(String error) {
                failCalled[0] = true;
                assertEquals("Failed to delete trip.", error);
            }
        };

        DeleteTripInteractor interactor =
                new DeleteTripInteractor(tripDAO, userDAO, presenter);

        interactor.delete(new DeleteTripInputData("a", "trip1"));

        assertTrue(failCalled[0]);
    }

    // ====== EXTRA TEST to achieve 100% branch coverage ======

    @Test
    void testInteractorBranchCoverage() {

        DeleteTripUserDataAccessInterface tripDAO = new DeleteTripUserDataAccessInterface() {
            @Override public boolean deleteTrip(String username, String tripName) { return true; }

            @Override public List<Trip> getTrips(String username) {
                List<Trip> list = new ArrayList<>();
                list.add(null); // branch: trip == null
                list.add(new Trip("xx", "NonCompleted", "a", "PLANNED",
                        "2024", "NYC",
                        new ArrayList<>(), new ArrayList<>(), new ArrayList<>()));
                return list;
            }
        };

        DeleteCurrentTripDataAccessInterface userDAO = new DeleteCurrentTripDataAccessInterface() {
            private User user = new User("a", "pass", null, new ArrayList<>()); // branch: currentTripId == null

            @Override public User getUser(String username) { return user; }

            @Override public Trip getTrip(String tripId) { return null; }
            @Override public boolean deleteTrip(String tripId) { return false; }

            @Override public void saveUser(User user) { this.user = user; }

            @Override public String getCurrentUsername() { return "a"; }
        };

        final boolean[] successCalled = {false};

        DeleteTripOutputBoundary presenter = new DeleteTripOutputBoundary() {
            @Override public void prepareSuccessView(DeleteTripOutputData data) {
                successCalled[0] = true;
                assertEquals(0, data.getUpdatedTrips().size()); // non-COMPLETED ignored
            }

            @Override public void prepareFailView(String error) { fail("Should not fail"); }
        };

        DeleteTripInteractor interactor =
                new DeleteTripInteractor(tripDAO, userDAO, presenter);

        interactor.delete(new DeleteTripInputData("a", "trip123"));

        assertTrue(successCalled[0]);
    }
}