package view;

import java.awt.FlowLayout;
import java.awt.Window;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import interface_adapter.hotel_search.HotelSearchController;
import interface_adapter.hotel_search.HotelSearchState;
import interface_adapter.hotel_search.HotelSearchViewModel;

public class HotelSearchDialog extends JDialog implements PropertyChangeListener {

    private final HotelSearchViewModel viewModel;
    private final HotelSearchController controller;

    private final JTextField queryField = new JTextField("Bali Resorts", 20);
    private final JTextField checkInField = new JTextField("2025-11-22", 10);
    private final JTextField checkOutField = new JTextField("2025-11-23", 10);
    private final JTextArea resultArea = new JTextArea(10, 40);

    public HotelSearchDialog(Window parent,
                             HotelSearchViewModel viewModel,
                             HotelSearchController controller) {
        super(parent, "Hotel Search", ModalityType.APPLICATION_MODAL);
        this.viewModel = viewModel;
        this.controller = controller;

        viewModel.addPropertyChangeListener(this);

        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        p.add(row("Query:", queryField));
        p.add(row("Check-in:", checkInField));
        p.add(row("Check-out:", checkOutField));

        resultArea.setEditable(false);
        resultArea.setLineWrap(true);
        resultArea.setWrapStyleWord(true);
        p.add(Box.createVerticalStrut(8));
        p.add(new JScrollPane(resultArea));

        JButton search = new JButton("Search");
        JButton close = new JButton("Close");
        JPanel btns = new JPanel();
        btns.add(search);
        btns.add(close);
        p.add(btns);

        search.addActionListener(e ->
                controller.execute(
                        queryField.getText(),
                        checkInField.getText(),
                        checkOutField.getText())
        );
        close.addActionListener(e -> dispose());

        setContentPane(p);
        pack();
        setLocationRelativeTo(parent);
    }

    private JPanel row(String label, JComponent field) {
        JPanel r = new JPanel(new FlowLayout(FlowLayout.LEFT));
        r.add(new JLabel(label));
        r.add(field);
        return r;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("state".equals(evt.getPropertyName())) {
            HotelSearchState s = (HotelSearchState) evt.getNewValue();
            resultArea.setText(s.getSummary());
        }
    }
}