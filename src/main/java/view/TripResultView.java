package view;

import interface_adapter.create_trip_result.TripResultViewModel;
import interface_adapter.create_trip_result.TripResultState;
import interface_adapter.create_new_trip.CreateNewTripController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class TripResultView extends JPanel implements
        PropertyChangeListener, ActionListener {

    private final String viewName = "trip result";
    private final TripResultViewModel viewModel;
    private CreateNewTripController createNewTripController;


    private final JLabel tripNameLabel = new JLabel();
    private final JLabel fromLabel = new JLabel();
    private final JLabel toLabel = new JLabel();
    private final JLabel startDateLabel = new JLabel();
    private final JLabel endDateLabel = new JLabel();
    private final JButton backButton = new JButton("Back");

    private final JTextArea itineraryArea = new JTextArea(10, 40);

    public TripResultView(TripResultViewModel viewModel) {
        this.viewModel = viewModel;
        this.viewModel.addPropertyChangeListener(this);

        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Title
        JLabel title = new JLabel("Trip Details", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 22));
        // add(title, BorderLayout.NORTH);

        // Top panel: back button on the left, title centered
        JPanel topPanel = new JPanel(new BorderLayout());

        JPanel leftWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 5));
        leftWrapper.setOpaque(false);
        leftWrapper.add(backButton);
        backButton.addActionListener(this);

        JPanel titleWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        titleWrapper.setOpaque(false);
        titleWrapper.add(title);

        topPanel.add(leftWrapper, BorderLayout.WEST);
        topPanel.add(titleWrapper, BorderLayout.CENTER);

        add(topPanel, BorderLayout.NORTH);

        // Info panel (top / center)
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));

        infoPanel.add(makeRow("Trip Name: ", tripNameLabel));
        infoPanel.add(makeRow("From: ", fromLabel));
        infoPanel.add(makeRow("To: ", toLabel));
        infoPanel.add(makeRow("Start Date: ", startDateLabel));
        infoPanel.add(makeRow("End Date: ", endDateLabel));

        add(infoPanel, BorderLayout.CENTER);

        // Itinerary area
        itineraryArea.setEditable(false);
        itineraryArea.setLineWrap(true);
        itineraryArea.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(itineraryArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Itinerary"));
        add(scrollPane, BorderLayout.SOUTH);
    }

    private JPanel makeRow(String labelText, JLabel valueLabel) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT));
        row.add(new JLabel(labelText));
        row.add(valueLabel);
        return row;
    }

    public String getViewName() {
        return viewName;
    }

    public void setCreateNewTripController(CreateNewTripController controller) {
        this.createNewTripController = controller;
    }


    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        // Your ViewModel only calls firePropertyChange("state"),
        // so we ignore anything else.
        if (!"state".equals(evt.getPropertyName())) {
            return;
        }

        TripResultState state = viewModel.getState();

        tripNameLabel.setText(state.getTripName());
        fromLabel.setText(state.getFrom());
        toLabel.setText(state.getTo());
        startDateLabel.setText(state.getStartDate());
        endDateLabel.setText(state.getEndDate());
        itineraryArea.setText(state.getItinerary());
        itineraryArea.setCaretPosition(0); // scroll to top
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();

        System.out.println("TripResultView: actionPerformed fired");
        System.out.println("TripResultView: source == backButton? " + (source == backButton));
        System.out.println("TripResultView: controller is " +
                (createNewTripController == null ? "NULL" : "NOT NULL"));

        if (source == backButton && createNewTripController != null) {
            System.out.println("TripResultView: calling controller.backFromResult()");
            createNewTripController.backFromResult();
        }
    }


}
