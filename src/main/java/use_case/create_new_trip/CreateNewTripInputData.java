package use_case.create_new_trip;


public class CreateNewTripInputData {

    private final String from;
    private final String to;
    private final String date;

    public CreateNewTripInputData(String from, String to, String date) {
        this.from = from;
        this.to = to;
        this.date = date;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public String getDate() {
        return date;
    }
}
