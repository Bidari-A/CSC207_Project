package view;
import javax.swing.*;
import java.awt.*;

import interface_adapter.create_new_trip.CreateNewTripViewModel;
import interface_adapter.create_new_trip.CreateNewTripState;


public class CreateNewTripView extends JPanel {
    private final String viewName = "create new trip";
    private final CreateNewTripViewModel viewModel;

    private final JTextField fromField = new JTextField(15);
    private final JTextField toField = new JTextField(15);
    private final JTextField dateField = new JTextField(15);
    private final JButton generateButton = new JButton("Create Trip");

    public CreateNewTripView(CreateNewTripViewModel viewModel) {
        this.viewModel = viewModel;

        final JLabel title = new JLabel("Create New Trip");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        this.setLayout(new GridLayout(5, 2));

        this.add(title);

        this.add(new JLabel("From:"));
        this.add(fromField);

        this.add(new JLabel("To:"));
        this.add(toField);

        this.add(new JLabel("Date:"));
        this.add(dateField);

        this.add(generateButton);
    }

    public String getViewName() {
        return viewName;
    }
}
