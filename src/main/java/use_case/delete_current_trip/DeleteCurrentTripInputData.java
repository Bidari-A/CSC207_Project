package use_case.delete_current_trip;

public class DeleteCurrentTripInputData {
    private final String username;

    public DeleteCurrentTripInputData(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}

