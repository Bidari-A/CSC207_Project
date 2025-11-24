package interface_adapter.flight_search;

// We import the output classes since presenter RECEIVES the output!
import use_case.flight_search.FlightSearchOutputBoundary;
import use_case.flight_search.FlightSearchOutputData;

// TWO STEPS! Prep success view and fail view.
public class FlightSearchPresenter implements FlightSearchOutputBoundary {
    private final FlightSearchViewModel viewModel; // We need to update the viewModel!
    public FlightSearchPresenter(FlightSearchViewModel viewModel) {
        this.viewModel = viewModel;
    }
    // Presenter is the output boundary, and it has two methods that takes the outputData as the argument.
    // Remember the interface contract for OutputBoundary?
    // Prepare Sucesss and fail view!
    public void prepareSuccessView(FlightSearchOutputData outputData){
        FlightSearchState state = viewModel.getState();
        // we set the viewModel's stored state summary text to the new summary text
        state.setSummary(outputData.getSummary());
        //we notify the app we have changed the state! Time for UI update.
        viewModel.firePropertyChange("state");
    }

    public void prepareFailView(String errorMessage){
        FlightSearchState state = viewModel.getState();
        state.setSummary("Error: " + errorMessage); // some error handling...
        viewModel.firePropertyChange("state");

    }


}
