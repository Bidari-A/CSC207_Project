import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.JFrame;

import entity.Accommodation;
import entity.Destination;
import entity.Flight;
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

        // Create mock trip data
        List<Trip> trips = new ArrayList<>();

        // Trip 1: Paris Vacation
        List<Date> parisDates = new ArrayList<>();
        parisDates.add(new Date(2025 - 1900, 5, 12)); // June 12, 2025
        parisDates.add(new Date(2025 - 1900, 5, 20)); // June 20, 2025

        List<Destination> parisDestinations = new ArrayList<>();
        parisDestinations.add(new Destination("Eiffel Tower", "Champ de Mars, 5 Avenue Anatole France", "Iconic iron lattice tower", 0.0f));
        parisDestinations.add(new Destination("Louvre Museum", "Rue de Rivoli", "World's largest art museum", 17.0f));

        List<Accommodation> parisAccommodations = new ArrayList<>();
        parisAccommodations.add(new Accommodation("Hotel Le Meurice", "228 Rue de Rivoli, 75001 Paris", 500.0f));

        List<Flight> parisFlights = new ArrayList<>();
        parisFlights.add(new Flight("Air France AF-347", new Date(2025 - 1900, 5, 12, 19, 0), 800.0f));

        trips.add(new Trip("Paris Vacation", "Paris", parisDates, parisDestinations, parisAccommodations, parisFlights));

        // Trip 2: Tokyo Adventure
        List<Date> tokyoDates = new ArrayList<>();
        tokyoDates.add(new Date(2025 - 1900, 7, 1)); // August 1, 2025
        tokyoDates.add(new Date(2025 - 1900, 7, 10)); // August 10, 2025

        List<Destination> tokyoDestinations = new ArrayList<>();
        tokyoDestinations.add(new Destination("Tokyo Skytree", "1 Chome-1-2 Oshiage, Sumida City", "Tallest tower in Japan", 20.0f));
        tokyoDestinations.add(new Destination("Shibuya Crossing", "Shibuya City", "World's busiest pedestrian crossing", 0.0f));

        List<Accommodation> tokyoAccommodations = new ArrayList<>();
        tokyoAccommodations.add(new Accommodation("Park Hyatt Tokyo", "3 Chome-7-1-2 Nishi-Shinjuku", 600.0f));

        List<Flight> tokyoFlights = new ArrayList<>();
        tokyoFlights.add(new Flight("Japan Airlines JL-61", new Date(2025 - 1900, 7, 1, 10, 30), 1200.0f));

        trips.add(new Trip("Tokyo Adventure", "Tokyo", tokyoDates, tokyoDestinations, tokyoAccommodations, tokyoFlights));

        // Trip 3: New York City
        List<Date> nycDates = new ArrayList<>();
        nycDates.add(new Date(2025 - 1900, 8, 15)); // September 15, 2025
        nycDates.add(new Date(2025 - 1900, 8, 22)); // September 22, 2025

        List<Destination> nycDestinations = new ArrayList<>();
        nycDestinations.add(new Destination("Statue of Liberty", "Liberty Island", "Iconic symbol of freedom", 25.0f));
        nycDestinations.add(new Destination("Central Park", "Manhattan", "Urban park in Manhattan", 0.0f));

        List<Accommodation> nycAccommodations = new ArrayList<>();
        nycAccommodations.add(new Accommodation("The Plaza", "768 5th Ave, New York", 700.0f));

        List<Flight> nycFlights = new ArrayList<>();
        nycFlights.add(new Flight("American Airlines AA-100", new Date(2025 - 1900, 8, 15, 8, 0), 600.0f));

        trips.add(new Trip("New York City", "New York", nycDates, nycDestinations, nycAccommodations, nycFlights));

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
