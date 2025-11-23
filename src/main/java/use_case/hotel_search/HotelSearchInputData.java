package use_case.hotel_search;

public class HotelSearchInputData {
    private final String query;
    private final String checkInDate;
    private final String checkOutDate;

    public HotelSearchInputData(String query, String checkInDate, String checkOutDate) {
        this.query = query;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
    }

    public String getQuery() { return query; }
    public String getCheckInDate() { return checkInDate; }
    public String getCheckOutDate() { return checkOutDate; }
}