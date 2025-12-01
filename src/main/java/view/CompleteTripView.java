package gui;

import javax.swing.*;
import java.awt.*;

/***** ADDED: import the correct ViewModel *****/
import interface_adapter.logged_in.LoggedInViewModel;

import interface_adapter.complete_trip.CompleteTripController;

public class CompleteTripView extends JFrame {

    private final CompleteTripController controller;

    /***** ADDED: You MUST store a ViewModel reference *****/
    private final LoggedInViewModel viewModel;

    private final JTextField tripIdField = new JTextField(20);
    private final JButton completeTripButton = new JButton("Complete Trip");
    private final JLabel statusLabel = new JLabel();

    /***** CHANGED: constructor now takes both controller AND viewmodel *****/
    public CompleteTripView(CompleteTripController controller, LoggedInViewModel viewModel) {
        this.controller = controller;
        this.viewModel = viewModel;   // <--- REQUIRED

        setTitle("Complete Trip");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout());
        add(new JLabel("Trip ID:"));
        add(tripIdField);
        add(completeTripButton);
        add(statusLabel);

        completeTripButton.addActionListener(e -> {
            String tripId = tripIdField.getText();
            controller.completeTrip(tripId);
        });

        /***** FIXED: viewModel now exists *****/
        viewModel.addPropertyChangeListener(evt -> {
            if ("tripStatus".equals(evt.getPropertyName())) {
                statusLabel.setText("Status: " + viewModel.getState().getTripStatus());
            } else if ("errorMessage".equals(evt.getPropertyName())) {
                String err = viewModel.getState().getErrorMessage();
                if (err != null) {
                    JOptionPane.showMessageDialog(this, err, "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        pack();
        setLocationRelativeTo(null);
    }
}