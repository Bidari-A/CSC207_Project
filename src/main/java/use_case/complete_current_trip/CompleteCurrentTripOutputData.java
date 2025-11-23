package use_case.complete_current_trip;

public class CompleteCurrentTripOutputData {
    private final boolean success;
    private final String message;

    public CompleteCurrentTripOutputData(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }
}
