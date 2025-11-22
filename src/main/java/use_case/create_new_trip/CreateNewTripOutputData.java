package use_case.create_new_trip;

public class CreateNewTripOutputData {
    private final String from;
    private final String to;
    private final String date;
    private final boolean submitted;

    public CreateNewTripOutputData(String from, String to, String date, boolean submitted) {
        this.from = from;
        this.to = to;
        this.date = date;
        this.submitted = submitted;
    }
    public String getFrom() { return from; }
    public String getTo() { return to; }
    public String getDate() { return date; }
    public boolean isSubmitted() { return submitted; }
}
