package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;

import entity.Trip;
import interface_adapter.trip.TripController;
import interface_adapter.trip_list.TripListController;
import interface_adapter.trip_list.TripListState;
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

        // Trip list panel (scrollable)
        tripListPanel = new JPanel();
        tripListPanel.setLayout(new BoxLayout(tripListPanel, BoxLayout.Y_AXIS));
        tripListPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JScrollPane scrollPane = new JScrollPane(tripListPanel);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        // Center
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        centerPanel.add(errorLabel, BorderLayout.NORTH);

        // Main layout
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

        // Back button
        if (source == backButton) {
            tripListController.goBack();
        }

        // Details button
        if (cmd.startsWith("DETAILS_")) {
            String tripId = cmd.substring("DETAILS_".length());
            TripListState state = tripListViewModel.getState();
            String username = state.getUsername();
            tripController.execute(username, viewName, tripId);
        }

        // ★★★ DELETE button — NEW ★★★
        if (cmd.startsWith("DELETE_")) {
            String tripId = cmd.substring("DELETE_".length());
            TripListState state = tripListViewModel.getState();
            String username = state.getUsername();

            // Call the DeleteTrip use case
            tripListController.deleteTrip(username, tripId);
        }
    }

    /**
     * Updates the view when TripListState changes.
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("state".equals(evt.getPropertyName())) {
            TripListState state = (TripListState) evt.getNewValue();

            // Clear error
            errorLabel.setText("");

            // Clear existing trip list
            tripListPanel.removeAll();

            // Show error
            if (state.getError() != null) {
                errorLabel.setText(state.getError());
            }

            // Show trips
            else if (state.getTrips() != null) {
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
        JLabel tripNameLabel = new JLabel(trip.getTripName());
        tripNameLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        rowPanel.add(tripNameLabel, BorderLayout.WEST);

        // Buttons panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));

        JButton detailsButton = new JButton("Details");
        detailsButton.setActionCommand("DETAILS_" + trip.getTripId());
        detailsButton.addActionListener(this);

        JButton deleteButton = new JButton("Delete");
        deleteButton.setActionCommand("DELETE_" + trip.getTripId());
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

    public TripListController getTripListController() {
        return tripListController;
    }

    public void setTripController(TripController tripController) {
        this.tripController = tripController;
    }
}