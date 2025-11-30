package use_case.create_new_trip;

import entity.Trip;
import entity.Destination;
import entity.Accommodation;
import entity.Flight;
import java.lang.reflect.Method;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for CreateNewTripInteractor.
 */
class CreateNewTripInteractorTest {

    private CreateNewTripInteractor interactor;
    private MockTripAIDataAccess mockTripAI;
    private MockTripSaver mockTripSaver;
    private MockUserDataAccess mockUserDataAccess;
    private MockPresenter mockPresenter;

    @BeforeEach
    void setUp() {
        mockTripAI = new MockTripAIDataAccess();
        mockTripSaver = new MockTripSaver();
        mockUserDataAccess = new MockUserDataAccess();
        mockPresenter = new MockPresenter();

        interactor = new CreateNewTripInteractor(
                mockPresenter,
                mockTripAI,
                mockTripSaver,
                mockUserDataAccess
        );
    }

    /**
     * Happy path. Itinerary has TRIP SUMMARY and DESTINATIONS with bullet points.
     * Covers normal parsing branch for both summary and destinations.
     */
    @Test
    void testExecuteHappyPathParsesSummaryAndDestinations() {
        String itinerary =
                "TRIP SUMMARY:\n" +
                        "This is a wonderful trip from Toronto to Paris with many sights to see.\n\n" +
                        "DESTINATIONS:\n" +
                        "- Eiffel Tower\n" +
                        "- Louvre Museum\n" +
                        "- Montmartre\n";

        mockTripAI.itineraryToReturn = itinerary;

        CreateNewTripInputData inputData = new CreateNewTripInputData(
                "Toronto",
                "Paris",
                "2025-01-01",
                "2025-01-05",
                "testuser"
        );

        interactor.execute(inputData);

        // TripAI was called with a prompt that includes the input
        assertNotNull(mockTripAI.lastPrompt);
        assertTrue(mockTripAI.lastPrompt.contains("From: Toronto"));
        assertTrue(mockTripAI.lastPrompt.contains("To: Paris"));

        // Trip was saved
        assertNotNull(mockTripSaver.savedTrip, "Trip should be saved");
        Trip savedTrip = mockTripSaver.savedTrip;
        assertEquals("testuser", savedTrip.getOwnerUserName());
        assertEquals("Toronto to Paris", savedTrip.getTripName());
        assertEquals("CURRENT", savedTrip.getStatus());
        assertEquals("2025-01-01 to 2025-01-05", savedTrip.getDates());
        assertEquals("Paris", savedTrip.getDestination());

        // Destinations parsed correctly
        List<Destination> attractions = savedTrip.getAttractions();
        assertEquals(3, attractions.size());
        assertEquals("Eiffel Tower", attractions.get(0).getName());
        assertEquals("Louvre Museum", attractions.get(1).getName());
        assertEquals("Montmartre", attractions.get(2).getName());

        // User trips updated with generated id
        assertTrue(mockUserDataAccess.updateCalled, "updateUserTrips should be called");
        assertEquals("testuser", mockUserDataAccess.updateUsername);
        assertEquals("GENERATED_ID", mockUserDataAccess.updateTripId);

        // Presenter was called
        assertTrue(mockPresenter.presentResultCalled, "presentResult should be called");
        assertNotNull(mockPresenter.outputData);
        assertEquals("Toronto to Paris", mockPresenter.outputData.getTripName());
        assertEquals("Toronto", mockPresenter.outputData.getFrom());
        assertEquals("Paris", mockPresenter.outputData.getTo());
        assertEquals("2025-01-01", mockPresenter.outputData.getStartDate());
        // Interactor currently passes startDate for endDate
        assertEquals("2025-01-01", mockPresenter.outputData.getEndDate());
        assertEquals(itinerary, mockPresenter.outputData.getItinerary());
    }

    /**
     * Itinerary has DESTINATIONS but no TRIP SUMMARY.
     * This covers the branch where parseTripSummary does not find the summary marker.
     */
    @Test
    void testExecuteItineraryWithoutSummary() {
        String itinerary =
                "Some intro text without the marker.\n" +
                        "DESTINATIONS:\n" +
                        "- Place One\n" +
                        "- Place Two\n";

        mockTripAI.itineraryToReturn = itinerary;

        CreateNewTripInputData inputData = new CreateNewTripInputData(
                "A",
                "B",
                "2025-02-01",
                "2025-02-03",
                "user2"
        );

        interactor.execute(inputData);

        // Trip saved and attractions parsed
        assertNotNull(mockTripSaver.savedTrip);
        assertEquals(2, mockTripSaver.savedTrip.getAttractions().size());
        assertEquals("user2", mockTripSaver.savedTrip.getOwnerUserName());

        // Presenter still called with itinerary
        assertTrue(mockPresenter.presentResultCalled);
        assertEquals(itinerary, mockPresenter.outputData.getItinerary());
    }

