package interface_adapter.create_new_trip;

import interface_adapter.ViewManagerModel;
import interface_adapter.create_trip_result.TripResultState;
import interface_adapter.create_trip_result.TripResultViewModel;
import use_case.create_new_trip.CreateNewTripOutputBoundary;
import interface_adapter.create_new_trip.CreateNewTripViewModel;
import use_case.create_new_trip.CreateNewTripOutputData;

public class CreateNewTripPresenter implements CreateNewTripOutputBoundary {

    private final ViewManagerModel viewManagerModel;
    private final CreateNewTripViewModel createNewTripViewModel;
    private final TripResultViewModel tripResultViewModel;

    public CreateNewTripPresenter(ViewManagerModel viewManagerModel,
                                  CreateNewTripViewModel createNewTripViewModel,
                                  TripResultViewModel tripResultViewModel) {
        this.viewManagerModel = viewManagerModel;
        this.createNewTripViewModel = createNewTripViewModel;
        this.tripResultViewModel = tripResultViewModel;
    }

    @Override
    public void prepareScreen() {
        // Reset state for a fresh form
        createNewTripViewModel.setState(new CreateNewTripState());

        // Switch view to the creation form
        viewManagerModel.setState(createNewTripViewModel.getViewName());
        viewManagerModel.firePropertyChange();
    }

    @Override
    public void prepareBackView() {
        // Simple version: go back using the string, like before
        viewManagerModel.setState("logged in");
        viewManagerModel.firePropertyChange();
    }

    @Override
    public void presentResult(CreateNewTripOutputData outputData) {
        // Fill the TripResult screen’s ViewModel
        TripResultState state = new TripResultState();
        state.setTripName(outputData.getTripName());
        state.setFrom(outputData.getFrom());
        state.setTo(outputData.getTo());
        state.setStartDate(outputData.getStartDate());
        state.setEndDate(outputData.getEndDate());
        state.setItinerary(outputData.getItinerary());
        tripResultViewModel.setState(state);

        // Switch to the TripResultView
        viewManagerModel.setState(tripResultViewModel.getViewName());
        viewManagerModel.firePropertyChange();
    }
}
