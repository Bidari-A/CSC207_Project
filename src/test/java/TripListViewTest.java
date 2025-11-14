import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import entity.Trip;
import interface_adapter.ViewManagerModel;
import interface_adapter.trip_list.TripListState;
import interface_adapter.trip_list.TripListViewModel;
import view.TripListView;

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

        // Show UI in a window
        JFrame frame = new JFrame("Trip List View Test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(tripListView);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
