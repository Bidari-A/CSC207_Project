package use_case.delete_current_trip;

public class DeleteCurrentTripOutputData {
    private final String message;
    public DeleteCurrentTripOutputData(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
