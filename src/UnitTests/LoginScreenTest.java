package UnitTests;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.control.TextInputControlMatchers.hasText;

@ExtendWith(ApplicationExtension.class)
public class LoginScreenTest extends FxRobot{

    @Start
    private void start(Stage stage) throws Exception {
        Parent mainNode = FXMLLoader.load(getClass().getResource("/Views/LoginScreen.fxml")); // Update path
        stage.setScene(new Scene(mainNode));
        stage.show();
        stage.toFront();
    }

    @Test
    void testInputIntoUsernameAndPasswordFields() {
        // Input text into the username and password fields
        clickOn("#usernameTextField").write("testUser");
        clickOn("#passwordTextField").write("testPass");

        // Verify that the username and password fields contain the inputted text
        verifyThat("#usernameTextField", hasText("testUser"));
        verifyThat("#passwordTextField", hasText("testPass"));
    }

    @Test
    void testLoginButtonAction() {
        // Assume that clicking the login button should change the scene, display a message, or some other verifiable action occurs
        clickOn("#usernameTextField").write("validUser");
        clickOn("#passwordTextField").write("validPass");
        clickOn("#loginButton");

        // Here you should verify the expected outcome after login.
        // This might involve checking for a new scene, a specific element becoming visible, etc.
        // Example (adjust according to actual application behavior):
        // verifyThat("#loggedInIndicator", isVisible());
    }

}

