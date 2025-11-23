package use_case.flight_search;

public class FlightSearchOutputData {
    // We make this final, because we basically make a NEW OutputData obj each time!!
    private final String summary;
    // This summary would hold the message to be displayed to the user, in exact.

    public FlightSearchOutputData(String summary) {
        this.summary = summary;
    }
    // Again, getter is needed as we SHOUOLD NOT directly work with instance variables.
    public String getSummary() {
        return summary;
    }
}
