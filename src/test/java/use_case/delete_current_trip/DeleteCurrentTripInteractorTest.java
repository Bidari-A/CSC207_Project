package use_case.delete_current_trip;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import entity.Accommodation;
import entity.Destination;
import entity.Flight;
import entity.Trip;
import entity.User;
import entity.UserFactory;

/**
 * Test class for DeleteCurrentTripInteractor.
 */
class DeleteCurrentTripInteractorTest {

    private DeleteCurrentTripInteractor interactor;
    private MockDeleteCurrentTripDataAccessInterface mockDataAccess;
    private MockDeleteCurrentTripOutputBoundary mockPresenter;
    private UserFactory userFactory;

    @BeforeEach
    void setUp() {
        mockDataAccess = new MockDeleteCurrentTripDataAccessInterface();
        mockPresenter = new MockDeleteCurrentTripOutputBoundary();
        userFactory = new UserFactory();
        interactor = new DeleteCurrentTripInteractor(mockDataAccess, mockPresenter, userFactory);
    }

    @Test
    void testSuccessDeleteCurrentTrip() {
        // Setup: Create a user with a current trip
        String username = "testuser";
        String tripId = "T001";
        List<String> tripList = new ArrayList<>();
        tripList.add(tripId);
        User user = userFactory.create(username, "password", tripId, tripList);
        mockDataAccess.saveUser(user);

        // Create a trip with CURRENT status
        Trip trip = createTestTrip(tripId, username, "CURRENT");
        mockDataAccess.saveTrip(trip);

        // Execute
        DeleteCurrentTripInputData inputData = new DeleteCurrentTripInputData(username);
        interactor.execute(inputData);

        // Verify success was called
        assertTrue(mockPresenter.successCalled, "Success view should be prepared");
        assertFalse(mockPresenter.failCalled, "Fail view should not be prepared");
        assertEquals(username, mockPresenter.successUsername, "Username should match");

        // Verify trip was deleted
        assertTrue(mockDataAccess.isTripDeleted(tripId), "Trip should be deleted");
        
        // Verify user's currentTripId is cleared
        User updatedUser = mockDataAccess.getUser(username);
        assertNull(updatedUser.getCurrentTripId(), "Current trip ID should be null");
        
        // Verify tripId is removed from tripList
        assertFalse(updatedUser.getTripList().contains(tripId), "Trip ID should be removed from trip list");
    }

    @Test
    void testFailureUserNotFound() {
        // Execute with non-existent user
        DeleteCurrentTripInputData inputData = new DeleteCurrentTripInputData("nonexistent");
        interactor.execute(inputData);

        // Verify fail was called with correct message
        assertTrue(mockPresenter.failCalled, "Fail view should be prepared");
        assertFalse(mockPresenter.successCalled, "Success view should not be prepared");
        assertEquals("User not found.", mockPresenter.failMessage, "Error message should match");
    }

    @Test
    void testFailureNoCurrentTripNull() {
        // Setup: Create a user without a current trip (null)
        String username = "testuser";
        User user = userFactory.create(username, "password", null, new ArrayList<>());
        mockDataAccess.saveUser(user);

        // Execute
        DeleteCurrentTripInputData inputData = new DeleteCurrentTripInputData(username);
        interactor.execute(inputData);

        // Verify fail was called with correct message
        assertTrue(mockPresenter.failCalled, "Fail view should be prepared");
        assertFalse(mockPresenter.successCalled, "Success view should not be prepared");
        assertEquals("There is no current trip, unable to delete, please create a new trip first", 
                     mockPresenter.failMessage, "Error message should match");
    }

    @Test
    void testFailureNoCurrentTripEmptyString() {
        // Setup: Create a user with empty string currentTripId
        String username = "testuser";
        // Create user with empty string currentTripId directly (User constructor accepts it)
        User user = new User(username, "password", "", new ArrayList<>());
        mockDataAccess.saveUser(user);

        // Execute
        DeleteCurrentTripInputData inputData = new DeleteCurrentTripInputData(username);
        interactor.execute(inputData);

        // Verify fail was called with correct message
        assertTrue(mockPresenter.failCalled, "Fail view should be prepared");
        assertFalse(mockPresenter.successCalled, "Success view should not be prepared");
        assertEquals("There is no current trip, unable to delete, please create a new trip first", 
                     mockPresenter.failMessage, "Error message should match");
    }

