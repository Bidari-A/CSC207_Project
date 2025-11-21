package use_case.load_trip;

/**
 * The Input Data for the Load Trip Use Case.
 */
public class LoadTripInputData {

    private final String username;

    public LoadTripInputData(String username) {
        this.username = username;
    }

    String getUsername() {
        return username;
    }
}