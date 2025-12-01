package use_case.hotel_search;

import data_access.FileTripDataAccessObject;
import data_access.FileUserDataAccessObject;
import entity.Accommodation;
import entity.Destination;
import entity.Trip;
import entity.User;
import entity.UserFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * ============================================================
 * HotelSearchInteractorTest
 * ============================================================
 *
 * This test class verifies the correctness of HotelSearchInteractor including:
 *
 *   - Gateway interaction
 *   - Presenter callbacks
 *   - Database writes
 *   - Guard clause behavior (accommodation/user/trip null checks)
 *
 * ------------------------------------------------------------
 * TEST CASE SUMMARY
 * ------------------------------------------------------------
 *
 * 1) testExecute_success_replacesHotelAndCallsPresenter
 *
 *      PURPOSE:
 *      Verifies the main success case.
 *
 *      SETUP:
 *        - Existing trip contains one hotel ("Old Hotel")
 *
 *      INPUT:
 *        query    = "Bali hotel"
 *        checkIn  = "2025-01-01"
 *        checkOut = "2025-01-10"
 *
 *      FAKE GATEWAY RETURNS:
 *        Summary = "Test hotel summary"
 *        Entity  = Accommodation("New Test Hotel", "", 200.0)
 *
 *      EXPECTED OUTPUT:
 *        - presenter.prepareSuccessView(...) called
 *        - Previous hotel removed
 *        - New hotel inserted into trip (hotels list size = 1)
 *
 * ------------------------------------------------------------
 *
 * 2) testExecute_gatewayThrows_callsFailView
 *
 *      PURPOSE:
 *      Verifies graceful failure handling when gateway throws.
 *
 *      FAKE GATEWAY:
 *        fetchRawJson throws RuntimeException("API error for hotels")
 *
 *      EXPECTED OUTPUT:
 *        - prepareFailView("API error for hotels") called
 *        - prepareSuccessView(...) not called
 *
 * ------------------------------------------------------------
 *
 * 3) testExecute_noCurrentUser_doesNotSaveButStillCallsSuccess
 *
 *      PURPOSE:
 *      Covers: username == null
 *
 *      SETUP:
 *        userDao.setCurrentUsername(null)
 *
 *      EXPECTED OUTPUT:
 *        - saveBestHotelToCurrentTrip returns early
 *        - old hotel remains in DB
 *
 * ------------------------------------------------------------
 *
 * 4) testExecute_bestHotelNull_doesNotSaveButStillCallsSuccess
 *
 *      PURPOSE:
 *      Covers: accommodation == null
 *
 *      FAKE GATEWAY:
 *        buildFirstHotelEntity(json) returns null
 *
 *      EXPECTED OUTPUT:
 *        - prepareSuccessView(...) called
 *        - Trip remains with "Old Hotel"
 *
 * ------------------------------------------------------------
 *
 * 5) testExecute_userIsNull_doesNotSaveButStillCallsSuccess
 *
 *      PURPOSE:
 *      Covers: userDao.getUser(username) == null
 *
 *      SETUP:
 *        userDao.setCurrentUsername("ghost")
 *
 * ------------------------------------------------------------
 *
 * 6) testExecute_userHasNoCurrentTripId_doesNotSaveButStillCallsSuccess
 *
 *      PURPOSE:
 *      Covers: user.getCurrentTripId() == null
 *
 *      SETUP:
 *        User("charlie", "pwd", null, trips)
 *
 * ------------------------------------------------------------
 *
 * 7) testExecute_tripNotFound_doesNotSaveButStillCallsSuccess
 *
 *      PURPOSE:
 *      Covers: trip == null (trip id does not exist)
 *
 *      SETUP:
 *        User("bob", "pwd", "missingTrip", trips)
 *
 * ============================================================
 */
class HotelSearchInteractorTest {

    private static final String TEST_USER_JSON = "test_hotel_users.json";
    private static final String TEST_TRIP_JSON = "test_hotel_trips.json";

    private FileUserDataAccessObject userDao;
    private FileTripDataAccessObject tripDao;
    private UserFactory userFactory;

    /**
     * Simple fake HotelSearchGateway that avoids real HTTP calls.
     */
    private static class FakeHotelSearchGateway implements HotelSearchGateway {
        boolean shouldThrow = false;

        @Override
        public String fetchRawJson(String query, String checkInDate, String checkOutDate) {
            if (shouldThrow) {
                throw new RuntimeException("API error for hotels");
            }
            return "{}"; // dummy JSON
        }

        @Override
        public String summarizeFirstHotel(String json) {
            return "Test hotel summary";
        }

        @Override
        public Accommodation buildFirstHotelEntity(String json) {
            return new Accommodation("New Test Hotel", "", 200.0f);
        }
    }