    @Test
    void testFailureCurrentTripNotFound() {
        // Setup: Create a user with a currentTripId that doesn't exist
        String username = "testuser";
        String tripId = "T999";
        List<String> tripList = new ArrayList<>();
        tripList.add(tripId);
        User user = userFactory.create(username, "password", tripId, tripList);
        mockDataAccess.saveUser(user);
        // Don't save the trip, so it doesn't exist

        // Execute
        DeleteCurrentTripInputData inputData = new DeleteCurrentTripInputData(username);
        interactor.execute(inputData);

        // Verify fail was called with correct message
        assertTrue(mockPresenter.failCalled, "Fail view should be prepared");
        assertFalse(mockPresenter.successCalled, "Success view should not be prepared");
        assertEquals("Current trip not found.", mockPresenter.failMessage, "Error message should match");
    }

    @Test
    void testFailureTripStatusNotCurrent() {
        // Setup: Create a user with a trip that has status "COMPLETED" instead of "CURRENT"
        String username = "testuser";
        String tripId = "T001";
        List<String> tripList = new ArrayList<>();
        tripList.add(tripId);
        User user = userFactory.create(username, "password", tripId, tripList);
        mockDataAccess.saveUser(user);

        // Create a trip with COMPLETED status
        Trip trip = createTestTrip(tripId, username, "COMPLETED");
        mockDataAccess.saveTrip(trip);

        // Execute
        DeleteCurrentTripInputData inputData = new DeleteCurrentTripInputData(username);
        interactor.execute(inputData);

        // Verify fail was called with correct message
        assertTrue(mockPresenter.failCalled, "Fail view should be prepared");
        assertFalse(mockPresenter.successCalled, "Success view should not be prepared");
        assertEquals("The trip is not a current trip.", mockPresenter.failMessage, "Error message should match");
    }

    @Test
    void testFailureTripStatusOther() {
        // Setup: Test with a different status (not COMPLETED, not CURRENT)
        String username = "testuser";
        String tripId = "T001";
        List<String> tripList = new ArrayList<>();
        tripList.add(tripId);
        User user = userFactory.create(username, "password", tripId, tripList);
        mockDataAccess.saveUser(user);

        // Create a trip with a different status (e.g., "PLANNED" or any other status)
        Trip trip = createTestTrip(tripId, username, "PLANNED");
        mockDataAccess.saveTrip(trip);

        // Execute
        DeleteCurrentTripInputData inputData = new DeleteCurrentTripInputData(username);
        interactor.execute(inputData);

        // Verify fail was called with correct message
        assertTrue(mockPresenter.failCalled, "Fail view should be prepared");
        assertFalse(mockPresenter.successCalled, "Success view should not be prepared");
        assertEquals("The trip is not a current trip.", mockPresenter.failMessage, "Error message should match");
    }

    @Test
    void testFailureDeleteTripFailed() {
        // Setup: Create a user with a current trip
        String username = "testuser";
        String tripId = "T001";
        List<String> tripList = new ArrayList<>();
        tripList.add(tripId);
        User user = userFactory.create(username, "password", tripId, tripList);
        mockDataAccess.saveUser(user);

        // Create a trip with CURRENT status
        Trip trip = createTestTrip(tripId, username, "CURRENT");
        mockDataAccess.saveTrip(trip);

        // Make deleteTrip return false (simulate failure)
        mockDataAccess.setDeleteTripShouldFail(true);

        // Execute
        DeleteCurrentTripInputData inputData = new DeleteCurrentTripInputData(username);
        interactor.execute(inputData);

        // Verify fail was called with correct message
        assertTrue(mockPresenter.failCalled, "Fail view should be prepared");
        assertFalse(mockPresenter.successCalled, "Success view should not be prepared");
        assertEquals("Failed to delete the trip.", mockPresenter.failMessage, "Error message should match");
    }

