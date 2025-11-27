package use_case.login;

/**
 * Output Data for the Login Use Case.
 */
public class LoginOutputData {

    private final String username;
    private final String currentTripName;
    private final String cityName;
    private final String date;

    public LoginOutputData(String username, String currentTripName, String cityName, String date) {
        this.username = username;
        this.currentTripName = currentTripName;
        this.cityName = cityName;
        this.date = date;
    }

    public String getUsername() {
        return username;
    }
    public String getCurrentTripName() {
        return currentTripName;
    }
    public String getCityName() {
        return cityName;
    }
    public String getDate() {
        return date;
    }

}
