package use_case.create_new_trip;


public class CreateNewTripInputData {

    private final String from;
    private final String to;
    private final String startDate;
    private final String endDate;

    public CreateNewTripInputData(String from, String to,
                                  String date, String endDate) {
        this.from = from;
        this.to = to;
        this.startDate = date;
        this.endDate = endDate;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {return endDate;}
}