    @Test
    void testSuccessWithMultipleTripsInList() {
        // Setup: Create a user with multiple trips in tripList
        String username = "testuser";
        String currentTripId = "T001";
        String otherTripId = "T002";
        List<String> tripList = new ArrayList<>();
        tripList.add(currentTripId);
        tripList.add(otherTripId);
        User user = userFactory.create(username, "password", currentTripId, tripList);
        mockDataAccess.saveUser(user);

        // Create the current trip
        Trip currentTrip = createTestTrip(currentTripId, username, "CURRENT");
        mockDataAccess.saveTrip(currentTrip);
        
        // Create another trip (not current)
        Trip otherTrip = createTestTrip(otherTripId, username, "COMPLETED");
        mockDataAccess.saveTrip(otherTrip);

        // Execute
        DeleteCurrentTripInputData inputData = new DeleteCurrentTripInputData(username);
        interactor.execute(inputData);

        // Verify success
        assertTrue(mockPresenter.successCalled, "Success view should be prepared");
        assertFalse(mockPresenter.failCalled, "Fail view should not be prepared");
        
        // Verify trip was deleted from trips
        assertTrue(mockDataAccess.isTripDeleted(currentTripId), "Current trip should be deleted");
        assertFalse(mockDataAccess.isTripDeleted(otherTripId), "Other trip should not be deleted");
        
        // Verify only the current trip was removed from tripList, other trip remains
        User updatedUser = mockDataAccess.getUser(username);
        assertNull(updatedUser.getCurrentTripId(), "Current trip ID should be null");
        assertFalse(updatedUser.getTripList().contains(currentTripId), "Current trip ID should be removed from trip list");
        assertTrue(updatedUser.getTripList().contains(otherTripId), "Other trip ID should remain in trip list");
    }

    @Test
    void testSuccessWhenCurrentTripNotInTripList() {
        // Setup: Edge case - currentTripId exists but is not in tripList
        String username = "testuser";
        String currentTripId = "T001";
        String otherTripId = "T002";
        List<String> tripList = new ArrayList<>();
        tripList.add(otherTripId);  // tripList doesn't include currentTripId
        User user = userFactory.create(username, "password", currentTripId, tripList);
        mockDataAccess.saveUser(user);

        // Create the current trip
        Trip trip = createTestTrip(currentTripId, username, "CURRENT");
        mockDataAccess.saveTrip(trip);

        // Execute
        DeleteCurrentTripInputData inputData = new DeleteCurrentTripInputData(username);
        interactor.execute(inputData);

        // Verify success
        assertTrue(mockPresenter.successCalled, "Success view should be prepared");
        
        // Verify trip was deleted
        assertTrue(mockDataAccess.isTripDeleted(currentTripId), "Trip should be deleted");
        
        // Verify user's currentTripId is cleared
        User updatedUser = mockDataAccess.getUser(username);
        assertNull(updatedUser.getCurrentTripId(), "Current trip ID should be null");
        
        // Verify tripList is unchanged (since currentTripId wasn't in it)
        assertEquals(1, updatedUser.getTripList().size(), "Trip list should still have one trip");
        assertTrue(updatedUser.getTripList().contains(otherTripId), "Other trip ID should remain");
    }

    @Test
    void testSuccessWithEmptyTripList() {
        // Setup: User has currentTripId but empty tripList
        String username = "testuser";
        String tripId = "T001";
        List<String> tripList = new ArrayList<>();  // Empty tripList
        User user = userFactory.create(username, "password", tripId, tripList);
        mockDataAccess.saveUser(user);

        // Create a trip with CURRENT status
        Trip trip = createTestTrip(tripId, username, "CURRENT");
        mockDataAccess.saveTrip(trip);

        // Execute
        DeleteCurrentTripInputData inputData = new DeleteCurrentTripInputData(username);
        interactor.execute(inputData);

        // Verify success
        assertTrue(mockPresenter.successCalled, "Success view should be prepared");
        assertFalse(mockPresenter.failCalled, "Fail view should not be prepared");
        
        // Verify trip was deleted
        assertTrue(mockDataAccess.isTripDeleted(tripId), "Trip should be deleted");
        
        // Verify user's currentTripId is cleared
        User updatedUser = mockDataAccess.getUser(username);
        assertNull(updatedUser.getCurrentTripId(), "Current trip ID should be null");
        
        // Verify tripList remains empty
        assertTrue(updatedUser.getTripList().isEmpty(), "Trip list should remain empty");
    }

