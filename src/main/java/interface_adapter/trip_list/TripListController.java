package interface_adapter.trip_list;

public interface TripListController {
    void executeDetails(String username, String tripName);
    void executeDelete(String username, String tripName);
}