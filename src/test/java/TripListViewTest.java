import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import entity.Trip;
import interface_adapter.ViewManagerModel;
import interface_adapter.trip_list.TripListState;
import interface_adapter.trip_list.TripListViewModel;
import view.TripListView;

/**
 * Test suite for TripListView.
 */
class TripListViewTest {

    private TripListViewModel tripListViewModel;
    private ViewManagerModel viewManagerModel;
    private TripListView tripListView;
    private JFrame testFrame;

    @BeforeEach
    void setUp() {
        tripListViewModel = new TripListViewModel();
        viewManagerModel = new ViewManagerModel();
        tripListView = new TripListView(tripListViewModel);

        // Create a frame to ensure proper initialization
        testFrame = new JFrame();
        testFrame.add(tripListView);
        testFrame.pack();
    }

    /**
     * Helper method to wait for Swing EDT to process events
     */
    private void waitForSwing() {
        try {
            SwingUtilities.invokeAndWait(() -> {});
            Thread.sleep(50);
        } catch (Exception e) {
            // Ignore
        }
    }

    /**
     * Helper method to safely get the trip list panel
     */
    private JPanel getTripListPanel() {
        final JPanel[] result = new JPanel[1];
        try {
            SwingUtilities.invokeAndWait(() -> {
                JPanel centerPanel = (JPanel) tripListView.getComponent(1);
                JScrollPane scrollPane = (JScrollPane) centerPanel.getComponent(0);
                result[0] = (JPanel) scrollPane.getViewport().getView();
            });
        } catch (Exception e) {
            return null;
        }
        return result[0];
    }

    @Test
    void testViewName() {
        assertEquals("trip list", tripListView.getViewName());
    }

    @Test
    void testInitialState() {
        // View should be initialized with empty state
        TripListState state = tripListViewModel.getState();
        assertNull(state.getTrips());
        assertEquals("", state.getUsername());
        assertNull(state.getError());
    }

    @Test
    void testPropertyChangeWithEmptyTrips() {
        // Create state with empty trips list
        TripListState state = new TripListState();
        state.setTrips(new ArrayList<>());
        state.setUsername("testuser");

        SwingUtilities.invokeLater(() -> {
            tripListViewModel.setState(state);
            tripListViewModel.firePropertyChange();
        });

        waitForSwing();

        // Check that "No trips found" message is displayed
        JPanel tripListPanel = getTripListPanel();
        assertNotNull(tripListPanel, "Trip list panel should not be null");
        assertTrue(tripListPanel.getComponentCount() > 0, "Should have at least one component");

        Component firstComponent = tripListPanel.getComponent(0);
        assertTrue(firstComponent instanceof JLabel, "First component should be a JLabel");
        JLabel label = (JLabel) firstComponent;
        assertTrue(label.getText().contains("No trips found"),
                "Should display 'No trips found' message");
    }

    @Test
    void testPropertyChangeWithTrips() {
        // Create trips
        List<Trip> trips = new ArrayList<>();
        trips.add(new Trip("Paris Vacation", "Paris",
                new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>()));
        trips.add(new Trip("Tokyo Adventure", "Tokyo",
                new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>()));

        TripListState state = new TripListState();
        state.setTrips(trips);
        state.setUsername("testuser");

        SwingUtilities.invokeLater(() -> {
            tripListViewModel.setState(state);
            tripListViewModel.firePropertyChange();
        });

        waitForSwing();

        // Verify trips are displayed
        JPanel tripListPanel = getTripListPanel();
        assertNotNull(tripListPanel, "Trip list panel should not be null");

        // Should have 2 trip rows + 2 vertical struts = 4 components
        assertTrue(tripListPanel.getComponentCount() >= 2,
                "Should have at least 2 components (trip rows)");
    }

    @Test
    void testPropertyChangeWithError() {
        TripListState state = new TripListState();
        state.setError("Failed to load trips");

        SwingUtilities.invokeLater(() -> {
            tripListViewModel.setState(state);
            tripListViewModel.firePropertyChange();
        });

        waitForSwing();

        // Verify error is displayed
        try {
            final JLabel[] errorLabel = new JLabel[1];
            SwingUtilities.invokeAndWait(() -> {
                JPanel centerPanel = (JPanel) tripListView.getComponent(1);
                errorLabel[0] = (JLabel) centerPanel.getComponent(1);
            });
            assertNotNull(errorLabel[0], "Error label should not be null");
            assertEquals("Failed to load trips", errorLabel[0].getText());
            assertEquals(Color.RED, errorLabel[0].getForeground());
        } catch (Exception e) {
            fail("Failed to access error label: " + e.getMessage());
        }
    }

