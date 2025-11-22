package interface_adapter.logged_in;

/**
 * The State information representing the logged-in user.
 */
public class LoggedInState {

    private String username = "";
    private String password = "";
    private String passwordError;

    // ====== LAST COMPLETED TRIP (PERMANENT) ======
    private String lastTripName = "";
    private String lastTripCity = "";
    private String lastTripDate = "";

    // ====== DRAFT TRIP (TEMPORARY) ======
    private String draftTripName = "";
    private String draftTripCity = "";
    private String draftTripDate = "";
    private boolean hasDraft = false;

    // Copy constructor
    public LoggedInState(LoggedInState copy) {
        username = copy.username;
        password = copy.password;
        passwordError = copy.passwordError;

        lastTripName = copy.lastTripName;
        lastTripCity = copy.lastTripCity;
        lastTripDate = copy.lastTripDate;

        draftTripName = copy.draftTripName;
        draftTripCity = copy.draftTripCity;
        draftTripDate = copy.draftTripDate;
        hasDraft = copy.hasDraft;
    }

    // Default constructor
    public LoggedInState() {}

    // ================= USER AUTH FIELDS =================

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getPasswordError() { return passwordError; }
    public void setPasswordError(String passwordError) { this.passwordError = passwordError; }

    // ================= LAST COMPLETED TRIP =================

    public void setLastTrip(String name, String city, String date) {
        this.lastTripName = name;
        this.lastTripCity = city;
        this.lastTripDate = date;
    }

    public String getLastTripName() { return lastTripName; }
    public String getLastTripCity() { return lastTripCity; }
    public String getLastTripDate() { return lastTripDate; }

    // ================= DRAFT TRIP =================

    public void setDraftTrip(String name, String city, String date) {
        this.draftTripName = name;
        this.draftTripCity = city;
        this.draftTripDate = date;
        this.hasDraft = true;
    }

    public String getDraftTripName() { return draftTripName; }
    public String getDraftTripCity() { return draftTripCity; }
    public String getDraftTripDate() { return draftTripDate; }
    public boolean hasDraft() { return hasDraft; }

    public void clearDraft() {
        this.draftTripName = "";
        this.draftTripCity = "";
        this.draftTripDate = "";
        this.hasDraft = false;
    }
}
