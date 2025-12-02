package use_case.complete_trip;

import entity.Trip;
import entity.User;
import entity.UserFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class CompleteTripInteractorTest {

    private MockUserDAO userDAO;
    private MockTripDAO tripDAO;
    private MockPresenter presenter;
    private CompleteTripInteractor interactor;

    @BeforeEach
    void setup() {
        userDAO = new MockUserDAO();
        tripDAO = new MockTripDAO();
        presenter = new MockPresenter();

        interactor = new CompleteTripInteractor(
                userDAO,
                tripDAO,
                presenter
        );
    }

    // ======================================================
    // SUCCESS CASE
    // ======================================================
    @Test
    void testCompleteTripSuccess() {

        // Pretend user has active trip "T123"
        userDAO.currentTripId = "T123";

        Trip trip = new Trip(
                "T123",
                "Hawaii Trip",
                "2025-01-01",
                "2025-01-10",
                "Honolulu",
                "Ongoing",
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>()
        );

        tripDAO.trip = trip;

        interactor.execute(new CompleteTripInputData("alice"));

        // Presenter success called
        assertTrue(presenter.successCalled);
        assertFalse(presenter.failCalled);

        // Correct output data
        assertEquals("T123", presenter.outputData.getTripId());
        assertEquals("Hawaii Trip", presenter.outputData.getTripName());

        // Trip status updated
        assertEquals("COMPLETED", tripDAO.trip.getStatus());

        // User current trip cleared
        assertNull(userDAO.currentTripId);

        // Trip added to "history"
        assertEquals("T123", userDAO.historyAddedTripId);

        // Save methods invoked
        assertTrue(userDAO.saveCalled);
        assertTrue(tripDAO.saveCalled);
    }

    // ======================================================
    // FAILURE CASE — NO CURRENT TRIP
    // ======================================================
    @Test
    void testCompleteTrip_NoCurrentTrip() {

        userDAO.currentTripId = null;

        interactor.execute(new CompleteTripInputData("alice"));

        assertTrue(presenter.failCalled);
        assertFalse(presenter.successCalled);

        assertEquals("No active trip to complete.", presenter.errorMessage);
    }


    // ======================================================
    // MOCK DAOs — DO NOT OVERRIDE NON-EXISTING METHODS
    // ======================================================

    private static class MockUserDAO extends data_access.FileUserDataAccessObject {

        public String currentTripId;
        public String historyAddedTripId;
        public boolean saveCalled = false;

        public MockUserDAO() {
            super("mock.json", new UserFactory());
        }

        // REAL method in your FileUserDataAccessObject
        @Override
        public String getCurrentTripId(String username) {
            return currentTripId;
        }

        // NOT in real DAO — do NOT use @Override
        public void clearCurrentTrip(String username) {
            currentTripId = null;
        }

        // NOT in real DAO — do NOT use @Override
        public void addTripToHistory(String username, String tripId) {
            historyAddedTripId = tripId;
        }

        // REAL method
        @Override
        public void save(User user) {
            saveCalled = true;
        }
    }


    private static class MockTripDAO extends data_access.FileTripDataAccessObject {

        public Trip trip;
        public boolean saveCalled = false;

        public MockTripDAO() {
            super("mock.json");
        }

        @Override
        public Trip get(String id) {
            return trip;
        }

        @Override
        public Trip saveTrip(Trip t) {
            this.trip = t;
            saveCalled = true;
            return t;
        }

        public void save() {
            saveCalled = true;
        }
    }

    private static class MockPresenter implements CompleteTripOutputBoundary {

        public boolean successCalled = false;
        public boolean failCalled = false;

        public CompleteTripOutputData outputData;
        public String errorMessage;

        @Override
        public void prepareSuccessView(CompleteTripOutputData outputData) {
            successCalled = true;
            this.outputData = outputData;
        }

        @Override
        public void prepareFailView(String errorMessage) {
            failCalled = true;
            this.errorMessage = errorMessage;
        }
    }
}