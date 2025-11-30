package use_case.hotel_search;
import entity.Accommodation;


public interface HotelSearchGateway {
    /**
     * Fetch raw JSON from SerpAPI's Google Hotels search.
     *
     * @param query          e.g. "Bali Resorts"
     * @param checkInDate    e.g. "2025-11-22"
     * @param checkOutDate   e.g. "2025-11-23"
     * @return raw JSON string
     */

    String fetchRawJson(String query, String checkInDate, String checkOutDate);

    String summarizeFirstHotel(String json);

    // NEW: build an Accommodation entity from the first/best hotel in the JSON
    Accommodation buildFirstHotelEntity(String json);
}
