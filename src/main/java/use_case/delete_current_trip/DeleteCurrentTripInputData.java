package use_case.delete_current_trip;

/**
 * Input data for the Delete Current Trip Use Case.
 */
public class DeleteCurrentTripInputData {
    private final String username;

    public DeleteCurrentTripInputData(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}

