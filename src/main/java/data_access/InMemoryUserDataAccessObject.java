package data_access;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import entity.Trip;
import entity.User;
import entity.UserFactory;
import use_case.change_password.ChangePasswordUserDataAccessInterface;
import use_case.load_trip_detail.LoadTripDetailDataAccessInterface;
import use_case.load_trip_list.LoadTripListUserDataAccessInterface;
import use_case.complete_current_trip.CompleteCurrentTripDataAccessInterface;
import use_case.login.LoginUserDataAccessInterface;
import use_case.logout.LogoutUserDataAccessInterface;
import use_case.signup.SignupUserDataAccessInterface;

/**
 * In-memory DAO for user data (for testing purposes).
 * Stores users in memory without persistence.
 */
public class InMemoryUserDataAccessObject implements SignupUserDataAccessInterface,
        LoginUserDataAccessInterface,
        CompleteCurrentTripDataAccessInterface,
        ChangePasswordUserDataAccessInterface,
        LogoutUserDataAccessInterface,
        LoadTripListUserDataAccessInterface,
        LoadTripDetailDataAccessInterface {

    private final Map<String, User> accounts = new HashMap<>();
    private final UserFactory userFactory;
    private String currentUsername;
    private FileTripDataAccessObject tripDataAccessObject;

    public InMemoryUserDataAccessObject(UserFactory userFactory) {
        this.userFactory = userFactory;
    }

    @Override
    public void save(User user) {
        accounts.put(user.getUsername(), user);
    }

    @Override
    public User get(String username) {
        return accounts.get(username);
    }

    @Override
    public void setCurrentUsername(String name) {
        currentUsername = name;
    }

    @Override
    public String getCurrentUsername() {
        return currentUsername;
    }

    @Override
    public boolean existsByName(String identifier) {
        return accounts.containsKey(identifier);
    }

    @Override
    public void changePassword(User user) {
        accounts.put(user.getUsername(), user);
    }

    public void setTripDataAccessObject(FileTripDataAccessObject tripDataAccessObject) {
        this.tripDataAccessObject = tripDataAccessObject;
    }

    @Override
    public List<Trip> getTrips(String username) {
        if (tripDataAccessObject != null) {
            return tripDataAccessObject.getTripsByUser(username);
        }
        return new ArrayList<>();
    }

    @Override
    public Trip getTrip(String tripId) {
        if (tripDataAccessObject != null) {
            return tripDataAccessObject.get(tripId);
        }
        return null;
    }

    @Override
    public User getUser(String username) {
        return get(username);
    }

    @Override
    public void updateTripStatus(Trip trip, String newStatus) {
        if (tripDataAccessObject != null) {
            tripDataAccessObject.updateTripStatus(trip, newStatus);
        }
    }

    @Override
    public void clearUserCurrentTrip(String username) {
        User user = get(username);
        if (user != null) {
            User updatedUser = userFactory.create(
                    user.getUsername(),
                    user.getPassword(),
                    null,
                    user.getTripList()
            );
            save(updatedUser);
        }
    }
}

