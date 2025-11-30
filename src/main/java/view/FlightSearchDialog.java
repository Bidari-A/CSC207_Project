package view;

import interface_adapter.flight_search.FlightSearchController;
import interface_adapter.flight_search.FlightSearchState;
import interface_adapter.flight_search.FlightSearchViewModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

// We are using JDialog instead of JPanel!! This gives more flexibility.
public class FlightSearchDialog extends JDialog implements PropertyChangeListener {
    // WE NEED TO ADD CONTROLLER AND VIEW MODEL!
    // CONTROLLER is used, when button is clicked,
    // we sent the input to controller to use case interactor to get new JSON!
    // VIEWMODEL is used
    private final FlightSearchViewModel viewModel;
    private final FlightSearchController controller;


    // STEP 1 Construct the JPanels (Example)
    private final JTextField fromField =  new JTextField("YYZ", 6);
    private final JTextField toField =  new JTextField("BUD", 6);
    private final JTextField outField =  new JTextField("2025-11-18", 10);
    private final JTextField returnField =  new JTextField("2025-11-24", 10);
    private final JTextArea resultArea =  new JTextArea(6, 30);


    //STEP 2 Let's set the UI
    public FlightSearchDialog(Window parent, FlightSearchViewModel viewModel, FlightSearchController controller) {
        super(parent, "Flight Search", ModalityType.APPLICATION_MODAL); // Title of this window will be Flight Search
        this.viewModel = viewModel;
        this.controller = controller;
        this.viewModel.addPropertyChangeListener(this);

        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBorder(BorderFactory.createEmptyBorder(8,8,8,8));
        p.add(new JLabel("Please use airport code. Date format: YYYY-MM-DD"));
        p.add(row("From:", fromField));
        p.add(row("To:", toField));
        p.add(row("Outbound:", outField));
        p.add(row("Return:", returnField));
        p.add(Box.createVerticalStrut(8)); // This will actually add some space between components so ermm it dont look too messy
        p.add(new JScrollPane(resultArea));

        JButton search = new JButton("Search");
        JButton close = new JButton("Close");

        JPanel btns = new JPanel();
        btns.add(search);
        btns.add(close);
        p.add(btns);

        // Arrow function... refresh your memory :)
        // WHEN SEARCH IS CLICKED, we use values from text fields as arguments
        // and execute them with the controller! (We know its existence since we take it in our constructor)
        // WHEN CLOSE IS CLICKED, we call DISPOSE() which is built in function for JPanel

        search.addActionListener(e ->
                controller.execute(fromField.getText(), toField.getText(), outField.getText(), returnField.getText()
                ));
        close.addActionListener(e -> {
            dispose();
        });

        setContentPane(p);
        pack();
        setLocationRelativeTo(parent);
    }

    private JPanel row(String label, JComponent field) {
        JPanel r = new JPanel(new FlowLayout(FlowLayout.LEFT));
        r.add(new JLabel(label)); // we need to make a new one since we are label is String
        r.add(field); // this is already a JComponent (JTextField)
        return r;
    }

    // Just copy the CA Lab example :) this is too complicated for my brain to understand. I don't know how does the
    // fire property works
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("state".equals(evt.getPropertyName())) {
            FlightSearchState s = (FlightSearchState) evt.getNewValue();
            resultArea.setText(s.getSummary());
        }
    }
}