    /**
     * Itinerary has TRIP SUMMARY but no DESTINATIONS marker.
     * This covers:
     *  - parseTripSummary branch where DESTINATIONS is not found
     *  - parseDestinations branch where marker is missing and result is empty list
     */
    @Test
    void testExecuteItineraryWithoutDestinations() {
        String itinerary =
                "TRIP SUMMARY:\n" +
                        "Short summary text only. No destinations section.\n";

        mockTripAI.itineraryToReturn = itinerary;

        CreateNewTripInputData inputData = new CreateNewTripInputData(
                "X",
                "Y",
                "2025-03-01",
                "2025-03-02",
                "user3"
        );

        interactor.execute(inputData);

        assertNotNull(mockTripSaver.savedTrip);
        assertEquals(0, mockTripSaver.savedTrip.getAttractions().size(),
                "Attractions list should be empty when there is no DESTINATIONS section");

        assertTrue(mockPresenter.presentResultCalled);
    }

    /**
     * prepareScreen should delegate to presenter.prepareScreen.
     */
    @Test
    void testPrepareScreen() {
        interactor.prepareScreen();
        assertTrue(mockPresenter.prepareScreenCalled);
    }


    /**
     * Itinerary contains an empty bullet after DESTINATIONS.
     * This covers the branch where a line starts with "- "
     * but the destination name is empty, so it is skipped.
     */

    @Test
    void testExecuteSkipsEmptyDestinationLine() {
        String itinerary =
                "TRIP SUMMARY:\n" +
                        "Short summary.\n\n" +
                        "DESTINATIONS:\n" +
                        "- First Place\n" +
                        "- \n" +                // empty destination line
                        "- Second Place\n";



        mockTripAI.itineraryToReturn = itinerary;

        CreateNewTripInputData inputData = new CreateNewTripInputData(
                "CityA",
                "CityB",
                "2025-04-01",
                "2025-04-03",
                "user4"
        );

        interactor.execute(inputData);

        assertNotNull(mockTripSaver.savedTrip);
        // Only the non empty destinations should be added
        assertEquals(2, mockTripSaver.savedTrip.getAttractions().size());
        assertEquals("First Place", mockTripSaver.savedTrip.getAttractions().get(0).getName());
        assertEquals("Second Place", mockTripSaver.savedTrip.getAttractions().get(1).getName());
    }

    /**
     * Ensures that parseDestinations ignores lines in the DESTINATIONS section
     * that do not start with "- ", and only parses valid bullet entries.
     */
    @Test
    void testExecuteIgnoresNonBulletLinesInDestinations() {
        String itinerary =
                "TRIP SUMMARY:\n" +
                        "Summary text.\n\n" +
                        "DESTINATIONS:\n" +
                        "Not a bullet line\n" +      // <<<<<< this is the missing branch
                        "- Real Destination\n";

        mockTripAI.itineraryToReturn = itinerary;

        CreateNewTripInputData inputData = new CreateNewTripInputData(
                "A",
                "B",
                "2025-01-01",
                "2025-01-02",
                "user"
        );

        interactor.execute(inputData);

        assertNotNull(mockTripSaver.savedTrip);
        List<Destination> attractions = mockTripSaver.savedTrip.getAttractions();

        assertEquals(1, attractions.size());
        assertEquals("Real Destination", attractions.get(0).getName());
    }



    /**
     * goBack should delegate to presenter.prepareBackView.
     */
    @Test
    void testGoBack() {
        interactor.goBack();
        assertTrue(mockPresenter.prepareBackViewCalled);
    }

    /**
     * Covers the parseTripSummary branch where TRIP SUMMARY exists
     * but DESTINATIONS is completely missing, causing end == -1.
     */
    @Test
    void testExecuteSummaryWithoutDestinationsTriggersEndMinusOneBranch() {
        String itinerary =
                "TRIP SUMMARY:\n" +
                        "This is only a summary.\n" +
                        "No destinations at all.";

        mockTripAI.itineraryToReturn = itinerary;

        CreateNewTripInputData inputData = new CreateNewTripInputData(
                "U",
                "V",
                "2025-07-01",
                "2025-07-02",
                "userX"
        );

        interactor.execute(inputData);

        assertNotNull(mockTripSaver.savedTrip);
        // Attractions should be empty
        assertEquals(0, mockTripSaver.savedTrip.getAttractions().size());

        // Presenter should still be called
        assertTrue(mockPresenter.presentResultCalled);

        // Summary should match trimmed part after TRIP SUMMARY:
        assertEquals("TRIP SUMMARY:\nThis is only a summary.\nNo destinations at all.",
                mockPresenter.outputData.getItinerary());
    }

