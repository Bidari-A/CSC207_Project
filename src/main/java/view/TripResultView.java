package view;

import interface_adapter.create_trip_result.TripResultViewModel;
import interface_adapter.create_trip_result.TripResultState;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class TripResultView extends JPanel implements PropertyChangeListener {

    private final String viewName = "trip result";
    private final TripResultViewModel viewModel;

    private final JLabel tripNameLabel = new JLabel();
    private final JLabel fromLabel = new JLabel();
    private final JLabel toLabel = new JLabel();
    private final JLabel startDateLabel = new JLabel();
    private final JLabel endDateLabel = new JLabel();

    private final JTextArea itineraryArea = new JTextArea(10, 40);

    public TripResultView(TripResultViewModel viewModel) {
        this.viewModel = viewModel;
        this.viewModel.addPropertyChangeListener(this);

        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Title
        JLabel title = new JLabel("Trip Details", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 22));
        add(title, BorderLayout.NORTH);

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
}
