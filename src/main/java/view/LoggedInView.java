package view;

import interface_adapter.logged_in.ChangePasswordController;
import interface_adapter.logged_in.LoggedInState;
import interface_adapter.logged_in.LoggedInViewModel;
import interface_adapter.logout.LogoutController;
import interface_adapter.signup.SignupState;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
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
    private LogoutController logoutController;

    private final JLabel username;
    private final JLabel lastTripLabel;
    private final JLabel nameLabel;
    private final JLabel cityLabel;
    private final JLabel dateLabel;

    private final JButton logOut;
    private final JButton tripListButton;
    private final JButton newTripButton;
    private final JButton detailsButton;
    private final JButton deleteButton;
    private final JButton createButton;
    private final JButton completeButton;

    public LoggedInView(LoggedInViewModel loggedInViewModel) {
        this.loggedInViewModel = loggedInViewModel;
        this.loggedInViewModel.addPropertyChangeListener(this);

        // ===== 顶部栏 =====
        JPanel topPanel = new JPanel(new BorderLayout());
        logOut = new JButton("Log Out");
        tripListButton = new JButton("Trip List");
        JLabel title = new JLabel("Dashboard", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 22));

        topPanel.add(logOut, BorderLayout.WEST);
        topPanel.add(title, BorderLayout.CENTER);
        topPanel.add(tripListButton, BorderLayout.EAST);

        // ===== 用户信息区域 =====
        JLabel usernameInfo = new JLabel("Currently logged in: ");
        username = new JLabel();
        username.setFont(new Font("Arial", Font.PLAIN, 14));

        // ===== 中间内容：Trip 信息 =====
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        lastTripLabel = new JLabel("Last Trip Created:");
        nameLabel = new JLabel("Name of Trip: ");
        cityLabel = new JLabel("City: ");
        dateLabel = new JLabel("Date: ");

        Font infoFont = new Font("Arial", Font.PLAIN, 14);
        lastTripLabel.setFont(infoFont);
        nameLabel.setFont(infoFont);
        cityLabel.setFont(infoFont);
        dateLabel.setFont(infoFont);

        // 居中排列
        lastTripLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        cityLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        dateLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        centerPanel.add(Box.createVerticalStrut(10));
        centerPanel.add(lastTripLabel);
        centerPanel.add(Box.createVerticalStrut(10));
        centerPanel.add(nameLabel);
        centerPanel.add(Box.createVerticalStrut(5));
        centerPanel.add(cityLabel);
        centerPanel.add(Box.createVerticalStrut(5));
        centerPanel.add(dateLabel);
        centerPanel.add(Box.createVerticalStrut(20));

        // ===== 四个按钮：Details / Delete / Create / Complete =====
        JPanel actionRow = new JPanel();
        actionRow.setLayout(new BoxLayout(actionRow, BoxLayout.X_AXIS));
        actionRow.setAlignmentX(Component.CENTER_ALIGNMENT);

        detailsButton = new JButton("Details");
        deleteButton = new JButton("Delete");
        createButton = new JButton("Create");
        completeButton = new JButton("Complete");

        actionRow.add(Box.createHorizontalGlue());
        actionRow.add(detailsButton);
        actionRow.add(Box.createHorizontalStrut(15));
        actionRow.add(deleteButton);
        actionRow.add(Box.createHorizontalStrut(15));
        actionRow.add(createButton);
        actionRow.add(Box.createHorizontalStrut(15));
        actionRow.add(completeButton);
        actionRow.add(Box.createHorizontalGlue());

        centerPanel.add(actionRow);

        // ===== 底部 New Trip =====
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        newTripButton = new JButton("New Trip");
        bottomPanel.add(newTripButton);

        // ===== 主布局 =====
        this.setLayout(new BorderLayout(20, 20));
        this.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        this.add(topPanel, BorderLayout.NORTH);

        JPanel midWrapper = new JPanel();
        midWrapper.setLayout(new BoxLayout(midWrapper, BoxLayout.Y_AXIS));
        midWrapper.add(usernameInfo);
        midWrapper.add(username);
        midWrapper.add(Box.createVerticalStrut(10));
        midWrapper.add(centerPanel);

        this.add(midWrapper, BorderLayout.CENTER);
        this.add(bottomPanel, BorderLayout.SOUTH);

        // ===== 添加监听器 =====
        logOut.addActionListener(this);
        tripListButton.addActionListener(this);
        newTripButton.addActionListener(this);
        detailsButton.addActionListener(this);
        deleteButton.addActionListener(this);
        createButton.addActionListener(this);
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
            System.out.println("Trip List clicked");
        } else if (source == newTripButton) {
            System.out.println("New Trip clicked");
        } else if (source == detailsButton) {
            System.out.println("Details clicked");
        } else if (source == deleteButton) {
            System.out.println("Delete clicked");
        } else if (source == createButton) {
            System.out.println("Create clicked");
        } else if (source == completeButton) {
            System.out.println("Complete clicked");
        }
    }

    /**
     * Updates username when state changes.
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
}