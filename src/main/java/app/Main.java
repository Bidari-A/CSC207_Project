package app;

import javax.swing.JFrame;

public class Main {
    public static void main(String[] args) {
        AppBuilder appBuilder = new AppBuilder();
        JFrame application = appBuilder
                .addLoginView()
                .addSignupView()
                .addLoggedInView()
                .addTripListView()
                .addSignupUseCase()
                .addLoginUseCase()
                .addLogoutUseCase()
                .addLoadTripListUseCase()
                .addCreateNewTripView()
                //.addFlightSearchUseCase()
                //.addHotelSearchUseCase()
                .addCreateNewTripUseCase()
                .addTripView()
                .addLoadTripDetailUseCase()
                .addDeleteCurrentTripUseCase()
                .addCompleteTripUseCase()
                .build();


        // IF YOU WANT TO CALL API!! :))
        // Just try out the code here and understand it. Call the api using the DataAccessObjects.
        // String raw1 = new SerpApiFlightSearchGateway().fetchRawJson("YYZ", "HKG", "2025-11-23", "2025-11-25");
        // String summary1 = new SerpApiFlightSearchGateway().summarizeFirstBestFlight(raw1);
        // System.out.println(summary1);

        // String raw2 = new SerpApiHotelSearchGateway().fetchRawJson("Bali Resort", "2025-11-29", "2025-11-30");
        // String summary2 = new SerpApiHotelSearchGateway().summarizeFirstHotel(raw2);
        // System.out.println(summary2);

        application.pack();
        application.setLocationRelativeTo(null);
        application.setVisible(true);
    }
}

