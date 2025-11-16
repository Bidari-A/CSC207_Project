package interface_adapter.login;

import interface_adapter.ViewModel;
import java.beans.PropertyChangeListener;

/**
 * The View Model for the Login View.
 */
public class LoginViewModel extends ViewModel<LoginState> {

    public LoginViewModel() {
        super("log in");
        setState(new LoginState());
    }

    @Override
    public void setState(LoginState state) {
        super.setState(state);
        firePropertyChanged();
    }

    public void firePropertyChanged() {
        super.firePropertyChange("state");
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        super.addPropertyChangeListener(listener);
    }
}