    @Test
    void testSuccessVerifyOutputData() {
        // Setup: Create a user with a current trip
        String username = "testuser";
        String tripId = "T001";
        List<String> tripList = new ArrayList<>();
        tripList.add(tripId);
        User user = userFactory.create(username, "password", tripId, tripList);
        mockDataAccess.saveUser(user);

        // Create a trip with CURRENT status
        Trip trip = createTestTrip(tripId, username, "CURRENT");
        mockDataAccess.saveTrip(trip);

        // Execute
        DeleteCurrentTripInputData inputData = new DeleteCurrentTripInputData(username);
        interactor.execute(inputData);

        // Verify success was called with correct username
        assertTrue(mockPresenter.successCalled, "Success view should be prepared");
        assertEquals(username, mockPresenter.successUsername, "Output data should contain correct username");
        
        // Verify all state changes
        assertTrue(mockDataAccess.isTripDeleted(tripId), "Trip should be deleted");
        User updatedUser = mockDataAccess.getUser(username);
        assertNull(updatedUser.getCurrentTripId(), "Current trip ID should be null");
        assertFalse(updatedUser.getTripList().contains(tripId), "Trip ID should be removed from trip list");
    }

    // Helper method to create a test trip
    private Trip createTestTrip(String tripId, String ownerUsername, String status) {
        List<Accommodation> hotels = new ArrayList<>();
        hotels.add(new Accommodation("Test Hotel", "Test Address", 100.0f));
        
        List<Flight> flights = new ArrayList<>();
        flights.add(new Flight("Test Airline", "2025-01-01", 500.0f));
        
        List<Destination> attractions = new ArrayList<>();
        attractions.add(new Destination("Test Attraction"));
        
        return new Trip(tripId, "Test Trip", ownerUsername, status, 
                       "2025-01-01 to 2025-01-05", "Test Destination", 
                       hotels, flights, attractions);
    }

    // Mock Data Access Interface
    private static class MockDeleteCurrentTripDataAccessInterface implements DeleteCurrentTripDataAccessInterface {
        private final Map<String, User> users = new HashMap<>();
        private final Map<String, Trip> trips = new HashMap<>();
        private boolean deleteTripShouldFail = false;

        @Override
        public User getUser(String username) {
            return users.get(username);
        }

        @Override
        public Trip getTrip(String tripId) {
            return trips.get(tripId);
        }

        @Override
        public boolean deleteTrip(String tripId) {
            if (deleteTripShouldFail) {
                return false;
            }
            return trips.remove(tripId) != null;
        }

        @Override
        public void saveUser(User user) {
            users.put(user.getUsername(), user);
        }

        @Override
        public String getCurrentUsername() {
            return null;
        }

        public void saveTrip(Trip trip) {
            trips.put(trip.getTripId(), trip);
        }

        public boolean isTripDeleted(String tripId) {
            return !trips.containsKey(tripId);
        }

        public void setDeleteTripShouldFail(boolean shouldFail) {
            this.deleteTripShouldFail = shouldFail;
        }
    }

    // Mock Output Boundary
    private static class MockDeleteCurrentTripOutputBoundary implements DeleteCurrentTripOutputBoundary {
        boolean successCalled = false;
        boolean failCalled = false;
        String successUsername = null;
        String failMessage = null;

        @Override
        public void prepareSuccessView(DeleteCurrentTripOutputData outputData) {
            successCalled = true;
            successUsername = outputData.getUsername();
        }

        @Override
        public void prepareFailView(String error) {
            failCalled = true;
            failMessage = error;
        }
    }
}

