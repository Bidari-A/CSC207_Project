package use_case.complete_current_trip;

import entity.Trip;
import java.util.ArrayList;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.text.ParseException;

public class CompleteCurrentTripInteractor implements CompleteCurrentTripInputBoundary {
    private final CompleteCurrentTripDataAccessInterface dataAccessObject;
    private final CompleteCurrentTripOutputBoundary presenter;

    public CompleteCurrentTripInteractor(CompleteCurrentTripDataAccessInterface dataAccessObject,
                                         CompleteCurrentTripOutputBoundary presenter) {
        this.dataAccessObject = dataAccessObject;
        this.presenter = presenter;
    }

    @Override
    public void execute(CompleteCurrentTripInputData inputData) {
        try {
            // Create a Trip entity with the basic info
            // For now, we'll use empty lists for destinations, accommodations, and flights
            // The date will be parsed if possible, otherwise use empty list
            ArrayList<Date> tripDates = new ArrayList<>();
            try {
                // Try to parse the date string - support multiple formats
                String dateStr = inputData.getDate();
                SimpleDateFormat[] formats = {
                        new SimpleDateFormat("MM/dd/yyyy"),
                        new SimpleDateFormat("yyyy-MM-dd"),
                        new SimpleDateFormat("MM-dd-yyyy"),
                        new SimpleDateFormat("dd/MM/yyyy")
                };
                for (SimpleDateFormat sdf : formats) {
                    try {
                        Date date = sdf.parse(dateStr);
                        tripDates.add(date);
                        break;
                    } catch (ParseException e) {
                        // Try next format
                    }
                }
                // If all formats fail, leave the list empty
            } catch (Exception e) {
                // If parsing fails, leave the list empty
            }

            Trip trip = new Trip(
                    inputData.getTripName(),
                    inputData.getCity(),
                    tripDates,
                    new ArrayList<>(), // destinations
                    new ArrayList<>(), // accommodations
                    new ArrayList<>()  // flights
            );

            // Save the trip to the user's trip list
            dataAccessObject.addTrip(inputData.getUsername(), trip);

            // Present success
            CompleteCurrentTripOutputData outputData =
                    new CompleteCurrentTripOutputData(true, "Trip completed successfully");
            presenter.presentCompletedTrip(outputData);
        } catch (Exception e) {
            presenter.presentError("Failed to complete trip: " + e.getMessage());
        }
    }
}
