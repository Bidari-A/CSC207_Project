package view;

import javax.swing.*;
import java.awt.*;

import interface_adapter.create_new_trip.CreateNewTripViewModel;

public class CreateNewTripView extends JPanel {
    private final String viewName = "create new trip";
    private final CreateNewTripViewModel viewModel;

    private final JTextField fromField = new JTextField(15);
    private final JTextField toField = new JTextField(15);
    private final JTextField dateField = new JTextField(15);
    private final JButton generateButton = new JButton("Create Trip");

    public CreateNewTripView(CreateNewTripViewModel viewModel) {
        this.viewModel = viewModel;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        final JLabel title = new JLabel("Create New Trip");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Row 1: From
        JPanel fromRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
        fromRow.add(new JLabel("From:"));
        fromRow.add(fromField);

        // Row 2: To
        JPanel toRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
        toRow.add(new JLabel("To:"));
        toRow.add(toField);

        // Row 3: Date
        JPanel dateRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
        dateRow.add(new JLabel("Date:"));
        dateRow.add(dateField);

        // Row 4: Button
        JPanel buttonRow = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonRow.add(generateButton);

        add(title);
        add(fromRow);
        add(toRow);
        add(dateRow);
        add(buttonRow);
    }

    public String getViewName() {
        return viewName;
    }
}