    @BeforeEach
    void setUp() {
        // Clean up any existing test files
        File uf = new File(TEST_USER_JSON);
        if (uf.exists()) uf.delete();
        File tf = new File(TEST_TRIP_JSON);
        if (tf.exists()) tf.delete();

        userFactory = new UserFactory();
        userDao = new FileUserDataAccessObject(TEST_USER_JSON, userFactory);
        tripDao = new FileTripDataAccessObject(TEST_TRIP_JSON);
        userDao.setTripDataAccessObject(tripDao);

        // Create user with current trip
        List<String> trips = new ArrayList<>();
        trips.add("trip1");
        User user = new User("alice", "pwd", "trip1", trips);
        userDao.save(user);
        userDao.setCurrentUsername("alice");

        // Create trip with one existing hotel ("Old Hotel")
        List<Accommodation> hotels = new ArrayList<>();
        hotels.add(new Accommodation("Old Hotel", "", 100.0f));

        Trip trip = new Trip(
                "trip1",
                "Trip to Bali",
                "alice",
                "PLANNED",
                "2025-01-01 to 2025-01-10",
                "Bali",
                hotels,
                new ArrayList<>(),               // flights
                new ArrayList<Destination>()      // attractions
        );
        tripDao.save();
    }

    @AfterEach
    void tearDown() {
        File uf = new File(TEST_USER_JSON);
        if (uf.exists()) uf.delete();
        File tf = new File(TEST_TRIP_JSON);
        if (tf.exists()) tf.delete();
    }

    @Test
    void testExecute_success_replacesHotelAndCallsPresenter() {
        FakeHotelSearchGateway gateway = new FakeHotelSearchGateway();

        final boolean[] successCalled = {false};
        final boolean[] failCalled = {false};

        HotelSearchOutputBoundary presenter = new HotelSearchOutputBoundary() {
            @Override
            public void prepareSuccessView(HotelSearchOutputData outputData) {
                successCalled[0] = true;
                assertEquals("Test hotel summary", outputData.getSummary());
            }

            @Override
            public void prepareFailView(String errorMessage) {
                failCalled[0] = true;
                fail("Failure view should not be called in success scenario.");
            }
        };

        HotelSearchInputBoundary interactor =
                new HotelSearchInteractor(gateway, presenter, userDao, tripDao);

        HotelSearchInputData inputData =
                new HotelSearchInputData("Bali hotel", "2025-01-01", "2025-01-10");

        interactor.execute(inputData);

        assertTrue(successCalled[0]);
        assertFalse(failCalled[0]);

        Trip trip = tripDao.get("trip1");
        assertNotNull(trip);
        assertEquals(1, trip.getHotels().size());
        Accommodation hotel = trip.getHotels().get(0);
        assertEquals("New Test Hotel", hotel.getName());
        assertEquals(200.0f, hotel.getPrice(), 0.001);
    }

    @Test
    void testExecute_gatewayThrows_callsFailView() {
        FakeHotelSearchGateway gateway = new FakeHotelSearchGateway();
        gateway.shouldThrow = true;

        final boolean[] successCalled = {false};
        final boolean[] failCalled = {false};
        final String[] errorMsg = {null};

        HotelSearchOutputBoundary presenter = new HotelSearchOutputBoundary() {
            @Override
            public void prepareSuccessView(HotelSearchOutputData outputData) {
                successCalled[0] = true;
            }

            @Override
            public void prepareFailView(String errorMessage) {
                failCalled[0] = true;
                errorMsg[0] = errorMessage;
            }
        };

        HotelSearchInputBoundary interactor =
                new HotelSearchInteractor(gateway, presenter, userDao, tripDao);

        HotelSearchInputData inputData =
                new HotelSearchInputData("Bali hotel", "2025-01-01", "2025-01-10");

        interactor.execute(inputData);

        assertFalse(successCalled[0]);
        assertTrue(failCalled[0]);
        assertNotNull(errorMsg[0]);
        assertTrue(errorMsg[0].contains("API error"));
    }

    @Test
    void testExecute_noCurrentUser_doesNotSaveButStillCallsSuccess() {
        // Remove current user
        userDao.setCurrentUsername(null);

        FakeHotelSearchGateway gateway = new FakeHotelSearchGateway();

        final boolean[] successCalled = {false};
        final boolean[] failCalled = {false};

        HotelSearchOutputBoundary presenter = new HotelSearchOutputBoundary() {
            @Override
            public void prepareSuccessView(HotelSearchOutputData outputData) {
                successCalled[0] = true;
            }

            @Override
            public void prepareFailView(String errorMessage) {
                failCalled[0] = true;
            }
        };

        HotelSearchInputBoundary interactor =
                new HotelSearchInteractor(gateway, presenter, userDao, tripDao);

        HotelSearchInputData inputData =
                new HotelSearchInputData("Bali hotel", "2025-01-01", "2025-01-10");

        interactor.execute(inputData);

        assertTrue(successCalled[0]);
        assertFalse(failCalled[0]);

        Trip trip = tripDao.get("trip1");
        assertNotNull(trip);
        assertEquals(1, trip.getHotels().size());
        assertEquals("Old Hotel", trip.getHotels().get(0).getName());
    }

