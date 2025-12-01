package use_case.complete_trip;

public class CompleteTripInputData {
    private final String username;

    public CompleteTripInputData(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}