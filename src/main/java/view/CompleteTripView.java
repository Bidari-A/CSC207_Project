package gui;

import javax.swing.*;
import java.awt.*;
import interface_adapter.complete_trip.CompleteTripController;
import interface_adapter.complete_trip.CompleteTripViewModel;

public class CompleteTripView extends JFrame {
    private final CompleteTripController controller;
    private final CompleteTripViewModel viewModel;

    private final JTextField tripIdField = new JTextField(20);
    private final JButton completeTripButton = new JButton("Complete Trip");
    private final JLabel statusLabel = new JLabel();

    public CompleteTripView(CompleteTripController controller, CompleteTripViewModel viewModel) {
        this.controller = controller;
        this.viewModel = viewModel;

        setTitle("Complete Trip");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout());
        add(new JLabel("Trip ID:"));
        add(tripIdField);
        add(completeTripButton);
        add(statusLabel);

        // Where your original snippet belongs:
        completeTripButton.addActionListener(e -> {
            String tripId = tripIdField.getText();
            controller.completeTrip(tripId);
        });

        // Update UI when ViewModel changes:
        viewModel.addPropertyChangeListener(evt -> {
            if ("tripStatus".equals(evt.getPropertyName())) {
                statusLabel.setText("Status: " + viewModel.getTripStatus());
            } else if ("errorMessage".equals(evt.getPropertyName())) {
                String err = viewModel.getErrorMessage();
                if (err != null) {
                    JOptionPane.showMessageDialog(this, err, "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else if ("lastCompletedTripId".equals(evt.getPropertyName())) {
                // optional behavior
            }
        });

        pack();
        setLocationRelativeTo(null);
    }
}