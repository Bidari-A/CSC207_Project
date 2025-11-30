package use_case.hotel_search;
import data_access.FileUserDataAccessObject;
import data_access.FileTripDataAccessObject;
import entity.Trip;
import entity.Accommodation;
import entity.User;

public class HotelSearchInteractor implements HotelSearchInputBoundary {

    private final HotelSearchGateway gateway;
    private final HotelSearchOutputBoundary presenter;

    private final FileUserDataAccessObject userDao;
    private final FileTripDataAccessObject tripDao;

    public HotelSearchInteractor(HotelSearchGateway gateway,
                                 HotelSearchOutputBoundary presenter,
                                 FileUserDataAccessObject userDao,
                                 FileTripDataAccessObject tripDao) {
        this.gateway = gateway;
        this.presenter = presenter;
        this.userDao = userDao;
        this.tripDao = tripDao;
    }


    @Override
    public void execute(HotelSearchInputData inputData) {
        try {
            String json = gateway.fetchRawJson(
                    inputData.getQuery(),
                    inputData.getCheckInDate(),
                    inputData.getCheckOutDate()
            );

            String summary = gateway.summarizeFirstHotel(json);

// NEW: build entity + save to current trip
            Accommodation bestHotel = gateway.buildFirstHotelEntity(json);
            saveBestHotelToCurrentTrip(bestHotel);

            HotelSearchOutputData outputData = new HotelSearchOutputData(summary);
            presenter.prepareSuccessView(outputData);

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

    private void saveBestHotelToCurrentTrip(Accommodation accommodation) {
        if (accommodation == null) return;

        String username = userDao.getCurrentUsername();
        if (username == null) return;

        User user = userDao.getUser(username);
        if (user == null || user.getCurrentTripId() == null) return;

        String tripId = user.getCurrentTripId();
        Trip trip = tripDao.get(tripId);
        if (trip == null) return;

        // REPLACE the old hotel instead of appending
        trip.getHotels().clear();
        trip.getHotels().add(accommodation);

        tripDao.save(trip);
    }


}
