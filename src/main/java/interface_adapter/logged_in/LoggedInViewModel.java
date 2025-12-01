package interface_adapter.logged_in;

import interface_adapter.ViewModel;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * The View Model for the Logged In View.
 */
// NOTE: I am making the ViewModel concrete by implementing the required methods.
// Assuming your base ViewModel<T> class already defines the state management.

public class LoggedInViewModel extends ViewModel { // Removed <LoggedInState> if base ViewModel handles it

    public static final String TITLE_LABEL = "Logged In View";
    private LoggedInState state = new LoggedInState(); // Initialize state

    public LoggedInViewModel() {
        super("logged in");
    }

    public LoggedInState getState() {
        return state;
    }

    public void setState(LoggedInState state) {
        this.state = state;
    }

    private final PropertyChangeSupport support = new PropertyChangeSupport(this);

    @Override
    public void firePropertyChanged() {
        support.firePropertyChange("state", null, this.state); // Fire change event
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }
}
