package interface_adapter.complete_trip;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class CompleteTripViewModel {
    private String tripStatus;
    private String lastCompletedTripId;
    private String errorMessage;

    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    public String getTripStatus() { return tripStatus; }
    public void setTripStatus(String tripStatus) {
        String old = this.tripStatus;
        this.tripStatus = tripStatus;
        pcs.firePropertyChange("tripStatus", old, tripStatus);
    }

    public String getLastCompletedTripId() { return lastCompletedTripId; }
    public void setLastCompletedTripId(String lastCompletedTripId) {
        String old = this.lastCompletedTripId;
        this.lastCompletedTripId = lastCompletedTripId;
        pcs.firePropertyChange("lastCompletedTripId", old, lastCompletedTripId);
    }

    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) {
        String old = this.errorMessage;
        this.errorMessage = errorMessage;
        pcs.firePropertyChange("errorMessage", old, errorMessage);
    }

    public String getMessage() {
        if (errorMessage != null && !errorMessage.isEmpty()) {
            return "Error: " + errorMessage;
        } else if (tripStatus != null && !tripStatus.isEmpty()) {
            return tripStatus;
        } else if (lastCompletedTripId != null && !lastCompletedTripId.isEmpty()) {
            return "Trip completed successfully: " + lastCompletedTripId;
        } else {
            return null; // nothing to show
        }
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }
}