    @Test
    void testExecute_bestHotelNull_doesNotSaveButStillCallsSuccess() {
        FakeHotelSearchGateway gateway = new FakeHotelSearchGateway() {
            @Override
            public Accommodation buildFirstHotelEntity(String json) {
                return null; // simulate no hotel found
            }
        };

        final boolean[] successCalled = {false};
        final boolean[] failCalled = {false};

        HotelSearchOutputBoundary presenter = new HotelSearchOutputBoundary() {
            @Override
            public void prepareSuccessView(HotelSearchOutputData outputData) {
                successCalled[0] = true;
            }

            @Override
            public void prepareFailView(String errorMessage) {
                failCalled[0] = true;
            }
        };

        HotelSearchInputBoundary interactor =
                new HotelSearchInteractor(gateway, presenter, userDao, tripDao);

        HotelSearchInputData inputData =
                new HotelSearchInputData("Bali hotel", "2025-01-01", "2025-01-10");

        interactor.execute(inputData);

        assertTrue(successCalled[0]);
        assertFalse(failCalled[0]);

        Trip trip = tripDao.get("trip1");
        assertNotNull(trip);
        assertEquals(1, trip.getHotels().size());
        assertEquals("Old Hotel", trip.getHotels().get(0).getName());
    }

    @Test
    void testExecute_userIsNull_doesNotSaveButStillCallsSuccess() {
        // current user is someone who doesn't exist
        userDao.setCurrentUsername("ghost");

        FakeHotelSearchGateway gateway = new FakeHotelSearchGateway();

        final boolean[] successCalled = {false};
        final boolean[] failCalled = {false};

        HotelSearchOutputBoundary presenter = new HotelSearchOutputBoundary() {
            @Override
            public void prepareSuccessView(HotelSearchOutputData outputData) {
                successCalled[0] = true;
            }

            @Override
            public void prepareFailView(String errorMessage) {
                failCalled[0] = true;
            }
        };

        HotelSearchInputBoundary interactor =
                new HotelSearchInteractor(gateway, presenter, userDao, tripDao);

        HotelSearchInputData inputData =
                new HotelSearchInputData("Bali hotel", "2025-01-01", "2025-01-10");

        interactor.execute(inputData);

        assertTrue(successCalled[0]);
        assertFalse(failCalled[0]);

        Trip trip = tripDao.get("trip1");
        assertNotNull(trip);
        assertEquals(1, trip.getHotels().size());
        assertEquals("Old Hotel", trip.getHotels().get(0).getName());
    }

    @Test
    void testExecute_userHasNoCurrentTripId_doesNotSaveButStillCallsSuccess() {
        // User with null currentTripId
        List<String> trips = new ArrayList<>();
        User userNoTrip = new User("charlie", "pwd", null, trips);
        userDao.save(userNoTrip);
        userDao.setCurrentUsername("charlie");

        FakeHotelSearchGateway gateway = new FakeHotelSearchGateway();

        final boolean[] successCalled = {false};
        final boolean[] failCalled = {false};

        HotelSearchOutputBoundary presenter = new HotelSearchOutputBoundary() {
            @Override
            public void prepareSuccessView(HotelSearchOutputData outputData) {
                successCalled[0] = true;
            }

            @Override
            public void prepareFailView(String errorMessage) {
                failCalled[0] = true;
            }
        };

        HotelSearchInputBoundary interactor =
                new HotelSearchInteractor(gateway, presenter, userDao, tripDao);

        HotelSearchInputData inputData =
                new HotelSearchInputData("Bali hotel", "2025-01-01", "2025-01-10");

        interactor.execute(inputData);

        assertTrue(successCalled[0]);
        assertFalse(failCalled[0]);

        Trip trip = tripDao.get("trip1");
        assertNotNull(trip);
        assertEquals(1, trip.getHotels().size());
        assertEquals("Old Hotel", trip.getHotels().get(0).getName());
    }

    @Test
    void testExecute_tripNotFound_doesNotSaveButStillCallsSuccess() {
        // User with currentTripId pointing to a non-existent trip
        List<String> trips = new ArrayList<>();
        trips.add("missingTrip");
        User userWithoutTrip = new User("bob", "pwd", "missingTrip", trips);
        userDao.save(userWithoutTrip);
        userDao.setCurrentUsername("bob");

        FakeHotelSearchGateway gateway = new FakeHotelSearchGateway();

        final boolean[] successCalled = {false};
        final boolean[] failCalled = {false};

        HotelSearchOutputBoundary presenter = new HotelSearchOutputBoundary() {
            @Override
            public void prepareSuccessView(HotelSearchOutputData outputData) {
                successCalled[0] = true;
            }

            @Override
            public void prepareFailView(String errorMessage) {
                failCalled[0] = true;
            }
        };

        HotelSearchInputBoundary interactor =
                new HotelSearchInteractor(gateway, presenter, userDao, tripDao);

        HotelSearchInputData inputData =
                new HotelSearchInputData("Bali hotel", "2025-01-01", "2025-01-10");

        interactor.execute(inputData);

        assertTrue(successCalled[0]);
        assertFalse(failCalled[0]);

        Trip existingTrip = tripDao.get("trip1");
        assertNotNull(existingTrip);
        assertEquals(1, existingTrip.getHotels().size());
        assertEquals("Old Hotel", existingTrip.getHotels().get(0).getName());
    }
}