package use_case.create_new_trip;


public class CreateNewTripInputData {

    private final String from;
    private final String to;
    private final String startDate;
    private final String endDate;
    private final String currentUsername;

    public CreateNewTripInputData(String from, String to,
                                  String date, String endDate,
                                  String currentUsername) {
        this.from = from;
        this.to = to;
        this.startDate = date;
        this.endDate = endDate;
        this.currentUsername = currentUsername;
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

    public String getCurrentUsername() {return currentUsername;}
}
