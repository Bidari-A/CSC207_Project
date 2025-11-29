package use_case.load_trip_detail;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import data_access.FileTripDataAccessObject;
import entity.*;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import data_access.FileUserDataAccessObject;

public class LoadTripDetailInteractorTest {
    private static final String TEST_USER_PATH = "test_users.json";
    private static final String TEST_TRIP_PATH = "test_trips.json";
    private FileUserDataAccessObject userRepository;
    private FileTripDataAccessObject tripRepository;
    private UserFactory factory;

    @BeforeEach
    void setUp() {
        // Clean up any existing test file
        File userFile = new File(TEST_USER_PATH);
        if (userFile.exists()) {
            userFile.delete();
        }
        File tripFile = new File(TEST_TRIP_PATH);
        if (tripFile.exists()) {
            tripFile.delete();
        }

        factory = new UserFactory();
        userRepository = new FileUserDataAccessObject(TEST_USER_PATH, factory);
        tripRepository = new FileTripDataAccessObject(TEST_TRIP_PATH);
        userRepository.setTripDataAccessObject(tripRepository);
    }

    @AfterEach
    void tearDown() {
        // Clean up test file after each test
        File testFile = new File(TEST_USER_PATH);
        if (testFile.exists()) {
            testFile.delete();
        }

        File tripFile = new File(TEST_TRIP_PATH);
        if (tripFile.exists()) {
            tripFile.delete();
        }
    }

    // 1. Success after opening trip details (Trip list View)
    @Test
    void successTestTripListView(){
        User user = factory.create("Paul", "password");
        userRepository.save(user);

        Accommodation accommodation = new Accommodation("Four Seasons Hotel", "Paris", 100);
        Flight flight = new Flight("RyanAir", "LHR → CDG on 2024-12-12", 100);
        Destination destination = new Destination("Eiffel Tower", "Paris",
                "Famous attraction",
                10);

        List<Accommodation> accommodations = new ArrayList<>();
        accommodations.add(accommodation);
        List<Flight> flights = new ArrayList<>();
        flights.add(flight);
        List<Destination> destinations = new ArrayList<>();
        destinations.add(destination);

        Trip newTrip = new Trip("newTripId1", "Paris Trip", "Paul", "currentTrip",
                "2024-12-12 to 2024-12-13", "Paris",accommodations, flights, destinations
        );

        tripRepository.save(newTrip);
        List<String> tripList = user.getTripList();
        tripList.add(newTrip.getTripId());
        userRepository.save(user);


        LoadTripDetailInputData loadTripDetailInputData = new LoadTripDetailInputData("Paul",
                "trip list", "newTripId1");



        LoadTripDetailOutputBoundary successPresenter = new LoadTripDetailOutputBoundary() {
            @Override
            public void prepareTripView(LoadTripDetailOutputData outputData) {
                assertEquals("Paris Trip", outputData.getTripName());
                assertEquals("Paris", outputData.getCityName());
                assertEquals("2024-12-12 to 2024-12-13", outputData.getDate());
                assertEquals("Eiffel Tower\nParis\nFamous attraction\nPrice: $10.00", outputData.getAttractions());
                assertEquals("RyanAir\nLHR → CDG on 2024-12-12", outputData.getFlightDetails());
                assertEquals("Four Seasons Hotel\nParis\n$100.0", outputData.getHotelDetails());
                assertEquals("trip list", outputData.getPrevViewName());
            }

            @Override
            public void back() {

            }
        };

        LoadTripDetailInputBoundary interactor = new LoadTripDetailInteractor(userRepository, successPresenter);
        interactor.execute(loadTripDetailInputData);
    }
    // 2. Success after opening trip details (Logged In View)
    @Test
    void successTestLoggedInView(){
        User user = factory.create("Paul", "password");
        userRepository.save(user);

        Accommodation accommodation = new Accommodation("Four Seasons Hotel", "Paris", 100);
        Flight flight = new Flight("RyanAir", "LHR → CDG on 2024-12-12", 100);
        Destination destination = new Destination("Eiffel Tower", "Paris",
                "Famous attraction",
                10);

        List<Accommodation> accommodations = new ArrayList<>();
        accommodations.add(accommodation);
        List<Flight> flights = new ArrayList<>();
        flights.add(flight);
        List<Destination> destinations = new ArrayList<>();
        destinations.add(destination);

        Trip newTrip = new Trip("newTripId1", "Paris Trip", "Paul", "currentTrip",
                "2024-12-12 to 2024-12-13", "Paris",accommodations, flights, destinations
        );


        tripRepository.save(newTrip);
        user.setCurrentTripId("newTripId1");
        userRepository.save(user);


        LoadTripDetailInputData loadTripDetailInputData = new LoadTripDetailInputData("Paul",
                "logged in");



        LoadTripDetailOutputBoundary successPresenter = new LoadTripDetailOutputBoundary() {
            @Override
            public void prepareTripView(LoadTripDetailOutputData outputData) {
                assertEquals("Paris Trip", outputData.getTripName());
                assertEquals("Paris", outputData.getCityName());
                assertEquals("2024-12-12 to 2024-12-13", outputData.getDate());
                assertEquals("Eiffel Tower\nParis\nFamous attraction\nPrice: $10.00", outputData.getAttractions());
                assertEquals("RyanAir\nLHR → CDG on 2024-12-12", outputData.getFlightDetails());
                assertEquals("Four Seasons Hotel\nParis\n$100.0", outputData.getHotelDetails());
                assertEquals("logged in", outputData.getPrevViewName());
            }

            @Override
            public void back() {

            }
        };

        LoadTripDetailInputBoundary interactor = new LoadTripDetailInteractor(userRepository, successPresenter);
        interactor.execute(loadTripDetailInputData);

    }

