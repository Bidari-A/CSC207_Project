package use_case.logout;

import java.io.File;

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import data_access.FileUserDataAccessObject;
import entity.User;
import entity.UserFactory;

class LogoutInteractorTest {

    private static final String TEST_JSON_PATH = "test_users.json";
    private FileUserDataAccessObject userRepository;
    private UserFactory factory;

    @BeforeEach
    void setUp() {
        // Clean up any existing test file
        File testFile = new File(TEST_JSON_PATH);
        if (testFile.exists()) {
            testFile.delete();
        }

        factory = new UserFactory();
        userRepository = new FileUserDataAccessObject(TEST_JSON_PATH, factory);
    }

    @AfterEach
    void tearDown() {
        // Clean up test file after each test
        File testFile = new File(TEST_JSON_PATH);
        if (testFile.exists()) {
            testFile.delete();
        }
    }

    @Test
    void successTest() {
        // For the success test, we need to add Paul to the data access repository before we log in.
        User user = factory.create("Paul", "password");
        userRepository.save(user);
        userRepository.setCurrentUsername("Paul");

        // This creates a successPresenter that tests whether the test case is as we expect.
        LogoutOutputBoundary successPresenter = new LogoutOutputBoundary() {
            @Override
            public void prepareSuccessView(LogoutOutputData user) {
                assertEquals("Paul", user.getUsername());
                assertNull(userRepository.getCurrentUsername());
            }
        };

        LogoutInputBoundary interactor = new LogoutInteractor(userRepository, successPresenter);
        interactor.execute();
        assertNull(userRepository.getCurrentUsername());
    }

}