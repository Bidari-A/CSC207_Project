package use_case.hotel_search;

public class HotelSearchOutputData {
    private final String summary;  // full JSON for first hotel result

    public HotelSearchOutputData(String summary) {
        this.summary = summary;
    }

    public String getSummary() { return summary; }
}
