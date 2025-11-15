package interface_adapter.create_new_trip;
/**
 * The state for the Create Trip View Model.
 */

public class CreateNewTripState {

    private String from = "";
    private String to = "";
    private String date = "";
    private String planText = "";
    private String error = "";

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public String getDate() {
        return date;
    }

    public String getPlanText() {
        return planText;
    }

    public String getError() {
        return error;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setPlanText(String planText) {
        this.planText = planText;
    }

    public void setError(String error) {
        this.error = error;
    }


}




