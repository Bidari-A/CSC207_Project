package entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Factory for creating User objects.
 */
public class UserFactory {

    /**
     * Creates a new User with default values for currentTripId (null) and tripList (empty).
     * @param name the username
     * @param password the password
     * @return a new User object
     */
    public User create(String name, String password) {
        return new User(name, password, null, new ArrayList<>());
    }

    /**
     * Creates a new User with all fields specified.
     * @param name the username
     * @param password the password
     * @param currentTripId the current trip ID (may be null)
     * @param tripList the list of trip IDs
     * @return a new User object
     */
    public User create(String name, String password, String currentTripId, List<String> tripList) {
        return new User(name, password, currentTripId, tripList);
    }
}