    // 3. Failure after opening trip details with no current trip (Logged In View)
    @Test
    void failureTestLoggedInView(){
        User user = factory.create("Paul", "password");
        userRepository.save(user);
        LoadTripDetailInputData loadTripDetailInputData = new LoadTripDetailInputData("Paul",
                "logged in");



        LoadTripDetailOutputBoundary successPresenter = new LoadTripDetailOutputBoundary() {
            @Override
            public void prepareTripView(LoadTripDetailOutputData outputData) {
                assertEquals("Trip not found", outputData.getTripName());
                assertEquals("", outputData.getCityName());
                assertEquals("", outputData.getDate());
                assertEquals("Trip not found", outputData.getAttractions());
                assertEquals("Trip not found", outputData.getFlightDetails());
                assertEquals("Trip not found", outputData.getHotelDetails());
                assertEquals("logged in", outputData.getPrevViewName());
            }

            @Override
            public void back() {

            }
        };

        LoadTripDetailInputBoundary interactor = new LoadTripDetailInteractor(userRepository, successPresenter);
        interactor.execute(loadTripDetailInputData);
    }

    // 4. Failure after opening trip details with no existing tripId (Trip List View)
    @Test
    void failureTestTripListView(){
        User user = factory.create("Paul", "password");
        userRepository.save(user);
        LoadTripDetailInputData loadTripDetailInputData = new LoadTripDetailInputData("Paul",
                "trip list", "newTripId1");



        LoadTripDetailOutputBoundary successPresenter = new LoadTripDetailOutputBoundary() {
            @Override
            public void prepareTripView(LoadTripDetailOutputData outputData) {
                assertEquals("Trip not found", outputData.getTripName());
                assertEquals("", outputData.getCityName());
                assertEquals("", outputData.getDate());
                assertEquals("Trip not found", outputData.getAttractions());
                assertEquals("Trip not found", outputData.getFlightDetails());
                assertEquals("Trip not found", outputData.getHotelDetails());
                assertEquals("trip list", outputData.getPrevViewName());
            }

            @Override
            public void back() {

            }
        };

        LoadTripDetailInputBoundary interactor = new LoadTripDetailInteractor(userRepository, successPresenter);
        interactor.execute(loadTripDetailInputData);
    }

    @Test
    void testEmptyListsProduceDefaultMessages() {
        User user = factory.create("Paul", "password");
        user.setCurrentTripId("trip1");
        userRepository.save(user);

        Trip emptyTrip = new Trip(
                "trip1", "Empty Trip", "Paul", "",
                "2024-12-12", "",
                new ArrayList<>(), new ArrayList<>(), new ArrayList<>()
        );

        tripRepository.save(emptyTrip);

        LoadTripDetailInputData input = new LoadTripDetailInputData("Paul", "logged in");

        LoadTripDetailOutputBoundary presenter = new LoadTripDetailOutputBoundary() {
            @Override
            public void prepareTripView(LoadTripDetailOutputData outputData) {
                assertEquals("No attractions selected", outputData.getAttractions());
                assertEquals("No flights selected", outputData.getFlightDetails());
                assertEquals("No hotels selected", outputData.getHotelDetails());
            }
            @Override
            public void back() {}
        };

        LoadTripDetailInputBoundary interactor = new LoadTripDetailInteractor(userRepository, presenter);
        interactor.execute(input);
    }
    @Test
    void testBackMethod() {
        final boolean[] called = {false};
        LoadTripDetailOutputBoundary presenter = new LoadTripDetailOutputBoundary() {

            @Override
            public void prepareTripView(LoadTripDetailOutputData outputData) {}

            @Override
            public void back() {
                called[0] = true;
            }
        };

        LoadTripDetailInteractor interactor = new LoadTripDetailInteractor(userRepository, presenter);
        interactor.back();
        assertEquals(called[0], true);
    }