    @Test
    void testBackButtonAction() {
        try {
            // Find and click the back button
            final JButton[] backButton = new JButton[1];
            SwingUtilities.invokeAndWait(() -> {
                JPanel topPanel = (JPanel) tripListView.getComponent(0);
                backButton[0] = (JButton) topPanel.getComponent(0);
            });

            assertNotNull(backButton[0], "Back button should not be null");

            // Simulate button click
            SwingUtilities.invokeLater(() -> {
                ActionEvent event = new ActionEvent(backButton[0], ActionEvent.ACTION_PERFORMED, "Back");
                tripListView.actionPerformed(event);
            });

            waitForSwing();

            // Verify navigation back to logged in view
            assertEquals("logged in", viewManagerModel.getState());
        } catch (Exception e) {
            fail("Failed to test back button: " + e.getMessage());
        }
    }

    @Test
    void testDetailsButtonExists() {
        // Create trips and set state
        List<Trip> trips = new ArrayList<>();
        trips.add(new Trip("Paris Vacation", "Paris",
                new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>()));

        TripListState state = new TripListState();
        state.setTrips(trips);
        state.setUsername("testuser");

        SwingUtilities.invokeLater(() -> {
            tripListViewModel.setState(state);
            tripListViewModel.firePropertyChange();
        });

        waitForSwing();

        // Find the details button
        try {
            final JButton[] detailsButton = new JButton[1];
            SwingUtilities.invokeAndWait(() -> {
                JPanel tripListPanel = getTripListPanel();
                if (tripListPanel != null && tripListPanel.getComponentCount() > 0) {
                    JPanel tripRow = (JPanel) tripListPanel.getComponent(0);
                    JPanel buttonsPanel = (JPanel) tripRow.getComponent(1);
                    detailsButton[0] = (JButton) buttonsPanel.getComponent(0);
                }
            });

            // Verify button exists and has correct action command
            assertNotNull(detailsButton[0], "Details button should not be null");
            assertEquals("Details", detailsButton[0].getText());
            assertEquals("DETAILS_Paris Vacation", detailsButton[0].getActionCommand());
        } catch (Exception e) {
            fail("Failed to access details button: " + e.getMessage());
        }
    }

    @Test
    void testDeleteButtonExists() {
        // Create trips and set state
        List<Trip> trips = new ArrayList<>();
        trips.add(new Trip("Tokyo Adventure", "Tokyo",
                new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>()));

        TripListState state = new TripListState();
        state.setTrips(trips);
        state.setUsername("testuser");

        SwingUtilities.invokeLater(() -> {
            tripListViewModel.setState(state);
            tripListViewModel.firePropertyChange();
        });

        waitForSwing();

        // Find the delete button
        try {
            final JButton[] deleteButton = new JButton[1];
            SwingUtilities.invokeAndWait(() -> {
                JPanel tripListPanel = getTripListPanel();
                if (tripListPanel != null && tripListPanel.getComponentCount() > 0) {
                    JPanel tripRow = (JPanel) tripListPanel.getComponent(0);
                    JPanel buttonsPanel = (JPanel) tripRow.getComponent(1);
                    deleteButton[0] = (JButton) buttonsPanel.getComponent(1);
                }
            });

            // Verify button exists and has correct properties
            assertNotNull(deleteButton[0], "Delete button should not be null");
            assertEquals("Delete", deleteButton[0].getText());
            assertEquals("DELETE_Tokyo Adventure", deleteButton[0].getActionCommand());
            assertEquals(Color.RED, deleteButton[0].getForeground());
        } catch (Exception e) {
            fail("Failed to access delete button: " + e.getMessage());
        }
    }

    @Test
    void testMultipleTripsDisplay() {
        // Create multiple trips
        List<Trip> trips = new ArrayList<>();
        trips.add(new Trip("Trip 1", "City 1",
                new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>()));
        trips.add(new Trip("Trip 2", "City 2",
                new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>()));
        trips.add(new Trip("Trip 3", "City 3",
                new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>()));

        TripListState state = new TripListState();
        state.setTrips(trips);
        state.setUsername("testuser");

        SwingUtilities.invokeLater(() -> {
            tripListViewModel.setState(state);
            tripListViewModel.firePropertyChange();
        });

        waitForSwing();

        // Verify all trips are displayed
        JPanel tripListPanel = getTripListPanel();
        assertNotNull(tripListPanel, "Trip list panel should not be null");

        // Should have 3 trip rows + 3 vertical struts = 6 components
        assertEquals(6, tripListPanel.getComponentCount(),
                "Should have 6 components (3 trip rows + 3 struts)");
    }

