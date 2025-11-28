package view;

import javax.swing.*;
import javax.swing.text.DateFormatter;
import java.awt.*;
import java.awt.event.*;
import java.text.*;
import java.time.*;
import java.time.format.*;

import interface_adapter.create_new_trip.CreateNewTripController;
import interface_adapter.create_new_trip.CreateNewTripViewModel;

public class CreateNewTripView extends JPanel implements ActionListener {
    private final String viewName = "create new trip";
    private final CreateNewTripViewModel viewModel;
    private CreateNewTripController createNewTripController;

    private final JTextField fromField = new JTextField();
    private final JTextField toField = new JTextField();

    // Date fields with formatting
    private final JFormattedTextField startDateField;
    private final JFormattedTextField endDateField;

    private final JButton generateButton = new JButton("Create Trip");
    private final JButton backButton = new JButton("Back");

    // Formatter for yyyy-MM-dd
    private static final String DATE_PATTERN = "yyyy-MM-dd";
    private static final DateTimeFormatter LOCAL_DATE_FORMATTER =
            DateTimeFormatter.ofPattern(DATE_PATTERN);
    private static final String DATE_PLACEHOLDER = "yyyy-MM-dd";

    public CreateNewTripView(CreateNewTripViewModel viewModel) {
        this.viewModel = viewModel;

        // Create Swing date formatter
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_PATTERN);
        dateFormat.setLenient(false);
        DateFormatter dateFormatter = new DateFormatter(dateFormat);

        startDateField = new JFormattedTextField(dateFormatter);
        endDateField = new JFormattedTextField(dateFormatter);

        addPlaceholder(startDateField);
        addPlaceholder(endDateField);

        // Main layout similar to TripListView
        this.setLayout(new BorderLayout(20, 20));
        this.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Title
        JLabel title = new JLabel("Create New Trip", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 22));

        // Back Button
        backButton.addActionListener(this);


        // Create Trip button
        generateButton.addActionListener(e -> {
            if (createNewTripController != null) {
                createNewTripController.execute(
                        getFromText(),
                        getToText(),
                        getStartDateText()
                );
            }
        });

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

        // Make text fields same size
        int fieldWidth = 300;
        int fieldHeight = 28;
        Dimension fieldSize = new Dimension(fieldWidth, fieldHeight);
        fromField.setPreferredSize(fieldSize);
        toField.setPreferredSize(fieldSize);
        startDateField.setPreferredSize(fieldSize);
        endDateField.setPreferredSize(fieldSize);

        // Labels with lined up fields
        JLabel fromLabel = new JLabel("From:");
        JLabel toLabel = new JLabel("To:");
        JLabel startDateLabel = new JLabel("Start date:");
        JLabel endDateLabel = new JLabel("End date:");

        Dimension labelSize = new Dimension(80, 20);
        fromLabel.setPreferredSize(labelSize);
        toLabel.setPreferredSize(labelSize);
        startDateLabel.setPreferredSize(labelSize);
        endDateLabel.setPreferredSize(labelSize);

        // Row 1: From
        JPanel fromRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        fromRow.add(fromLabel);
        fromRow.add(fromField);

        // Row 2: To
        JPanel toRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        toRow.add(toLabel);
        toRow.add(toField);

        // Row 3: Start date
        JPanel startDateRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        startDateRow.add(startDateLabel);
        startDateRow.add(startDateField);

        // Row 4: End date
        JPanel endDateRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        endDateRow.add(endDateLabel);
        endDateRow.add(endDateField);

        // Row 5: Button
        JPanel buttonRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 15));
        buttonRow.add(generateButton);

        // Add rows to center panel
        centerPanel.add(fromRow);
        centerPanel.add(toRow);
        centerPanel.add(startDateRow);
        centerPanel.add(endDateRow);
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

    // Reset all fields to a "fresh" state (call this when the view is shown)
    public void resetFields() {
        fromField.setText("");
        toField.setText("");
        applyPlaceholder(startDateField);
        applyPlaceholder(endDateField);
    }

    // Getters the controller can use

    public String getFromText() {
        return fromField.getText().trim();
    }

    public String getToText() {
        return toField.getText().trim();
    }

    public String getStartDateText() {
        String text = startDateField.getText().trim();
        return DATE_PLACEHOLDER.equals(text) ? "" : text;
    }

    public String getEndDateText() {
        String text = endDateField.getText().trim();
        return DATE_PLACEHOLDER.equals(text) ? "" : text;
    }

    public LocalDate getStartDate() throws DateTimeParseException {
        String text = getStartDateText();
        if (text.isEmpty()) {
            return null;
        }
        return LocalDate.parse(text, LOCAL_DATE_FORMATTER);
    }

    public LocalDate getEndDate() throws DateTimeParseException {
        String text = getEndDateText();
        if (text.isEmpty()) {
            return null;
        }
        return LocalDate.parse(text, LOCAL_DATE_FORMATTER);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();

        if (source == backButton && createNewTripController != null) {
            createNewTripController.goBack();
        }
        else if (source == generateButton) {

        }

    }


    // Apply placeholder style and text
    private void applyPlaceholder(JTextField field) {
        field.setForeground(Color.GRAY);
        field.setText(DATE_PLACEHOLDER);
    }

    // Helper method to add placeholder behavior to date fields
    private void addPlaceholder(JTextField field) {
        applyPlaceholder(field);

        field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (field.getText().equals(DATE_PLACEHOLDER)) {
                    field.setText("");
                    field.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (field.getText().isEmpty()) {
                    applyPlaceholder(field);
                }
            }
        });
    }
}
