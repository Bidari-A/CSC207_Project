import interface_adapter.trip.TripState;
import interface_adapter.trip.TripViewModel;
import view.TripView;

import javax.swing.*;

public class TripViewTest {

    public static void main(String[] args) {

        // Create the view model + view
        TripViewModel vm = new TripViewModel();
        TripView view = new TripView(vm);

        // Mock some fake trip data
        TripState s = new TripState();
        s.setTripName("Paris Vacation");
        s.setCity("Paris");
        s.setDate("June 12 - June 20, 2025");
        s.setAttractions("Eiffel Tower\nLouvre Museum");
        s.setFlightDetails("Air France AF-347\nYYZ → CDG\nDepart: 7:00 PM");
        s.setHotelDetails("Hotel Le Meurice\n5-star luxury suite");
        s.setGeminiInput("What should I pack?");

        // Push state into VM and update view
        vm.setState(s);
        vm.firePropertyChange();

        // Show UI in a window
        JFrame frame = new JFrame("Trip View Test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(view);
        frame.pack();
        frame.setVisible(true);
    }
}