    @Test
    void testErrorClearedOnStateUpdate() {
        // First set an error
        TripListState state1 = new TripListState();
        state1.setError("Error message");

        SwingUtilities.invokeLater(() -> {
            tripListViewModel.setState(state1);
            tripListViewModel.firePropertyChange();
        });
        waitForSwing();

        // Then update with no error
        TripListState state2 = new TripListState();
        state2.setTrips(new ArrayList<>());
        state2.setUsername("testuser");

        SwingUtilities.invokeLater(() -> {
            tripListViewModel.setState(state2);
            tripListViewModel.firePropertyChange();
        });
        waitForSwing();

        // Verify error is cleared
        try {
            final JLabel[] errorLabel = new JLabel[1];
            SwingUtilities.invokeAndWait(() -> {
                JPanel centerPanel = (JPanel) tripListView.getComponent(1);
                errorLabel[0] = (JLabel) centerPanel.getComponent(1);
            });
            assertNotNull(errorLabel[0], "Error label should not be null");
            assertEquals("", errorLabel[0].getText(), "Error should be cleared");
        } catch (Exception e) {
            fail("Failed to access error label: " + e.getMessage());
        }
    }

    @Test
    void testTripRowCreation() {
        // Create a trip
        List<Trip> trips = new ArrayList<>();
        trips.add(new Trip("Test Trip", "Test City",
                new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>()));

        TripListState state = new TripListState();
        state.setTrips(trips);
        state.setUsername("testuser");

        SwingUtilities.invokeLater(() -> {
            tripListViewModel.setState(state);
            tripListViewModel.firePropertyChange();
        });
        waitForSwing();

        // Verify trip row structure
        try {
            final JPanel[] tripRow = new JPanel[1];
            SwingUtilities.invokeAndWait(() -> {
                JPanel tripListPanel = getTripListPanel();
                if (tripListPanel != null && tripListPanel.getComponentCount() > 0) {
                    tripRow[0] = (JPanel) tripListPanel.getComponent(0);
                }
            });

            assertNotNull(tripRow[0], "Trip row should not be null");

            // Should have trip name on left and buttons on right
            assertTrue(tripRow[0].getComponent(0) instanceof JLabel,
                    "First component should be a JLabel");
            JLabel tripNameLabel = (JLabel) tripRow[0].getComponent(0);
            assertEquals("Test Trip", tripNameLabel.getText());

            assertTrue(tripRow[0].getComponent(1) instanceof JPanel,
                    "Second component should be a JPanel");
            JPanel buttonsPanel = (JPanel) tripRow[0].getComponent(1);
            assertEquals(2, buttonsPanel.getComponentCount(),
                    "Should have 2 buttons (Details and Delete)");
        } catch (Exception e) {
            fail("Failed to access trip row: " + e.getMessage());
        }
    }

    @Test
    void testViewLayoutStructure() {
        try {
            // Verify the view has correct layout structure
            final Integer[] componentCount = new Integer[1];
            SwingUtilities.invokeAndWait(() ->
                    componentCount[0] = tripListView.getComponentCount());
            assertEquals(2, componentCount[0], "Should have 2 main components (top and center panels)");

            // Top panel should have back button and title
            final JPanel[] topPanel = new JPanel[1];
            SwingUtilities.invokeAndWait(() ->
                    topPanel[0] = (JPanel) tripListView.getComponent(0));
            assertNotNull(topPanel[0], "Top panel should not be null");
            assertTrue(topPanel[0].getComponent(0) instanceof JButton,
                    "First component should be back button");
            assertTrue(topPanel[0].getComponent(1) instanceof JLabel,
                    "Second component should be title");

            // Center panel should have scroll pane and error label
            final JPanel[] centerPanel = new JPanel[1];
            SwingUtilities.invokeAndWait(() ->
                    centerPanel[0] = (JPanel) tripListView.getComponent(1));
            assertNotNull(centerPanel[0], "Center panel should not be null");
            assertTrue(centerPanel[0].getComponent(0) instanceof JScrollPane,
                    "First component should be scroll pane");
            assertTrue(centerPanel[0].getComponent(1) instanceof JLabel,
                    "Second component should be error label");
        } catch (Exception e) {
            fail("Failed to verify layout structure: " + e.getMessage());
        }
    }

    @Test
    void testTitleDisplay() {
        try {
            // Verify title is displayed correctly
            final JLabel[] title = new JLabel[1];
            SwingUtilities.invokeAndWait(() -> {
                JPanel topPanel = (JPanel) tripListView.getComponent(0);
                title[0] = (JLabel) topPanel.getComponent(1);
            });

            assertNotNull(title[0], "Title should not be null");
            assertEquals("My Trips", title[0].getText());
            assertEquals(SwingConstants.CENTER, title[0].getHorizontalAlignment());
            assertTrue(title[0].getFont().isBold());
        } catch (Exception e) {
            fail("Failed to verify title: " + e.getMessage());
        }
    }

