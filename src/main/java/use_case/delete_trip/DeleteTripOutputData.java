package use_case.delete_trip;

/**
 * Output data for the Delete Trip Use Case.
 */
public class DeleteTripOutputData {
    private final String username;
    private final boolean success;

    public DeleteTripOutputData(String username, boolean success) {
        this.username = username;
        this.success = success;
    }

    public String getUsername() {
        return username;
    }

    public boolean isSuccess() {
        return success;
    }
}
