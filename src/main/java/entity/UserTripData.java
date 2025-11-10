package entity;
import java.util.List;

public class UserTripData {
    private final String name;
    private Trip currentTrip;
    private List<Trip> tripHistory;

    public UserTripData(String name, Trip currentTrip, List<Trip> tripHistory) {
        this.name = name;
        this.currentTrip = currentTrip;
        this.tripHistory = tripHistory;
    }

    public UserTripData(String name) {
        this(name, null, null);
    }

    public String getName() {
        return name;
    }

    public Trip getCurrentTrip() {
        return currentTrip;
    }

    public List<Trip> getTripHistory() {
        return tripHistory;
    }

    public void createCurrentTrip(Trip trip) {
        this.currentTrip = trip;
    }

    public void completeTrip() {
        if (this.currentTrip != null) {
            this.tripHistory.add(this.currentTrip);
            this.currentTrip = null;
        }
    }

    public void deleteCurrentTrip() {
        if (this.currentTrip != null) {
            this.currentTrip = null;
        }
    }
}
