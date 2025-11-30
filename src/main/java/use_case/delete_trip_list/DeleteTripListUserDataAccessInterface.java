package use_case.delete_trip_list;

import entity.User;

public interface DeleteTripListUserDataAccessInterface {
    User getUser(String username);
    void saveUser(User user);
}
