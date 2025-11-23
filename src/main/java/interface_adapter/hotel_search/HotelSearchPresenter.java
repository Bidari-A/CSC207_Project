package interface_adapter.hotel_search;

import use_case.hotel_search.HotelSearchOutputBoundary;
import use_case.hotel_search.HotelSearchOutputData;

public class HotelSearchPresenter implements HotelSearchOutputBoundary {

    private final HotelSearchViewModel viewModel;

    public HotelSearchPresenter(HotelSearchViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void prepareSuccessView(HotelSearchOutputData outputData) {
        HotelSearchState state = viewModel.getState();
        state.setSummary(outputData.getSummary());
        viewModel.firePropertyChanged();
    }

    @Override
    public void prepareFailView(String errorMessage) {
        HotelSearchState state = viewModel.getState();
        state.setSummary("Error: " + errorMessage);
        viewModel.firePropertyChanged();
    }
}