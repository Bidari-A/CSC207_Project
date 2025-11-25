package use_case.create_new_trip;
import use_case.create_new_trip.TripAIDataAccessInterface;

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
        System.out.println("Date: " + inputData.getDate());

        String from = inputData.getFrom();
        String to = inputData.getTo();
        String date = inputData.getDate();

        String tripName = from + " to " + to;

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
                date,   // startDate
                date,   // endDate (same for now)
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