    /**
     * Visual test method - displays the trip list view with example data.
     * This can be run manually to see how the view looks with sample trips.
     * Comment out @Test annotation if you don't want it to run automatically.
     */
    // @Test
    void displayTripListViewWithExampleData() {
        SwingUtilities.invokeLater(() -> {
            // Create example trips with sample data
            List<Trip> exampleTrips = new ArrayList<>();

            // Trip 1: Paris Vacation
            exampleTrips.add(new Trip("Paris Vacation", "Paris",
                    new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>()));

            // Trip 2: Tokyo Adventure
            exampleTrips.add(new Trip("Tokyo Adventure", "Tokyo",
                    new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>()));

            // Trip 3: New York City
            exampleTrips.add(new Trip("New York City", "New York",
                    new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>()));

            // Trip 4: London Explorer
            exampleTrips.add(new Trip("London Explorer", "London",
                    new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>()));

            // Trip 5: Rome Cultural Tour
            exampleTrips.add(new Trip("Rome Cultural Tour", "Rome",
                    new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>()));

            // Set up the state with example trips
            TripListState state = new TripListState();
            state.setTrips(exampleTrips);
            state.setUsername("testuser");

            // Update the view model
            tripListViewModel.setState(state);
            tripListViewModel.firePropertyChange();

            // Create and display the frame
            JFrame displayFrame = new JFrame("Trip List View - Example Data");
            displayFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            displayFrame.getContentPane().add(tripListView);
            displayFrame.setSize(800, 600);
            displayFrame.setLocationRelativeTo(null);
            displayFrame.setVisible(true);
        });

        // Keep the test running so the window stays visible
        try {
            Thread.sleep(30000); // Keep window open for 30 seconds
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Visual test method - displays empty trip list view.
     * This shows how the view looks when there are no trips.
     */
    // @Test
    void displayEmptyTripListView() {
        SwingUtilities.invokeLater(() -> {
            // Set up empty state
            TripListState state = new TripListState();
            state.setTrips(new ArrayList<>());
            state.setUsername("testuser");

            // Update the view model
            tripListViewModel.setState(state);
            tripListViewModel.firePropertyChange();

            // Create and display the frame
            JFrame displayFrame = new JFrame("Trip List View - Empty State");
            displayFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            displayFrame.getContentPane().add(tripListView);
            displayFrame.setSize(800, 600);
            displayFrame.setLocationRelativeTo(null);
            displayFrame.setVisible(true);
        });

        // Keep the test running so the window stays visible
        try {
            Thread.sleep(30000); // Keep window open for 30 seconds
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Standalone main method to display the trip list view with example data.
     * Run this method directly to see the view with sample trips.
     */
    public static void main(String[] args) {
        // Create the view model and view
        TripListViewModel tripListViewModel = new TripListViewModel();
        ViewManagerModel viewManagerModel = new ViewManagerModel();
        TripListView tripListView = new TripListView(tripListViewModel);

        // Create example trips with sample data
        List<Trip> exampleTrips = new ArrayList<>();

        // Trip 1: Paris Vacation
        exampleTrips.add(new Trip("Paris Vacation", "Paris",
                new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>()));

        // Trip 2: Tokyo Adventure
        exampleTrips.add(new Trip("Tokyo Adventure", "Tokyo",
                new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>()));

        // Trip 3: New York City
        exampleTrips.add(new Trip("New York City", "New York",
                new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>()));

        // Trip 4: London Explorer
        exampleTrips.add(new Trip("London Explorer", "London",
                new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>()));

        // Trip 5: Rome Cultural Tour
        exampleTrips.add(new Trip("Rome Cultural Tour", "Rome",
                new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>()));

        // Trip 6: Barcelona Beach Trip
        exampleTrips.add(new Trip("Barcelona Beach Trip", "Barcelona",
                new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>()));

        // Trip 7: Dubai Luxury Experience
        exampleTrips.add(new Trip("Dubai Luxury Experience", "Dubai",
                new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>()));

        // Create state and set trips
        TripListState state = new TripListState();
        state.setTrips(exampleTrips);
        state.setUsername("testuser");

        // Push state into VM and update view
        tripListViewModel.setState(state);
        tripListViewModel.firePropertyChange();

        // Show UI in a window
        JFrame frame = new JFrame("Trip List View - Example Data");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(tripListView);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
