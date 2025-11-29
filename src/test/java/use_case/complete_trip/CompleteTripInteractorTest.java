package use_case.complete_trip;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import data_access.FileTripDataAccessObject;
import entity.Trip;
import entity.Accommodation;
import entity.Flight;
import entity.Destination;
import interface_adapter.complete_trip.CompleteTripPresenter;
import interface_adapter.complete_trip.CompleteTripViewModel;

public class CompleteTripInteractorTest {

    private TripDataAccessInterface dao;
    private CompleteTripViewModel viewModel;
    private CompleteTripPresenter presenter;
    private CompleteTripInteractor interactor;
    private static final String TEST_JSON_PATH = "test_trips.json";

    @BeforeEach
    public void setup() {
        // Delete test JSON file to start fresh
        File f = new File(TEST_JSON_PATH);
        if (f.exists()) f.delete();

        // Use interface type for dependency injection
        dao = new FileTripDataAccessObject(TEST_JSON_PATH);
        viewModel = new CompleteTripViewModel();
        presenter = new CompleteTripPresenter(viewModel);
        interactor = new CompleteTripInteractor(dao, presenter);

        // Add a test trip
        Trip t = new Trip(
                "t-1",
                "Test Trip",
                "alice",
                "ongoing",
                "2025-12-01 - 2025-12-05",
                "Paris",
                Collections.emptyList(),
                Collections.emptyList(),
                Collections.emptyList()
        );
        dao.saveTrip(t); // Use interface method
    }

    @Test
    public void testCompleteTripSuccess() {
        CompleteTripInputData input = new CompleteTripInputData("t-1");
        interactor.execute(input);

        Trip updatedTrip = dao.getTripById("t-1"); // Use interface method
        assertEquals("completed", updatedTrip.getStatus().toLowerCase());
        assertNull(viewModel.getErrorMessage());
        assertEquals("completed", viewModel.getTripStatus());
        assertEquals("t-1", viewModel.getLastCompletedTripId());
    }

    @Test
    public void testCompleteTripNotFound() {
        CompleteTripInputData input = new CompleteTripInputData("no-such-id");
        interactor.execute(input);

        assertNotNull(viewModel.getErrorMessage());
    }
}