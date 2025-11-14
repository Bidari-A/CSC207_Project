package view;

import interface_adapter.logged_in.LoggedInState;
import interface_adapter.logged_in.LoggedInViewModel;
import interface_adapter.logout.LogoutController;
import interface_adapter.trip_list.TripListController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * The View for when the user is logged into the program.
 */
public class LoggedInView extends JPanel implements ActionListener, PropertyChangeListener {

    private final String viewName = "logged in";
    private final LoggedInViewModel loggedInViewModel;
    private TripListController tripListController;

    // TODO to be assigned later..
    // tripHistoryController
    // detailController
    // deleteTripController
    // completeTripController
    // newTripController

    private final JLabel tripName;
    private final JLabel cityName;
    private final JLabel date;
    private final JLabel username;

    private final JButton logOut;
    private final JButton tripListButton;
    private final JButton newTripButton;
    private final JButton detailsButton;
    private final JButton deleteButton;
    private final JButton completeButton;
    private LogoutController logoutController;

    public LoggedInView(LoggedInViewModel loggedInViewModel) {
        this.loggedInViewModel = loggedInViewModel;
        this.loggedInViewModel.addPropertyChangeListener(this);

        final JLabel usernameInfo = new JLabel("Currently logged in as: ");
        username = new JLabel();

        JPanel topPanel = new JPanel(new BorderLayout());
        logOut = new JButton("Log Out");
        tripListButton = new JButton("Trip List");

        JLabel title = new JLabel("Dashboard", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 22));

        topPanel.add(logOut, BorderLayout.WEST);
        topPanel.add(title, BorderLayout.CENTER);
        topPanel.add(tripListButton, BorderLayout.EAST);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel usernameHolderPanel = new JPanel();
        usernameHolderPanel.setLayout(new BoxLayout(usernameHolderPanel, BoxLayout.X_AXIS));
        usernameHolderPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        usernameHolderPanel.add(usernameInfo);
        usernameHolderPanel.add(username);

        JLabel lastTrip = new JLabel("Last Trip Created:");
        JLabel tripNameInfo = new JLabel("Name of Trip: ");
        JLabel cityNameInfo = new JLabel("City: ");
        JLabel dateInfo = new JLabel("Date: ");

        // TODO EDIT LATER
        tripName = new JLabel("Holder Trip");
        cityName = new JLabel("Holder City Name");
        date = new JLabel("Holder Date");

        Font infoFont = new Font("Arial", Font.PLAIN, 14);
        tripNameInfo.setFont(infoFont);
        cityNameInfo.setFont(infoFont);
        dateInfo.setFont(infoFont);
        lastTrip.setFont(infoFont);
        tripName.setFont(infoFont);
        cityName.setFont(infoFont);
        date.setFont(infoFont);

        lastTrip.setAlignmentX(Component.CENTER_ALIGNMENT);


        JPanel topPanel_1 = new JPanel();
        topPanel_1.setLayout(new BoxLayout(topPanel_1, BoxLayout.X_AXIS));
        topPanel_1.setAlignmentX(Component.CENTER_ALIGNMENT);
        topPanel_1.add(tripNameInfo);
        topPanel_1.add(tripName);

        JPanel topPanel_2 = new JPanel();
        topPanel_2.setLayout(new BoxLayout(topPanel_2, BoxLayout.X_AXIS));
        topPanel_2.setAlignmentX(Component.CENTER_ALIGNMENT);
        topPanel_2.add(cityNameInfo);
        topPanel_2.add(cityName);

        JPanel topPanel_3 = new JPanel();
        topPanel_3.setLayout(new BoxLayout(topPanel_3, BoxLayout.X_AXIS));
        topPanel_3.setAlignmentX(Component.CENTER_ALIGNMENT);
        topPanel_3.add(dateInfo);
        topPanel_3.add(date);

        centerPanel.add(usernameHolderPanel);
        centerPanel.add(Box.createVerticalStrut(10));
        centerPanel.add(lastTrip);
        centerPanel.add(Box.createVerticalStrut(10));
        centerPanel.add(topPanel_1);
        centerPanel.add(Box.createVerticalStrut(5));
        centerPanel.add(topPanel_2);
        centerPanel.add(Box.createVerticalStrut(5));
        centerPanel.add(topPanel_3);
        centerPanel.add(Box.createVerticalStrut(25));

        JPanel actionRow = new JPanel();
        actionRow.setLayout(new BoxLayout(actionRow, BoxLayout.X_AXIS));
        actionRow.setAlignmentX(Component.CENTER_ALIGNMENT);

        detailsButton = new JButton("Details");
        deleteButton = new JButton("Delete");
        completeButton = new JButton("Complete");

        actionRow.add(Box.createHorizontalGlue());
        actionRow.add(detailsButton);
        actionRow.add(Box.createHorizontalStrut(15));
        actionRow.add(deleteButton);
        actionRow.add(Box.createHorizontalStrut(15));
        actionRow.add(completeButton);
        actionRow.add(Box.createHorizontalGlue());

        centerPanel.add(actionRow);
        centerPanel.add(Box.createVerticalStrut(30));

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        newTripButton = new JButton("New Trip");
        bottomPanel.add(newTripButton);

        this.setLayout(new BorderLayout(20, 20));
        this.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        this.add(topPanel, BorderLayout.NORTH);
        this.add(centerPanel, BorderLayout.CENTER);
        this.add(bottomPanel, BorderLayout.SOUTH);

        logOut.addActionListener(this);
        tripListButton.addActionListener(this);
        newTripButton.addActionListener(this);
        detailsButton.addActionListener(this);
        deleteButton.addActionListener(this);
        completeButton.addActionListener(this);
    }

    /**
     * Handles button click events.
     */
    @Override
    public void actionPerformed(ActionEvent evt) {
        Object source = evt.getSource();

        if (source == logOut && logoutController != null) {
            logoutController.execute();
        } else if (source == tripListButton) {
            // Get the current username from the logged in state
            LoggedInState state = loggedInViewModel.getState();
            String username = state.getUsername();
            
            // Load trips (navigation will be handled by TripListPresenter)
            if (tripListController != null && username != null) {
                tripListController.execute(username);
            }
        } else if (source == newTripButton) {
            System.out.println("New Trip clicked");
        } else if (source == detailsButton) {
            System.out.println("Details clicked");
        } else if (source == deleteButton) {
            System.out.println("Delete clicked");
        } else if (source == completeButton) {
            System.out.println("Complete clicked");
        }
    }

    /**
     * Updates the labels when LoggedInState changes.
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("state".equals(evt.getPropertyName())) {
            final LoggedInState state = (LoggedInState) evt.getNewValue();
            username.setText(state.getUsername());
        }
    }

    public String getViewName() {
        return viewName;
    }

    public void setLogoutController(LogoutController logoutController) {
        this.logoutController = logoutController;
    }

    public void setTripListController(TripListController tripListController) {
        this.tripListController = tripListController;
    }
}