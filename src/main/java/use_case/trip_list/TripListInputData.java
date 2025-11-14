package use_case.trip_list;

/**
 * Input data for the Trip List Use Case.
 */

public class TripListInputData {
    private final String username;

    public TripListInputData(String username){
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
