package use_case.create_new_trip;

import entity.Accommodation;
import entity.Destination;
import entity.Flight;
import entity.Trip;

import java.util.ArrayList;
import java.util.List;

public class CreateNewTripInteractor implements CreateNewTripInputBoundary {

    private final CreateNewTripOutputBoundary createNewTripPresenter;
    private final TripAIDataAccessInterface tripAIDataAccessObject;
    private final CreateNewTripTripDataAccessInterface tripSaver;
    private final CreateNewTripUserDataAccessInterface userDataAccess;

    public CreateNewTripInteractor(CreateNewTripOutputBoundary createNewTripPresenter,
                                   TripAIDataAccessInterface tripAIDataAccessObject,
                                   CreateNewTripTripDataAccessInterface tripSaver,
                                   CreateNewTripUserDataAccessInterface userDataAccess) {
        this.createNewTripPresenter = createNewTripPresenter;
        this.tripAIDataAccessObject = tripAIDataAccessObject;
        this.tripSaver = tripSaver;
        this.userDataAccess = userDataAccess;
    }

    @Override
    public void execute(CreateNewTripInputData inputData) {

        String from = inputData.getFrom();
        String to = inputData.getTo();
        String startDate = inputData.getStartDate();
        String endDate = inputData.getEndDate();
        String currentUsername = inputData.getCurrentUsername();

        String tripName = from + " to " + to;

        String prompt =
                "You are a travel assistant. Using the trip information below, generate a plan.\n"
                        + "From: " + from + "\n"
                        + "To: " + to + "\n"
                        + "Start Date: " + startDate + "\n"
                        + "End Date: " + endDate + "\n\n"
                        + "Your response must be plain text in exactly this structure:\n\n"
                        + "TRIP SUMMARY:\n"
                        + "<A natural paragraph between 50 and 150 words that describes the trip, "
                        + "taking into account the length of stay between the start and end dates.>\n\n"
                        + "DESTINATIONS:\n"
                        + "- <Attraction or destination 1>\n"
                        + "- <Attraction or destination 2>\n"
                        + "- <Attraction or destination 3>\n"
                        + "- <More attractions appropriate for the trip duration>\n\n"
                        + "Rules:\n"
                        + "1. TRIP SUMMARY must be between 50 and 150 words.\n"
                        + "2. Under DESTINATIONS, list between 3 and 12 items, each on its own line starting with '- '.\n"
                        + "3. Do not add any other headings or sections. Do not use markdown, numbering, or extra labels.";


        String itinerary = tripAIDataAccessObject.generateTripPlan(prompt);

        // Parse destinations from the AI text
        List<Destination> attractions = parseDestinations(itinerary);

        // Build dates string for Trip entity
        String dates = startDate + " to " + endDate;

        // Empty lists for hotels and flights for now
        List<Accommodation> hotels = new ArrayList<>();
        List<Flight> flights = new ArrayList<>();

        // Create Trip with blank ID; DAO will generate the real ID
        Trip trip = new Trip(
                "",             // tripId will be set in DAO
                tripName,
                currentUsername,     // ownerUserName from input data
                "CURRENT",           // status
                dates,
                to,                  // main destination city
                hotels,
                flights,
                attractions          // parsed destinations from Gemini
        );

        // Save the generated trip via DAI (CreateNewTripTripDataAccessInterface)
        Trip savedTrip = tripSaver.saveTrip(trip);
        String tripId = savedTrip.getTripId();
        userDataAccess.updateUserTrips(currentUsername, tripId);
        userDataAccess.setCurrentTripId(currentUsername,tripId);
        CreateNewTripOutputData outputData = new CreateNewTripOutputData(
                tripName,
                from,
                to,
                startDate,   // startDate
                endDate,   // endDate
                itinerary
        );
        createNewTripPresenter.presentResult(outputData);
    }

    private List<Destination> parseDestinations(String aiText) {
        List<Destination> result = new ArrayList<>();

        String marker = "DESTINATIONS:";
        int index = aiText.indexOf(marker);
        if (index == -1) {
            // No DESTINATIONS section found
            return result;
        }

        // Take the part after "DESTINATIONS:"
        String after = aiText.substring(index + marker.length());

        // Split by lines
        String[] lines = after.split("\\R"); // splits on any line break

        for (String rawLine : lines) {
            String line = rawLine.trim();
            if (line.startsWith("- ")) {
                String name = line.substring(2).trim();
                result.add(new Destination(name));
            }
        }

        return result;
    }

    private String parseTripSummary(String aiText) {
        String summaryMarker = "TRIP SUMMARY:";
        String destinationsMarker = "DESTINATIONS:";

        int summaryIndex = aiText.indexOf(summaryMarker);
        if (summaryIndex == -1) {
            // No summary marker found, just return the whole text
            return aiText;
        }

        // Start right after "TRIP SUMMARY:"
        int start = summaryIndex + summaryMarker.length();

        int end = aiText.indexOf(destinationsMarker, start);
        if (end == -1) {
            // No DESTINATIONS found; take everything after TRIP SUMMARY
            end = aiText.length();
        }

        String rawSummary = aiText.substring(start, end);
        return rawSummary.trim();
    }



    @Override
    public void prepareScreen() {
        // It is the interactor that contacts the presenter.
        // Controller does not directly touch presenter
        createNewTripPresenter.prepareScreen();
    }

    public void goBack() {
        createNewTripPresenter.prepareBackView();
    }

    @Override
    public void backFromResult() {
        // From the TripResultView, go back to CreateNewTripView
        createNewTripPresenter.presentBackToCreateNewTripView();
    }
}


