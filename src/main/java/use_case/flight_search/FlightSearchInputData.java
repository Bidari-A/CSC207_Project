package use_case.flight_search;

// This class tells you the specification of the input data,
// that the UseCase Interactor is supposed to receive
public class FlightSearchInputData {
    private final String from;
    private final String to;
    private final String outboundDate;
    private final String returnDate;

    // we make the constructor here :)
    public FlightSearchInputData(String from, String to,
                                        String outboundDate, String returnDate) {
        this.from = from;
        this.to = to;
        this.outboundDate = outboundDate;
        this.returnDate = returnDate;

    }


    // QUICK Shortcut! Use 'Refactor, encapsulate field, and select getter"
    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public String getOutboundDate() {
        return outboundDate;
    }

    public String getReturnDate() {
        return returnDate;
    }
}
