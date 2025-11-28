package use_case.login;

import java.io.File;

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import data_access.FileUserDataAccessObject;
import entity.User;
import entity.UserFactory;

class LoginInteractorTest {

    private static final String TEST_JSON_PATH = "test_login_users.json";
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
        LoginInputData inputData = new LoginInputData("Paul", "password");

        // For the success test, we need to add Paul to the data access repository before we log in.
        User user = factory.create("Paul", "password");
        userRepository.save(user);

        // This creates a successPresenter that tests whether the test case is as we expect.
        LoginOutputBoundary successPresenter = new LoginOutputBoundary() {
            @Override
            public void prepareSuccessView(LoginOutputData user) {
                assertEquals("Paul", user.getUsername());
                assertEquals("Paul", LoginInteractorTest.this.userRepository.getCurrentUsername());
            }

            @Override
            public void prepareFailView(String error) {
                fail("Use case failure is unexpected.");
            }

            @Override
            public void switchToSignUpView() {

            }
        };

        LoginInputBoundary interactor = new LoginInteractor(userRepository, successPresenter);
        interactor.execute(inputData);
    }


    @Test
    void failurePasswordMismatchTest() {
        LoginInputData inputData = new LoginInputData("Paul", "wrong");

        // For this failure test, we need to add Paul to the data access repository before we log in, and
        // the passwords should not match.
        User user = factory.create("Paul", "password");
        userRepository.save(user);

        // This creates a presenter that tests whether the test case is as we expect.
        LoginOutputBoundary failurePresenter = new LoginOutputBoundary() {
            @Override
            public void prepareSuccessView(LoginOutputData user) {
                // this should never be reached since the test case should fail
                fail("Use case success is unexpected.");
            }

            @Override
            public void prepareFailView(String error) {
                assertEquals("Incorrect password for \"Paul\".", error);
            }

            @Override
            public void switchToSignUpView() {

            }
        };

        LoginInputBoundary interactor = new LoginInteractor(userRepository, failurePresenter);
        interactor.execute(inputData);
    }

    @Test
    void failureUserDoesNotExistTest() {
        LoginInputData inputData = new LoginInputData("Paul", "password");

        // Add Paul to the repo so that when we check later they already exist

        // This creates a presenter that tests whether the test case is as we expect.
        LoginOutputBoundary failurePresenter = new LoginOutputBoundary() {
            @Override
            public void prepareSuccessView(LoginOutputData user) {
                // this should never be reached since the test case should fail
                fail("Use case success is unexpected.");
            }

            @Override
            public void prepareFailView(String error) {
                assertEquals("Paul: Account does not exist.", error);
            }

            @Override
            public void switchToSignUpView() {

            }
        };

        LoginInputBoundary interactor = new LoginInteractor(userRepository, failurePresenter);
        interactor.execute(inputData);
    }
}