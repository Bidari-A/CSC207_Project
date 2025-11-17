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

        // later you will call createNewTripPresenter.prepareSuccessView(outputData)
    }
    //maybe check later
    @Override
    public void prepareScreen() {
        createNewTripPresenter.showCreateNewTripView();
    }
}
