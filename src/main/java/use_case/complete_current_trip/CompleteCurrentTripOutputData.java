package use_case.complete_current_trip;

/**
 * Output data for the Complete Current Trip Use Case.
 */
public class CompleteCurrentTripOutputData {
    private final String username;

    public CompleteCurrentTripOutputData(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
