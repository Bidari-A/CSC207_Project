package interface_adapter.hotel_search;

import use_case.hotel_search.HotelSearchInputBoundary;
import use_case.hotel_search.HotelSearchInputData;

public class HotelSearchController {

    private final HotelSearchInputBoundary interactor;

    public HotelSearchController(HotelSearchInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void execute(String query, String checkInDate, String checkOutDate) {
        HotelSearchInputData inputData =
                new HotelSearchInputData(query, checkInDate, checkOutDate);
        interactor.execute(inputData);
    }
}