    @Test
    void testEmptyCityNameAndZeroPrices() {
        User user = factory.create("Paul", "pass");
        user.setCurrentTripId("tripZ");
        userRepository.save(user);

        // price == 0 to hit the "else" of ternary branches
        Accommodation hotel = new Accommodation("Budget Inn", "Somewhere", 0);
        Flight flight = new Flight("AirTest", "TestFlight", 0);
        Destination dest = new Destination("Park", "City", "Nice place", 0);

        Trip trip = new Trip("tripZ", "Trip to Berlin", "Paul", "",
                "dates", "", // cityName empty → triggers fallback
                List.of(hotel), List.of(flight), List.of(dest));

        tripRepository.save(trip);

        LoadTripDetailInputData input = new LoadTripDetailInputData("Paul", "logged in");

        LoadTripDetailOutputBoundary pres = new LoadTripDetailOutputBoundary() {
            @Override
            public void prepareTripView(LoadTripDetailOutputData o) {
                assertEquals("Berlin", o.getCityName()); // fallback via "to"
                assertEquals("Budget Inn\nSomewhere", o.getHotelDetails()); // no price line
                assertEquals("Park\nCity\nNice place", o.getAttractions()); // no price line
            }
            @Override public void back() {}
        };

        new LoadTripDetailInteractor(userRepository, pres).execute(input);
    }

    @Test
    void testCityNameNotEmptySkipsFallback() {
        User user = factory.create("Paul", "pass");
        user.setCurrentTripId("tripK");
        userRepository.save(user);

        Trip trip = new Trip(
                "tripK", "Some Trip Name", "Paul", "",
                "dates", "Vancouver", // SKIPS fallback
                new ArrayList<>(), new ArrayList<>(), new ArrayList<>());

        tripRepository.save(trip);

        LoadTripDetailInputData input = new LoadTripDetailInputData("Paul", "logged in");

        LoadTripDetailOutputBoundary pres = new LoadTripDetailOutputBoundary() {
            @Override
            public void prepareTripView(LoadTripDetailOutputData o) {
                assertEquals("Vancouver", o.getCityName());
            }
            @Override public void back() {}
        };

        new LoadTripDetailInteractor(userRepository, pres).execute(input);
    }

    @Test
    void testExtractCityNameFallbackNoKeywords() {
        User user = factory.create("Paul", "pass");
        user.setCurrentTripId("tripF");
        userRepository.save(user);

        Trip trip = new Trip("tripF", "TorontoTrip", "Paul", "",
                "dates", null, // triggers fallback
                new ArrayList<>(), new ArrayList<>(), new ArrayList<>());

        tripRepository.save(trip);

        LoadTripDetailInputData input = new LoadTripDetailInputData("Paul", "logged in");

        LoadTripDetailOutputBoundary pres = new LoadTripDetailOutputBoundary() {
            @Override
            public void prepareTripView(LoadTripDetailOutputData o) {
                // "TorontoTrip" → first word fallback → "TorontoTrip"
                assertEquals("TorontoTrip", o.getCityName());
            }
            @Override public void back() {}
        };

        new LoadTripDetailInteractor(userRepository, pres).execute(input);
    }


    @Test
    void testExtractCityNameToPartsLengthGreaterThanOne() {
    User user = factory.create("Paul", "pass");
    user.setCurrentTripId("tripTo");
    userRepository.save(user);

    // EXACT string that matches: " to "
    String tripName = "Trip to Paris";

    Trip trip = new Trip(
            "tripTo",
            tripName,
            "Paul",
            "",
            "dates",
            null,   // IMPORTANT: forces extractCityName to run
            new ArrayList<>(), new ArrayList<>(), new ArrayList<>()
    );

    tripRepository.save(trip);

    LoadTripDetailInputData input = new LoadTripDetailInputData("Paul", "logged in");

    LoadTripDetailOutputBoundary presenter = new LoadTripDetailOutputBoundary() {
        @Override
        public void prepareTripView(LoadTripDetailOutputData outputData) {
            // If we hit parts.length > 1, cityName will be "Paris"
            assertEquals("Paris", outputData.getCityName());
        }
        @Override public void back() {}
    };

    new LoadTripDetailInteractor(userRepository, presenter).execute(input);
}

    @Test
    void testExtractCityNameInPartsLengthGreaterThanOne() {
        User user = factory.create("Paul", "pass");
        user.setCurrentTripId("tripIn");
        userRepository.save(user);

        String tripName = "Vacation in Tokyo";

        Trip trip = new Trip(
                "tripIn",
                tripName,
                "Paul",
                "",
                "dates",
                null,   // IMPORTANT: force fallback
                new ArrayList<>(), new ArrayList<>(), new ArrayList<>()
        );

        tripRepository.save(trip);

        LoadTripDetailInputData input = new LoadTripDetailInputData("Paul", "logged in");

        LoadTripDetailOutputBoundary presenter = new LoadTripDetailOutputBoundary() {
            @Override
            public void prepareTripView(LoadTripDetailOutputData outputData) {
                assertEquals("Tokyo", outputData.getCityName());
            }
            @Override public void back() {}
        };

        new LoadTripDetailInteractor(userRepository, presenter).execute(input);
    }



}
