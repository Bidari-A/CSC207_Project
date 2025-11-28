package use_case.delete_current_trip;

public class DeleteCurrentTripOutputData {
    private final String username;
    private final boolean deleted;

    public DeleteCurrentTripOutputData(String username, boolean deleted) {
        this.username = username;
        this.deleted = deleted;
    }

    public String getUsername() {
        return username;
    }

    public boolean isDeleted() {
        return deleted;
    }
}

