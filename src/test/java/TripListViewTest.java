import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

import entity.Trip;
import interface_adapter.ViewManagerModel;
import interface_adapter.trip_list.TripListController;
import interface_adapter.trip_list.TripListState;
import interface_adapter.trip_list.TripListViewModel;
import view.TripListView;
import interface_adapter.trip.TripState;
import interface_adapter.trip.TripViewModel;
import view.TripView;

public class TripListViewTest {

    public static void main(String[] args) {

        // Create the view model + view
        TripListViewModel tripListViewModel = new TripListViewModel();
        ViewManagerModel viewManagerModel = new ViewManagerModel();
        TripListView tripListView = new TripListView(tripListViewModel, viewManagerModel);

        // Create mock trip data (minimal since only trip names are displayed in the list)
        List<Trip> trips = new ArrayList<>();

        // Create trips with minimal data - only name and city are needed for display
        trips.add(new Trip("Paris Vacation", "Paris",
                new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>()));
        trips.add(new Trip("Tokyo Adventure", "Tokyo",
                new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>()));
        trips.add(new Trip("New York City", "New York",
                new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>()));

        // Create state and set trips
        TripListState state = new TripListState();
        state.setTrips(trips);
        state.setUsername("testuser");

        // Push state into VM and update view
        tripListViewModel.setState(state);
        tripListViewModel.firePropertyChange();

        // Wire a dummy TripListController for the Details button
        tripListView.setTripListController(new TripListController() {
            @Override
            public void executeDetails(String username, String tripName) {
                // ★ Create a NEW TripViewModel + TripView each time ★
                TripViewModel popupVM = new TripViewModel();
                TripView popupView = new TripView(popupVM);

                TripState s = new TripState();
                s.setTripName(tripName);
                s.setCity("Paris");
                s.setDate("June 12 – June 20, 2025");
                s.setAttractions("Eiffel Tower\nLouvre Museum");
                s.setFlightDetails("Air France AF-347\nYYZ → CDG\nDepart: 7:00 PM");
                s.setHotelDetails("Hotel Le Meurice\n5-star luxury suite");
                s.setGeminiInput("What should I pack?");

                popupVM.setState(s);
                popupVM.firePropertyChange();

                // OPEN POPUP WINDOW
                JFrame tripFrame = new JFrame("Trip Details");
                tripFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                tripFrame.getContentPane().add(popupView);

                tripFrame.pack();
                tripFrame.setLocationRelativeTo(null);
                tripFrame.setVisible(true);
            }

            @Override
            public void executeDelete(String username, String tripName) {
                System.out.println("Delete clicked: " + tripName);
            }
        });

        // Show UI in a window
        JFrame frame = new JFrame("Trip List View Test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(tripListView);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
