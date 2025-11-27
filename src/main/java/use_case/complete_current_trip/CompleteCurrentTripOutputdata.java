package use_case.complete_current_trip;

public class CompleteCurrentTripOutputdata {
    private String username;
    private boolean completed;

    public CompleteCurrentTripOutputdata(String username, boolean completed) {
        this.username = username;
        this.completed = completed;
    }

    public String getUsername() {
        return username;
    }

    public boolean isCompleted() {
        return completed;
    }
}
