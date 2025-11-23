package use_case.load_trip_detail;

public interface LoadTripDetailInputBoundary {
    void execute(LoadTripDetailInputData loadTripDetailInputData);
    void back();
}