    /**
     * Verifies that multiple bullet destinations in the DESTINATIONS section
     * are all parsed and saved as attractions on the Trip.
     */
    @Test
    void testExecuteParsesMultipleDestinations() {
        String itinerary =
                "TRIP SUMMARY:\n" +
                        "A nice long trip.\n\n" +
                        "DESTINATIONS:\n" +
                        "- Eiffel Tower\n" +
                        "- Louvre Museum\n" +
                        "- Montmartre\n" +
                        "- Notre Dame\n" +
                        "- Luxembourg Gardens\n";

        mockTripAI.itineraryToReturn = itinerary;

        CreateNewTripInputData inputData = new CreateNewTripInputData(
                "Toronto",
                "Paris",
                "2025-01-01",
                "2025-01-10",
                "multiUser"
        );

        interactor.execute(inputData);

        assertNotNull(mockTripSaver.savedTrip);
        List<Destination> attractions = mockTripSaver.savedTrip.getAttractions();

        assertEquals(5, attractions.size());
        assertEquals("Eiffel Tower", attractions.get(0).getName());
        assertEquals("Louvre Museum", attractions.get(1).getName());
        assertEquals("Montmartre", attractions.get(2).getName());
        assertEquals("Notre Dame", attractions.get(3).getName());
        assertEquals("Luxembourg Gardens", attractions.get(4).getName());
    }

    /**
     * When a line starts with "- " and has a non empty name,
     * parseDestinations should add a Destination to the result.
     */
    @Test
    void testParseDestinationsAddsDestinationWhenNameNotEmpty() throws Exception {
        String itinerary = "DESTINATIONS:\n- Eiffel Tower\n";

        Method method = CreateNewTripInteractor.class
                .getDeclaredMethod("parseDestinations", String.class);
        method.setAccessible(true);

        @SuppressWarnings("unchecked")
        List<Destination> result =
                (List<Destination>) method.invoke(interactor, itinerary);

        assertEquals(1, result.size());
        assertEquals("Eiffel Tower", result.get(0).getName());
    }

    /**
     * When a line starts with "- " but the name is empty,
     * parseDestinations should skip it.
     */
    @Test
    void testParseDestinationsSkipsEmptyName() throws Exception {
        String itinerary = "DESTINATIONS:\n-   \n";

        Method method = CreateNewTripInteractor.class
                .getDeclaredMethod("parseDestinations", String.class);
        method.setAccessible(true);

        @SuppressWarnings("unchecked")
        List<Destination> result =
                (List<Destination>) method.invoke(interactor, itinerary);

        assertEquals(0, result.size());
    }

    /**
     * backFromResult should delegate to presenter.presentBackToCreateNewTripView.
     */
    @Test
    void testBackFromResult() {
        interactor.backFromResult();
        assertTrue(mockPresenter.presentBackToCreateNewTripViewCalled);
    }

    // Mocks

    private static class MockTripAIDataAccess implements TripAIDataAccessInterface {
        String itineraryToReturn;
        String lastPrompt;

        @Override
        public String generateTripPlan(String prompt) {
            this.lastPrompt = prompt;
            return itineraryToReturn;
        }
    }

    private static class MockTripSaver implements CreateNewTripTripDataAccessInterface {
        Trip savedTrip;

        @Override
        public Trip saveTrip(Trip trip) {
            this.savedTrip = trip;

            // Simulate DAO generating an id and returning a new Trip with that id
            List<Accommodation> hotels = trip.getHotels();
            List<Flight> flights = trip.getFlights();
            List<Destination> attractions = trip.getAttractions();

            return new Trip(
                    "GENERATED_ID",
                    trip.getTripName(),
                    trip.getOwnerUserName(),
                    trip.getStatus(),
                    trip.getDates(),
                    trip.getDestination(),
                    hotels,
                    flights,
                    attractions
            );
        }
    }

    private static class MockUserDataAccess implements CreateNewTripUserDataAccessInterface {
        boolean updateCalled = false;
        String updateUsername;
        String updateTripId;

        @Override
        public void updateUserTrips(String username, String tripId) {
            updateCalled = true;
            updateUsername = username;
            updateTripId = tripId;
        }

        @Override
        public void setCurrentTripId(String username, String tripId) {

        }
    }

    private static class MockPresenter implements CreateNewTripOutputBoundary {

        boolean presentResultCalled = false;
        boolean prepareScreenCalled = false;
        boolean prepareBackViewCalled = false;
        boolean presentBackToCreateNewTripViewCalled = false;

        CreateNewTripOutputData outputData;

        @Override
        public void presentResult(CreateNewTripOutputData outputData) {
            presentResultCalled = true;
            this.outputData = outputData;
        }

        @Override
        public void prepareScreen() {
            prepareScreenCalled = true;
        }

        @Override
        public void prepareBackView() {
            prepareBackViewCalled = true;
        }

        @Override
        public void presentBackToCreateNewTripView() {
            presentBackToCreateNewTripViewCalled = true;
        }
    }
}
