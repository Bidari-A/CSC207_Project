package use_case.create_new_trip;

public class CreateNewTripOutputData {

    private final String tripName;
    private final String from;
    private final String to;
    private final String startDate;
    private final String endDate;
    private final String itinerary;

    public CreateNewTripOutputData(String tripName,
                                   String from,
                                   String to,
                                   String startDate,
                                   String endDate,
                                   String itinerary) {
        this.tripName = tripName;
        this.from = from;
        this.to = to;
        this.startDate = startDate;
        this.endDate = endDate;
        this.itinerary = itinerary;
    }

    public String getTripName() {
        return tripName;
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

    public String getEndDate() {
        return endDate;
    }

    public String getItinerary() {
        return itinerary;
    }
}
