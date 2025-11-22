package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import interface_adapter.create_new_trip.CreateNewTripController;
import interface_adapter.create_new_trip.CreateNewTripViewModel;
import interface_adapter.trip_list.TripListController;

public class CreateNewTripView extends JPanel implements ActionListener {
    private final String viewName = "create new trip";
    private final CreateNewTripViewModel viewModel;
    private CreateNewTripController createNewTripController;


    private final JTextField fromField = new JTextField();
    private final JTextField toField = new JTextField();
    private final JTextField dateField = new JTextField();

    private final JButton generateButton = new JButton("Create Trip");
    JButton backButton = new JButton("Back");

    public CreateNewTripView(CreateNewTripViewModel viewModel) {
        this.viewModel = viewModel;
        //this.viewModel.addPropertyChangeListener(this);

        // Main layout similar to TripListView
        this.setLayout(new BorderLayout(20, 20));
        this.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Title
        JLabel title = new JLabel("Create New Trip", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 22));

        // Back Button
        backButton.addActionListener(this);

        //create button
        generateButton.addActionListener(this);

        // Top panel: back button slightly above, title centered
        JPanel topPanel = new JPanel(new BorderLayout());

        JPanel leftWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 5));
        leftWrapper.setOpaque(false);
        leftWrapper.add(backButton);

        JPanel titleWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        titleWrapper.setOpaque(false);
        titleWrapper.add(title);

        topPanel.add(leftWrapper, BorderLayout.WEST);
        topPanel.add(titleWrapper, BorderLayout.CENTER);

        // Center panel for form fields
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));

        // make text fields same size
        int fieldWidth = 300;
        int fieldHeight = 28;
        Dimension fieldSize = new Dimension(fieldWidth, fieldHeight);
        fromField.setPreferredSize(fieldSize);
        toField.setPreferredSize(fieldSize);
        dateField.setPreferredSize(fieldSize);

        // labels with lined up fields
        JLabel fromLabel = new JLabel("From:");
        JLabel toLabel = new JLabel("To:");
        JLabel dateLabel = new JLabel("Date:");

        Dimension labelSize = new Dimension(60, 20);
        fromLabel.setPreferredSize(labelSize);
        toLabel.setPreferredSize(labelSize);
        dateLabel.setPreferredSize(labelSize);

        // Row 1: From
        JPanel fromRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        fromRow.add(fromLabel);
        fromRow.add(fromField);

        // Row 2: To
        JPanel toRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        toRow.add(toLabel);
        toRow.add(toField);

        // Row 3: Date
        JPanel dateRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        dateRow.add(dateLabel);
        dateRow.add(dateField);

        // Row 4: Button
        JPanel buttonRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 15));
        buttonRow.add(generateButton);

        // Add rows to center panel
        centerPanel.add(fromRow);
        centerPanel.add(toRow);
        centerPanel.add(dateRow);
        centerPanel.add(Box.createVerticalStrut(10));
        centerPanel.add(buttonRow);

        this.add(topPanel, BorderLayout.NORTH);
        this.add(centerPanel, BorderLayout.CENTER);
    }

    public String getViewName() {
        return viewName;
    }


    public void setCreateNewTripController(CreateNewTripController controller) {
        this.createNewTripController = controller;
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();

        if (source == backButton) {
            //System.out.println("Back button clicked in CreateNewTripView");
            // Navigate back to logged in view
            createNewTripController.goBack();
        } else if (source == generateButton) {
            //call the controller to create the trip
            createNewTripController.execute(
                    fromField.getText(),
                    toField.getText(),
                    dateField.getText()
            );
        }
}}
