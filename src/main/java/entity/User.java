package entity;
import java.util.List;

/**
 * A simple entity representing a user. Users have a username and password..
 */
public class User {

    private final String name;
    private final String password;
    private Trip currentTrip;
    private List<Trip> tripHistory;

    /**
     * Creates a new user with the given non-empty name and non-empty password.
     * @param name the username
     * @param password the password
     * @throws IllegalArgumentException if the password or name are empty
     */

    // TODO: Implement how user trip info gets
    public User(String name, String password) {
        if ("".equals(name)) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        if ("".equals(password)) {
            throw new IllegalArgumentException("Password cannot be empty");
        }
        this.name = name;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public Trip getCurrentTrip() {
        return currentTrip;
    }

    public List<Trip> getTripHistory() {
        return tripHistory;
    }

    public void completeTrip() {
        this.tripHistory.add(this.currentTrip);
        this.currentTrip = null;
    }

    public void removeCurrentTrip() {
        this.currentTrip = null;
    }

    public void createNewTrip(Trip newTrip) {
        // TODO: Edit This
        if (this.currentTrip == null) {
            this.currentTrip = newTrip;
        }
    }

}
