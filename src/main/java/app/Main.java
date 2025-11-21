package app;

import javax.swing.*;

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
                .addCreateNewTripUseCase()
                .addTripView()
                .addLoadTripDetailUseCase()
                .build();

        application.pack();
        application.setLocationRelativeTo(null);
        application.setVisible(true);
    }
}

