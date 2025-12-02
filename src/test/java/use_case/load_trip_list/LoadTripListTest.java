package use_case.load_trip_list;

import entity.Trip;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for LoadTripListInteractor.
 */
class LoadTripListTest {

    private LoadTripListInteractor interactor;
    private MockUserDataAccess mockUserDataAccess;
    private MockPresenter mockPresenter;

    @BeforeEach
    void setUp() {
        mockUserDataAccess = new MockUserDataAccess();
        mockPresenter = new MockPresenter();

        interactor = new LoadTripListInteractor(
                mockUserDataAccess,
                mockPresenter
        );
    }

    /**
     * Tests that execute() successfully loads trips and calls prepareSuccessView.
     * Covers the main execution path.
     */
    @Test
    void testExecuteSuccess() {
        // Create test trips
        List<Trip> testTrips = new ArrayList<>();
        testTrips.add(new Trip(
                "trip1",
                "Trip to Paris",
                "testuser",
                "COMPLETED",
                "2025-01-01 to 2025-01-05",
                "Paris",
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>()
        ));
        testTrips.add(new Trip(
                "trip2",
                "Trip to Tokyo",
                "testuser",
                "COMPLETED",
                "2025-02-01 to 2025-02-10",
                "Tokyo",
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>()
        ));

        // Set up mock to return test trips
        mockUserDataAccess.tripsToReturn = testTrips;

        LoadTripListInputData inputData = new LoadTripListInputData("testuser");

        // Execute
        interactor.execute(inputData);

        // Verify getTrips was called with correct parameters
        assertTrue(mockUserDataAccess.getTripsCalled, "getTrips should be called");
        assertEquals("testuser", mockUserDataAccess.lastUsername, "Username should match");
        assertEquals("COMPLETED", mockUserDataAccess.lastStatus, "Status should be COMPLETED");

        // Verify presenter was called with correct output data
        assertTrue(mockPresenter.prepareSuccessViewCalled, "prepareSuccessView should be called");
        assertNotNull(mockPresenter.outputData, "Output data should not be null");
        assertEquals("testuser", mockPresenter.outputData.getUsername(), "Username in output should match");
        assertEquals(2, mockPresenter.outputData.getTrips().size(), "Should have 2 trips");
        assertEquals("Trip to Paris", mockPresenter.outputData.getTrips().get(0).getTripName());
        assertEquals("Trip to Tokyo", mockPresenter.outputData.getTrips().get(1).getTripName());
    }

    /**
     * Tests that execute() handles empty trip list correctly.
     */
    @Test
    void testExecuteWithEmptyTripList() {
        // Set up mock to return empty list
        mockUserDataAccess.tripsToReturn = new ArrayList<>();

        LoadTripListInputData inputData = new LoadTripListInputData("testuser");

        // Execute
        interactor.execute(inputData);

        // Verify getTrips was called
        assertTrue(mockUserDataAccess.getTripsCalled);
        assertEquals("testuser", mockUserDataAccess.lastUsername);
        assertEquals("COMPLETED", mockUserDataAccess.lastStatus);

        // Verify presenter was called with empty list
        assertTrue(mockPresenter.prepareSuccessViewCalled);
        assertNotNull(mockPresenter.outputData);
        assertEquals("testuser", mockPresenter.outputData.getUsername());
        assertEquals(0, mockPresenter.outputData.getTrips().size(), "Should have 0 trips");
    }

    /**
     * Tests that goBack() calls prepareBackView on the presenter.
     */
    @Test
    void testGoBack() {
        // Execute goBack
        interactor.goBack();

        // Verify prepareBackView was called
        assertTrue(mockPresenter.prepareBackViewCalled, "prepareBackView should be called");
    }

    // Mock classes

    private static class MockUserDataAccess implements LoadTripListUserDataAccessInterface {
        List<Trip> tripsToReturn = new ArrayList<>();
        boolean getTripsCalled = false;
        String lastUsername;
        String lastStatus;

        @Override
        public List<Trip> getTrips(String username) {
            getTripsCalled = true;
            lastUsername = username;
            return tripsToReturn;
        }

        @Override
        public List<Trip> getTrips(String username, String status) {
            getTripsCalled = true;
            lastUsername = username;
            lastStatus = status;
            return tripsToReturn;
        }
    }

    private static class MockPresenter implements LoadTripListOutputBoundary {
        boolean prepareSuccessViewCalled = false;
        boolean prepareBackViewCalled = false;
        LoadTripListOutputData outputData;

        @Override
        public void prepareSuccessView(LoadTripListOutputData outputData) {
            prepareSuccessViewCalled = true;
            this.outputData = outputData;
        }

        @Override
        public void prepareFailView(String errorMessage) {
            fail("prepareFailView should not be called in these tests");
        }

        @Override
        public void prepareBackView() {
            prepareBackViewCalled = true;
        }
    }
}
