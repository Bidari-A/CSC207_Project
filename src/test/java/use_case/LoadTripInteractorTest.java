package use_case;

import use_case.load_trip.*;
import data_access.FileTripDataAccessObject;
import entity.Trip;

import java.util.List;

public class LoadTripInteractorTest {

    public static void main(String[] args) {
        // 1. Create DAO pointing to your CSV folder
        String folderPath = "csv_data"; // folder containing your CSV files
        FileTripDataAccessObject dao = new FileTripDataAccessObject(folderPath);

        // 2. Create a simple mock presenter
        LoadTripOutputBoundary mockPresenter = new LoadTripOutputBoundary() {

            @Override
            public void prepareSuccessView(LoadTripOutputData outputData) {
                System.out.println("=== Trips loaded for user: " + outputData.getUsername() + " ===");
                List<Trip> trips = outputData.getTrips();
                for (Trip t : trips) {
                    System.out.println("Trip: " + t.getName() + ", City: " + t.getCityName());
                    System.out.println("Dates: " + t.getTripDates());
                    System.out.println("Destinations: " + t.getDestinations());
                    System.out.println("Accommodations: " + t.getAccommodations());
                    System.out.println("Flights: " + t.getFlights());
                    System.out.println("-----------------------------------");
                }
            }

            @Override
            public void prepareFailView(String errorMessage) {
                System.out.println("Failed to load trips: " + errorMessage);
            }

            @Override
            public void prepareBackView() {
                System.out.println("Go back triggered.");
            }
        };

        // 3. Create the interactor
        LoadTripInteractor interactor = new LoadTripInteractor(dao, mockPresenter);

        // 4. Create input data (choose a user to test)
        LoadTripInputData inputData = new LoadTripInputData("a");

        // 5. Execute
        interactor.execute(inputData);

        // 6. Test goBack
        interactor.goBack();
    }
}
