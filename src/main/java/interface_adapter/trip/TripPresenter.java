package interface_adapter.trip;

import interface_adapter.ViewManagerModel;
import use_case.load_trip_detail.LoadTripDetailOutputBoundary;
import use_case.load_trip_detail.LoadTripDetailOutputData;
public class TripPresenter implements LoadTripDetailOutputBoundary {

    // Model here
    private final TripViewModel tripViewModel;
    private final ViewManagerModel viewManagerModel;

    public TripPresenter(TripViewModel tripListModel, ViewManagerModel viewManagerModel) {
        this.tripViewModel = tripListModel;
        this.viewManagerModel = viewManagerModel;
    }

    @Override
    public void prepareTripView(LoadTripDetailOutputData outputData) {
        final TripState tripState = tripViewModel.getState();
        tripState.setPrevViewName(outputData.getPrevViewName());

        tripState.setTripName(outputData.getTripName());
        tripState.setCity(outputData.getCityName());
        tripState.setDate(outputData.getDate());
        tripState.setAttractions(outputData.getAttractions());
        tripState.setFlightDetails(outputData.getFlightDetails());
        tripState.setHotelDetails(outputData.getHotelDetails());

        // TODO GEMINI OUTPUT ???
        tripViewModel.firePropertyChange();

        viewManagerModel.setState(tripViewModel.getViewName());
        viewManagerModel.firePropertyChange();
    }

    @Override
    public void back() {
        final TripState tripState = tripViewModel.getState();
        viewManagerModel.setState(tripState.getPrevViewName());
        viewManagerModel.firePropertyChange();
    }
}
