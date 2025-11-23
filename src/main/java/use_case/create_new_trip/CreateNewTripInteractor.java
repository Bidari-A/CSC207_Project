package use_case.create_new_trip;

public class CreateNewTripInteractor implements CreateNewTripInputBoundary {

    private final CreateNewTripOutputBoundary createNewTripPresenter;

    public CreateNewTripInteractor(CreateNewTripOutputBoundary createNewTripPresenter) {
        this.createNewTripPresenter = createNewTripPresenter;
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

        String itinerary =
                "Sample itinerary for " + tripName + " on " + date + ".\n\n" +
                        "(Dummy content until the real API is connected.)";

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
