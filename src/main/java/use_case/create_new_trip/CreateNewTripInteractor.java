
package use_case.create_new_trip;

/**
 * The Interactor for the Create New Trip Use Case.
 */
public class CreateNewTripInteractor implements CreateNewTripInputBoundary {

    public CreateNewTripInteractor() {
        // no dependencies yet (we will add presenter later)
    }

    @Override
    public void execute(CreateNewTripInputData inputData) {
        // TEMPORARY FAKE LOGIC
        System.out.println("Generating fake trip...");
        System.out.println("From: " + inputData.getFrom());
        System.out.println("To: " + inputData.getTo());
        System.out.println("Date: " + inputData.getDate());
    }
}
