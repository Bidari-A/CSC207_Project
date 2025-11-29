package use_case.complete_current_trip;

/**
 * Input data for the Complete Current Trip Use Case.
 */
public class CompleteCurrentTripInputData {
    private final String username;

    public CompleteCurrentTripInputData(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}

