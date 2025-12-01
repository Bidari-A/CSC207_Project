package view;

import interface_adapter.complete_trip.CompleteTripController;
import interface_adapter.trip.TripController;
import interface_adapter.trip.TripState;
import interface_adapter.trip.TripViewModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Minimal Trip View UI.
 * Shows Trip details on left + right columns,
 * a Gemini input field at bottom, and a Back button.
 */
public class TripView extends JPanel implements ActionListener, PropertyChangeListener {

    private final TripViewModel tripViewModel;
    private TripController tripController;
    private CompleteTripController completeTripController;

    private final JLabel tripNameLabel = new JLabel();
    private final JLabel cityLabel = new JLabel();
    private final JLabel dateLabel = new JLabel();
    private final JTextArea attractionsArea = new JTextArea(4, 20);

    private final JTextArea flightArea = new JTextArea(4, 20);
    private final JTextArea hotelArea = new JTextArea(4, 20);

    private final JTextField geminiInputField = new JTextField(25);
    private final JButton backButton = new JButton("Back");
    private final JButton completeTripButton = new JButton("Complete Trip");

    public TripView(TripViewModel tripViewModel,
                    TripController tripController,
                    CompleteTripController completeTripController) {
        this.tripViewModel = tripViewModel;
        this.tripViewModel.addPropertyChangeListener(this);
        this.tripController = tripController;
        this.completeTripController = completeTripController;

        attractionsArea.setEditable(false);
        flightArea.setEditable(false);
        hotelArea.setEditable(false);

        setLayout(new BorderLayout(10, 10));

        // ---- Left panel ----
        final JPanel left = new JPanel();
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));

        left.add(labeled("Name of Trip:", tripNameLabel));
        left.add(labeled("City:", cityLabel));
        left.add(labeled("Date:", dateLabel));

        left.add(new JLabel("Attractions:"));
        left.add(new JScrollPane(attractionsArea));

        // ---- Right panel ----
        final JPanel right = new JPanel();
        right.setLayout(new BoxLayout(right, BoxLayout.Y_AXIS));

        right.add(new JLabel("Flight:"));
        right.add(new JScrollPane(flightArea));

        right.add(Box.createVerticalStrut(10));

        right.add(new JLabel("Hotel:"));
        right.add(new JScrollPane(hotelArea));

        // Top grid: left & right columns
        final JPanel top = new JPanel(new GridLayout(1, 2, 10, 0));
        top.add(left);
        top.add(right);

        // Bottom panel: Gemini + Back
        final JPanel bottom = new JPanel();
        bottom.setLayout(new BoxLayout(bottom, BoxLayout.Y_AXIS));

        final JPanel geminiPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        geminiPanel.add(new JLabel("Ask Gemini:"));
        geminiPanel.add(geminiInputField);

        final JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(backButton);

        backButton.addActionListener(this);

        completeTripButton.addActionListener(e -> {
            if (completeTripController != null) {
                completeTripController.completeTrip(tripViewModel.getCurrentTripId());
            } else {
                JOptionPane.showMessageDialog(this, "CompleteTripController not set!");
            }
        });

        bottom.add(geminiPanel);
        bottom.add(buttonPanel);

        // Add to main layout
        add(top, BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);
    }

    /**
     * Helper method for "Label: value" rows.
     * @param title String title of the desired panel
     * @param value JLabel representing the information desired in the panel
     * @return a JPanel with the title and JLabel connected
     */
    private JPanel labeled(String title, JLabel value) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
        p.add(new JLabel(title));
        p.add(value);
        return p;
    }

    /** Handle button clicks (minimalism). */
    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source == backButton) {
            tripController.back();
        }
    }

    /** Updates labels when ViewModel state changes. */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        TripState s = (TripState) evt.getNewValue();
        tripNameLabel.setText(s.getTripName());
        cityLabel.setText(s.getCity());
        dateLabel.setText(s.getDate());

        attractionsArea.setText(s.getAttractions());
        flightArea.setText(s.getFlightDetails());
        hotelArea.setText(s.getHotelDetails());
        geminiInputField.setText(s.getGeminiInput());
    }

    public void setCompleteTripController(CompleteTripController controller) {
        this.completeTripController = controller;
    }

    public void setTripController(TripController tripController) {
        this.tripController = tripController;
    }
}
