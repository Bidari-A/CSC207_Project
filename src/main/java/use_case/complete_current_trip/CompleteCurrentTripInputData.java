package use_case.complete_current_trip;

public class CompleteCurrentTripInputData {
    private final String username;

    public CompleteCurrentTripInputData(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
