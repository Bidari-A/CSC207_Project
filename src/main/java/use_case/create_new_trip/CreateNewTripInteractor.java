package use_case.create_new_trip;
import use_case.create_new_trip.TripAIDataAccessInterface;

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
                                   TripAIDataAccessInterface tripAIDataAccessObject) {
    public CreateNewTripInteractor(CreateNewTripOutputBoundary createNewTripPresenter,
                                   TripAIDataAccessInterface tripAIDataAccessObject,
                                   CreateNewTripTripDataAccessInterface tripSaver,
                                   CreateNewTripUserDataAccessInterface userDataAccess) {
        this.createNewTripPresenter = createNewTripPresenter;
        this.tripAIDataAccessObject = tripAIDataAccessObject;
        this.tripAIDataAccessObject = tripAIDataAccessObject;
        this.tripSaver = tripSaver;
        this.userDataAccess = userDataAccess;
    }

    @Override
    public void execute(CreateNewTripInputData inputData) {
        // TEMPORARY FAKE LOGIC
        System.out.println("Generating fake trip...");
        System.out.println("From: " + inputData.getFrom());
        System.out.println("To: " + inputData.getTo());
        System.out.println("Date: " + inputData.getStartDate());
        System.out.println("Date: " + inputData.getEndDate());

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

        // Temporary: just to check it works
        System.out.println("Parsed attractions:");
        for (Destination d : attractions) {
            System.out.println(" - " + d.getName());
        }

        String tripSummary = parseTripSummary(itinerary);

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

        // Store the trip
        // tripSaver.saveTrip(trip);
        Trip savedTrip = tripSaver.saveTrip(trip);
        String tripId = savedTrip.getTripId();
        userDataAccess.updateUserTrips(currentUsername, tripId);

        String prompt =
                "You are a travel assistant. Create a SHORT, friendly trip summary.\n"
                        + "From city: " + from + "\n"
                        + "To city: " + to + "\n"
                        + "Date of departure: " + date + "\n\n"
                        + "Respond with:\n"
                        + "1) One paragraph (3–4 sentences) describing the trip.\n"
                        + "2) A bullet list of 5 main places to visit.\n"
                        + "Keep it under 100 words. Plain text only, no markdown.";

        String itinerary = tripAIDataAccessObject.generateTripPlan(prompt);


        CreateNewTripOutputData outputData = new CreateNewTripOutputData(
                tripName,
                from,
                to,
                startDate,   // startDate
                startDate,   // endDate (same for now)
                itinerary
        );

        // THIS LINE IS WHAT MATTERS:
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
                if (!name.isEmpty()) {
                    result.add(new Destination(name));
                }
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



    //maybe check later
    @Override
    public void prepareScreen() {
        createNewTripPresenter.prepareScreen();
    }

    public void goBack() {
        createNewTripPresenter.prepareBackView();
    }
}
