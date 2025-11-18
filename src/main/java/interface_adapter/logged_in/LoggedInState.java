package interface_adapter.logged_in;

/**
 * The State information representing the logged-in user.
 */
public class LoggedInState {
    private String username = "";

    private String password = "";
    private String passwordError;

    private String lastTripName = "";

    public LoggedInState(LoggedInState copy) {
        username = copy.username;
        password = copy.password;
        passwordError = copy.passwordError;
        lastTripName = copy.lastTripName;
    }

    // Because of the previous copy constructor, the default constructor must be explicit.
    public LoggedInState() {

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setPasswordError(String passwordError) {
        this.passwordError = passwordError;
    }

    public String getPasswordError() {
        return passwordError;
    }

    public String getLastTripName() {
        return lastTripName;
    }

    public void setLastTripName(String lastTripName) {
        this.lastTripName = lastTripName;
    }
}
