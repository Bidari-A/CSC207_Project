package use_case.load_trip_detail;

public class LoadTripDetailInteractor implements LoadTripDetailInputBoundary {
    private final LoadTripDetailDataAccessInterface loadTripDetailDataAccessInterface;
    private final LoadTripDetailOutputBoundary loadTripDetailOutputBoundary;

    public LoadTripDetailInteractor(LoadTripDetailDataAccessInterface loadTripDetailDataAccessInterface, LoadTripDetailOutputBoundary loadTripDetailOutputBoundary) {
        this.loadTripDetailDataAccessInterface = loadTripDetailDataAccessInterface;
        this.loadTripDetailOutputBoundary = loadTripDetailOutputBoundary;
    }

    @Override
    public void execute(LoadTripDetailInputData loadTripDetailInputData) {
        // TODO
        if (loadTripDetailInputData.getTripId() != null) {
            String testName = loadTripDetailInputData.getTripId();
            // Here TODO DATABASE STUFF WITH TRIPID
            final String prevViewName = loadTripDetailInputData.getPrevViewName();
            final LoadTripDetailOutputData loadTripDetailOutputData = new LoadTripDetailOutputData(
                    testName, "Paris", "June 12 - June 20, 2025",
                    "Eiffel Tower\nLouvre Museum", "Air France AF-347\nYYZ → CDG\nDepart: 7:00 PM",
                    "Hotel Le Meurice\n5-star luxury suite", prevViewName);

            loadTripDetailOutputBoundary.prepareTripView(loadTripDetailOutputData);
            System.out.println("I sense a tripID");
        } else {
            // grab from database TODO
            // ...
            final String prevViewName = loadTripDetailInputData.getPrevViewName();
            // TODO: Change Place Holder Values
            final LoadTripDetailOutputData loadTripDetailOutputData = new LoadTripDetailOutputData(
                    "Paris Vacation", "Paris", "June 12 - June 20, 2025",
                    "Eiffel Tower\nLouvre Museum", "Air France AF-347\nYYZ → CDG\nDepart: 7:00 PM",
                    "Hotel Le Meurice\n5-star luxury suite", prevViewName);

            loadTripDetailOutputBoundary.prepareTripView(loadTripDetailOutputData);
        }
    }

    @Override
    public void back() {
        loadTripDetailOutputBoundary.back();
    }

}
