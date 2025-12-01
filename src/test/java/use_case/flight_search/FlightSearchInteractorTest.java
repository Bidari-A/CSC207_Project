package use_case.flight_search;

import data_access.FileTripDataAccessObject;
import data_access.FileUserDataAccessObject;
import entity.Destination;
import entity.Flight;
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
 * FlightSearchInteractorTest
 * ============================================================
 *
 * This test class verifies the behavior of FlightSearchInteractor with the following goals:
 *
 *   - Ensure the SerpAPI gateway is called properly
 *   - Ensure success outputs are delivered to the presenter
 *   - Ensure failures are handled gracefully
 *   - Ensure flights are saved correctly into the Trip database
 *   - Ensure early-return guard clauses are correctly executed
 *
 * ------------------------------------------------------------
 * TEST CASE SUMMARY
 * ------------------------------------------------------------
 *
 * 1) testExecute_success_savesFlightAndCallsPresenter
 *
 *      PURPOSE:
 *      Verifies the normal success flow where:
 *        - API gateway works
 *        - Flight entity is created
 *        - Old flights are replaced
 *        - Presenter is notified
 *
 *      INPUT:
 *        from         = "YYZ"
 *        to           = "FCO"
 *        outboundDate = "2025-01-01"
 *        returnDate   = "2025-01-10"
 *
 *      FAKE GATEWAY RETURNS:
 *        JSON          = "{}"
 *        Summary       = string containing "LOT" and outbound time
 *        Flight Entity = Flight("LOT", "Outbound: 2025-01-01 10:00 | Return: 2025-01-10 18:00", 1257.0)
 *
 *      EXPECTED OUTPUT:
 *        - presenter.prepareSuccessView(...) is called
 *        - Trip now stores exactly one flight
 *        - Flight airline = LOT
 *        - Price = 1257.0
 *
 * ------------------------------------------------------------
 *
 * 2) testExecute_gatewayThrows_callsFailView
 *
 *      PURPOSE:
 *      Ensures API failure is safely handled by the interactor.
 *
 *      FAKE GATEWAY:
 *        fetchRawJson throws RuntimeException("API error for flights")
 *
 *      EXPECTED OUTPUT:
 *        - prepareFailView("API error for flights") is called
 *        - prepareSuccessView(...) is NOT called
 *
 * ------------------------------------------------------------
 *
 * 3) testExecute_noCurrentUser_doesNotSaveButStillCallsSuccess
 *
 *      PURPOSE:
 *      Tests the case where no user is logged in.
 *
 *      SETUP:
 *        userDao.setCurrentUsername(null)
 *
 *      EXPECTED OUTPUT:
 *        - presenter.prepareSuccessView(...) is called
 *        - No flight is written into the DB
 *
 * ------------------------------------------------------------
 *
 * 4) testExecute_bestFlightNull_doesNotSaveButStillCallsSuccess
 *
 *      PURPOSE:
 *      Tests when API returns no valid flight (null entity).
 *
 *      FAKE GATEWAY:
 *        buildFirstFlightEntity(...) returns null
 *
 *      EXPECTED OUTPUT:
 *        - prepareSuccessView(...) is called
 *        - saveBestFlightToCurrentTrip returns early
 *        - Trip has no flights
 *
 * ------------------------------------------------------------
 *
 * 5) testExecute_userIsNull_doesNotSaveButStillCallsSuccess
 *
 *      PURPOSE:
 *      Covers: userDao.getUser(username) == null
 *
 *      SETUP:
 *        currentUsername = "ghost" (never saved in DAO)
 *
 *      EXPECTED OUTPUT:
 *        - saveBestFlightToCurrentTrip returns early on user == null
 *        - Trip remains unchanged (no flights)
 *
 * ------------------------------------------------------------
 *
 * 6) testExecute_userHasNoCurrentTripId_doesNotSaveButStillCallsSuccess
 *
 *      PURPOSE:
 *      Covers: user.getCurrentTripId() == null
 *
 *      SETUP:
 *        new User("charlie", "pwd", null, trips)
 *        userDao.setCurrentUsername("charlie")
 *
 *      EXPECTED OUTPUT:
 *        - saveBestFlightToCurrentTrip returns early
 *        - Trip "trip1" unchanged
 *
 * ------------------------------------------------------------
 *
 * 7) testExecute_tripNotFound_doesNotSaveButStillCallsSuccess
 *
 *      PURPOSE:
 *      Covers: tripDao.get(tripId) == null
 *
 *      SETUP:
 *        new User("bob", "pwd", "missingTrip", trips)
 *        userDao.setCurrentUsername("bob")
 *
 *      EXPECTED OUTPUT:
 *        - no new flights are saved
 *        - existing real trip remains unchanged
 *
 * ============================================================
 */
class FlightSearchInteractorTest {

    private static final String TEST_USER_JSON = "test_flight_users.json";
    private static final String TEST_TRIP_JSON = "test_flight_trips.json";

    private FileUserDataAccessObject userDao;
    private FileTripDataAccessObject tripDao;
    private UserFactory userFactory;

