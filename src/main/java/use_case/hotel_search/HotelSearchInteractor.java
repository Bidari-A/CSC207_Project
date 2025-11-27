package use_case.hotel_search;

public class HotelSearchInteractor implements HotelSearchInputBoundary {

    private final HotelSearchGateway gateway;
    private final HotelSearchOutputBoundary presenter;

    public HotelSearchInteractor(HotelSearchGateway gateway,
                                 HotelSearchOutputBoundary presenter) {
        this.gateway = gateway;
        this.presenter = presenter;
    }


    @Override
    public void execute(HotelSearchInputData inputData) {
        try {
            String json = gateway.fetchRawJson(
                    inputData.getQuery(),
                    inputData.getCheckInDate(),
                    inputData.getCheckOutDate()
            );
            // String json = gateway.fetchRawJson(query, checkIn, checkOut);
            // String summary = ((SerpApiHotelSearchGateway) gateway)
            //        .summarizeFirstHotel(json, checkIn, checkOut, 1);

            String firstHotel = gateway.summarizeFirstHotel(json);
            HotelSearchOutputData out = new HotelSearchOutputData(firstHotel);
            presenter.prepareSuccessView(out);
        } catch (Exception e) {
            presenter.prepareFailView(e.getMessage());
        }
    }

    // Minimal parser: grab first object in "ads" array
    private String extractFirstHotel(String json) {
        int adsIdx = json.indexOf("\"ads\"");
        if (adsIdx < 0) {
            return json; // fallback: whole JSON
        }

        int start = json.indexOf('{', adsIdx);
        if (start < 0) {
            return json;
        }

        int depth = 0;
        int i = start;
        for (; i < json.length(); i++) {
            char c = json.charAt(i);
            if (c == '{') depth++;
            else if (c == '}') {
                depth--;
                if (depth == 0) {
                    i++; // include closing brace
                    break;
                }
            }
        }

        return json.substring(start, i);
    }
}
