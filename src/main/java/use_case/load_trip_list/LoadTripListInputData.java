package use_case.load_trip_list;

/**
 * The Input Data for the Load Trip List Use Case.
 */
public class LoadTripListInputData {

    private final String username;

    public LoadTripListInputData(String username) {
        this.username = username;
    }

    String getUsername() {
        return username;
    }
}
