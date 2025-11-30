package interface_adapter.complete_trip;

import use_case.complete_trip.CompleteTripOutputBoundary;
import use_case.complete_trip.CompleteTripOutputData;

public class CompleteTripPresenter implements CompleteTripOutputBoundary {

    private final CompleteTripViewModel viewModel;

    public CompleteTripPresenter(CompleteTripViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void prepareSuccessView(CompleteTripOutputData outputData) {
        viewModel.setTripStatus(outputData.getStatus());
        viewModel.setLastCompletedTripId(outputData.getTripId());
        viewModel.setErrorMessage(null);
    }

    @Override
    public void prepareFailView(String errorMessage) {
        viewModel.setErrorMessage(errorMessage);
    }
}

