package use_case.delete_current_trip;

/**
 * Output data for the Delete Current Trip Use Case.
 */
public class DeleteCurrentTripOutputData {
    private final String username;

    public DeleteCurrentTripOutputData(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}

