package use_case.create_new_trip;

public class CreateNewTripInteractor implements CreateNewTripInputBoundary {

    private final CreateNewTripOutputBoundary createNewTripPresenter;
    private final TripAIDataAccessInterface tripAIDataAccessObject;

    public CreateNewTripInteractor(CreateNewTripOutputBoundary createNewTripPresenter,
                                   TripAIDataAccessInterface tripAIDataAccessObject) {
        this.createNewTripPresenter = createNewTripPresenter;
        this.tripAIDataAccessObject = tripAIDataAccessObject;
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
    //maybe check later
    @Override
    public void prepareScreen() {
        createNewTripPresenter.prepareScreen();
    }

    public void goBack() {
        createNewTripPresenter.prepareBackView();
    }
}
