package view;

import interface_adapter.trip.TripController;
import interface_adapter.trip_list.TripListController;
import interface_adapter.trip_list.TripListState;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import entity.Trip;
import interface_adapter.trip_list.TripListViewModel;

/**
 * This View is for displaying the list of trips.
 */

public class TripListView extends JPanel implements ActionListener, PropertyChangeListener {

    private final String viewName = "trip list";
    private final TripListViewModel tripListViewModel;
    private TripListController tripListController;
    private TripController tripController;

    private final JButton backButton;
    private final JPanel tripListPanel;
    private final JLabel title;
    private final JLabel errorLabel;

    public TripListView(TripListViewModel tripListViewModel) {
        this.tripListViewModel = tripListViewModel;
        this.tripListViewModel.addPropertyChangeListener(this);

        // Title
        title = new JLabel("My Trips", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD,22));

        // Back Button
        backButton = new JButton("Back");
        backButton.addActionListener(this);

        // Top panel with back button and title
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(title, BorderLayout.CENTER);
        topPanel.add(backButton, BorderLayout.WEST);

        // Error label
        errorLabel = new JLabel();
        errorLabel.setForeground(Color.RED);
        errorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        errorLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Trip list panel (for scrolling)
        tripListPanel = new JPanel();
        tripListPanel.setLayout(new BoxLayout(tripListPanel, BoxLayout.Y_AXIS));
        tripListPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JScrollPane scrollPane = new JScrollPane(tripListPanel);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        // Center panel with error label and scroll pane
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        centerPanel.add(errorLabel, BorderLayout.NORTH);

        // Main Layout
        this.setLayout(new BorderLayout(20,20));
        this.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        this.add(topPanel, BorderLayout.NORTH);
        this.add(centerPanel, BorderLayout.CENTER);
    }

    /**
     * Handles button click events.
     */
    @Override
    public void actionPerformed(ActionEvent evt) {
        Object source = evt.getSource();
        String cmd = evt.getActionCommand();

        if (source == backButton) {
            // Navigate back to logged in view
            tripListController.goBack();
        }
        if (cmd.startsWith("DETAILS_")) {
            String tripId = cmd.substring("DETAILS_".length());
            final TripListState tripListState = tripListViewModel.getState();
            final String username = tripListState.getUsername();

            tripController.execute(username, viewName, tripId);

        }
    }

    /**
     * Updates the view when TripListState changes.
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("state".equals(evt.getPropertyName())) {
            final TripListState state = (TripListState) evt.getNewValue();

            // Clear error
            errorLabel.setText("");

            // Clear existing trip list
            tripListPanel.removeAll();

            // Display error if any
            if (state.getError() != null) {
                errorLabel.setText(state.getError());
            } else if (state.getTrips() != null) {
                // Display trips
                List<Trip> trips = state.getTrips();

                if (trips.isEmpty()) {
                    JLabel noTripsLabel = new JLabel("No trips found. Create a new trip to get started!");
                    noTripsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                    noTripsLabel.setFont(new Font("Arial", Font.ITALIC, 14));
                    tripListPanel.add(noTripsLabel);
                } else {
                    for (Trip trip : trips) {
                        tripListPanel.add(createTripRow(trip));
                        tripListPanel.add(Box.createVerticalStrut(10));
                    }
                }
            }

            // Refresh the panel
            tripListPanel.revalidate();
            tripListPanel.repaint();
        }
    }

    /**
     * Creates a row panel for a single trip with its name and action buttons.
     */
    private JPanel createTripRow(Trip trip) {
        JPanel rowPanel = new JPanel(new BorderLayout());
        rowPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY, 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        rowPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        rowPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Trip name label
        JLabel tripNameLabel = new JLabel(trip.getName());
        tripNameLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        rowPanel.add(tripNameLabel, BorderLayout.WEST);

        // Buttons panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));

        JButton detailsButton = new JButton("Details");
        detailsButton.setActionCommand("DETAILS_" + trip.getName());
        detailsButton.addActionListener(this);

        JButton deleteButton = new JButton("Delete");
        deleteButton.setActionCommand("DELETE_" + trip.getName());
        deleteButton.addActionListener(this);
        deleteButton.setForeground(Color.RED);

        buttonsPanel.add(detailsButton);
        buttonsPanel.add(deleteButton);

        rowPanel.add(buttonsPanel, BorderLayout.EAST);

        return rowPanel;
    }

    public String getViewName() {
        return viewName;
    }

    public void setTripListController(TripListController tripListController) {
        this.tripListController = tripListController;
    }

    public void setTripController(TripController tripController) {
        this.tripController = tripController;
    }
}