    /**
     * Simple fake FlightSearchGateway that avoids real HTTP calls.
     */
    private static class FakeFlightSearchGateway implements FlightSearchGateway {
        boolean shouldThrow = false;

        @Override
        public String fetchRawJson(String from, String to, String outboundDate, String returnDate) {
            if (shouldThrow) {
                throw new RuntimeException("API error for flights");
            }
            return "{}"; // dummy JSON
        }

        @Override
        public String summarizeFirstBestFlight(String json) {
            return "- Airline: LOT\n- Departure: Outbound: 2025-01-01 10:00 | Return: 2025-01-10 18:00\n- Price: $1257.00 USD";
        }

        @Override
        public Flight buildFirstFlightEntity(String json) {
            return new Flight(
                    "LOT",
                    "Outbound: 2025-01-01 10:00 | Return: 2025-01-10 18:00",
                    1257.0f
            );
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
        List<String> tripList = new ArrayList<>();
        tripList.add("trip1");
        User user = new User("alice", "pwd", "trip1", tripList);
        userDao.save(user);
        userDao.setCurrentUsername("alice");

        // Create one empty trip
        Trip trip = new Trip(
                "trip1",
                "Trip to Rome",
                "alice",
                "PLANNED",
                "2025-01-01 to 2025-01-10",
                "Rome",
                new ArrayList<>(),                  // hotels
                new ArrayList<>(),                  // flights
                new ArrayList<Destination>()        // attractions
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
    void testExecute_success_savesFlightAndCallsPresenter() {
        FakeFlightSearchGateway gateway = new FakeFlightSearchGateway();

        final boolean[] successCalled = {false};
        final boolean[] failCalled = {false};

        FlightSearchOutputBoundary presenter = new FlightSearchOutputBoundary() {
            @Override
            public void prepareSuccessView(FlightSearchOutputData outputData) {
                successCalled[0] = true;
                String summary = outputData.getSummary();
                assertNotNull(summary);
                assertTrue(summary.contains("LOT"));
                assertTrue(summary.contains("Outbound: 2025-01-01 10:00"));
            }

            @Override
            public void prepareFailView(String errorMessage) {
                failCalled[0] = true;
                fail("Failure view should not be called in success scenario.");
            }
        };

        FlightSearchInputBoundary interactor =
                new FlightSearchInteractor(gateway, presenter, userDao, tripDao);

        FlightSearchInputData inputData =
                new FlightSearchInputData("YYZ", "FCO", "2025-01-01", "2025-01-10");

        interactor.execute(inputData);

        assertTrue(successCalled[0]);
        assertFalse(failCalled[0]);

        Trip trip = tripDao.get("trip1");
        assertNotNull(trip);
        assertEquals(1, trip.getFlights().size());
        Flight flight = trip.getFlights().get(0);
        assertEquals("LOT", flight.getAirlineName());
        assertEquals("Outbound: 2025-01-01 10:00 | Return: 2025-01-10 18:00",
                flight.getDepartureTimes());
        assertEquals(1257.0f, flight.getPrice(), 0.001);
    }

    @Test
    void testExecute_gatewayThrows_callsFailView() {
        FakeFlightSearchGateway gateway = new FakeFlightSearchGateway();
        gateway.shouldThrow = true;

        final boolean[] successCalled = {false};
        final boolean[] failCalled = {false};
        final String[] errorMsg = {null};

        FlightSearchOutputBoundary presenter = new FlightSearchOutputBoundary() {
            @Override
            public void prepareSuccessView(FlightSearchOutputData outputData) {
                successCalled[0] = true;
            }

            @Override
            public void prepareFailView(String errorMessage) {
                failCalled[0] = true;
                errorMsg[0] = errorMessage;
            }
        };

        FlightSearchInputBoundary interactor =
                new FlightSearchInteractor(gateway, presenter, userDao, tripDao);

        FlightSearchInputData inputData =
                new FlightSearchInputData("YYZ", "FCO", "2025-01-01", "2025-01-10");

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

        FakeFlightSearchGateway gateway = new FakeFlightSearchGateway();

        final boolean[] successCalled = {false};
        final boolean[] failCalled = {false};

        FlightSearchOutputBoundary presenter = new FlightSearchOutputBoundary() {
            @Override
            public void prepareSuccessView(FlightSearchOutputData outputData) {
                successCalled[0] = true;
            }

            @Override
            public void prepareFailView(String errorMessage) {
                failCalled[0] = true;
            }
        };

        FlightSearchInputBoundary interactor =
                new FlightSearchInteractor(gateway, presenter, userDao, tripDao);

        FlightSearchInputData inputData =
                new FlightSearchInputData("YYZ", "FCO", "2025-01-01", "2025-01-10");

        interactor.execute(inputData);

        assertTrue(successCalled[0]);
        assertFalse(failCalled[0]);

        Trip trip = tripDao.get("trip1");
        assertNotNull(trip);
        assertTrue(trip.getFlights().isEmpty());
    }

    @Test
    void testExecute_bestFlightNull_doesNotSaveButStillCallsSuccess() {
        FakeFlightSearchGateway gateway = new FakeFlightSearchGateway() {
            @Override
            public Flight buildFirstFlightEntity(String json) {
                return null; // simulate no flight found
            }
        };

        final boolean[] successCalled = {false};
        final boolean[] failCalled = {false};

        FlightSearchOutputBoundary presenter = new FlightSearchOutputBoundary() {
            @Override
            public void prepareSuccessView(FlightSearchOutputData outputData) {
                successCalled[0] = true;
            }

            @Override
            public void prepareFailView(String errorMessage) {
                failCalled[0] = true;
            }
        };

        FlightSearchInputBoundary interactor =
                new FlightSearchInteractor(gateway, presenter, userDao, tripDao);

        FlightSearchInputData inputData =
                new FlightSearchInputData("YYZ", "FCO", "2025-01-01", "2025-01-10");

        interactor.execute(inputData);

        assertTrue(successCalled[0]);
        assertFalse(failCalled[0]);

        Trip trip = tripDao.get("trip1");
        assertNotNull(trip);
        assertTrue(trip.getFlights().isEmpty());
    }

    @Test
    void testExecute_userIsNull_doesNotSaveButStillCallsSuccess() {
        // current user is someone who doesn't exist in the repo
        userDao.setCurrentUsername("ghost");

        FakeFlightSearchGateway gateway = new FakeFlightSearchGateway();

        final boolean[] successCalled = {false};
        final boolean[] failCalled = {false};

        FlightSearchOutputBoundary presenter = new FlightSearchOutputBoundary() {
            @Override
            public void prepareSuccessView(FlightSearchOutputData outputData) {
                successCalled[0] = true;
            }

            @Override
            public void prepareFailView(String errorMessage) {
                failCalled[0] = true;
            }
        };

        FlightSearchInputBoundary interactor =
                new FlightSearchInteractor(gateway, presenter, userDao, tripDao);

        FlightSearchInputData inputData =
                new FlightSearchInputData("YYZ", "FCO", "2025-01-01", "2025-01-10");

        interactor.execute(inputData);

        assertTrue(successCalled[0]);
        assertFalse(failCalled[0]);

        Trip trip = tripDao.get("trip1");
        assertNotNull(trip);
        assertTrue(trip.getFlights().isEmpty());
    }

    @Test
    void testExecute_userHasNoCurrentTripId_doesNotSaveButStillCallsSuccess() {
        // User exists but has null currentTripId
        List<String> trips = new ArrayList<>();
        User userNoTrip = new User("charlie", "pwd", null, trips);
        userDao.save(userNoTrip);
        userDao.setCurrentUsername("charlie");

        FakeFlightSearchGateway gateway = new FakeFlightSearchGateway();

        final boolean[] successCalled = {false};
        final boolean[] failCalled = {false};

        FlightSearchOutputBoundary presenter = new FlightSearchOutputBoundary() {
            @Override
            public void prepareSuccessView(FlightSearchOutputData outputData) {
                successCalled[0] = true;
            }

            @Override
            public void prepareFailView(String errorMessage) {
                failCalled[0] = true;
            }
        };

        FlightSearchInputBoundary interactor =
                new FlightSearchInteractor(gateway, presenter, userDao, tripDao);

        FlightSearchInputData inputData =
                new FlightSearchInputData("YYZ", "FCO", "2025-01-01", "2025-01-10");

        interactor.execute(inputData);

        assertTrue(successCalled[0]);
        assertFalse(failCalled[0]);

        Trip trip = tripDao.get("trip1");
        assertNotNull(trip);
        assertTrue(trip.getFlights().isEmpty());
    }

    @Test
    void testExecute_tripNotFound_doesNotSaveButStillCallsSuccess() {
        // User with currentTripId pointing to a non-existent trip
        List<String> trips = new ArrayList<>();
        trips.add("missingTrip");
        User userWithoutTrip = new User("bob", "pwd", "missingTrip", trips);
        userDao.save(userWithoutTrip);
        userDao.setCurrentUsername("bob");

        FakeFlightSearchGateway gateway = new FakeFlightSearchGateway();

        final boolean[] successCalled = {false};
        final boolean[] failCalled = {false};

        FlightSearchOutputBoundary presenter = new FlightSearchOutputBoundary() {
            @Override
            public void prepareSuccessView(FlightSearchOutputData outputData) {
                successCalled[0] = true;
            }

            @Override
            public void prepareFailView(String errorMessage) {
                failCalled[0] = true;
            }
        };

        FlightSearchInputBoundary interactor =
                new FlightSearchInteractor(gateway, presenter, userDao, tripDao);

        FlightSearchInputData inputData =
                new FlightSearchInputData("YYZ", "FCO", "2025-01-01", "2025-01-10");

        interactor.execute(inputData);

        assertTrue(successCalled[0]);
        assertFalse(failCalled[0]);

        Trip existingTrip = tripDao.get("trip1");
        assertNotNull(existingTrip);
        assertTrue(existingTrip.getFlights().isEmpty());
    }
}