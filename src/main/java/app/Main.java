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
                .addTripView()
                .addCreateNewTripUseCase()
                .addLoadTripDetailUseCase()
                .addDeleteCurrentTripUseCase()
                .addCompleteCurrentTripUseCase()
                .build();

        application.pack();
        application.setLocationRelativeTo(null);
        application.setVisible(true);
    }
}

