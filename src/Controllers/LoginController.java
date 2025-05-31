package Controllers;

import SQLiteDatabase.DatabaseConnection;
import Model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.Date;
import java.util.Optional;

public class LoginController {

    String currentUser = "0";

    @FXML
    private TextField usernameTextField;
    @FXML
    private PasswordField passwordTextField;
    @FXML
    private Button loginButton;

    @FXML
    private void handleUsernameTextField(ActionEvent event) {
        // Method implementation
    }

    @FXML
    private void handlePasswordTextField(ActionEvent event) {
        // Method implementation
    }

    @FXML
    private void handleLoginButton(ActionEvent event) throws SQLException, IOException {
        String usernameInput = usernameTextField.getText();
        String passwordInput = passwordTextField.getText();

        if (isValidPassword(usernameInput, passwordInput)) {
            successfulLogIn(usernameTextField.getText());

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/MainScreen.fxml"));
            MainScreenController controller = new MainScreenController();
            loader.setController(controller);
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } else {
            failedLogIn(usernameTextField.getText());
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Login Failed");
            alert.setHeaderText("Incorrect Username or Password");
            alert.setContentText("Please check your credentials and try again.");
            Optional<ButtonType> result = alert.showAndWait();
        }
    }

    private boolean isValidPassword(String username, String password) {
        String sql = "SELECT * FROM users WHERE User_Name=?";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            try (ResultSet result = ps.executeQuery()) {
                if (result.next()) {
                    String dbPassword = result.getString("Password");

                    if (dbPassword.equals(password)) {
                        currentUser = username;
                        User.setUserID(result.getInt("User_ID"));
                        return true;
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Error querying the database: " + e.getMessage());
            // Handle exception
        }
        return false;
    }



    private void successfulLogIn(String user) throws IOException {
        try {
            String fileName = "login_activity.txt";
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true));
            Date date = new Date();
            Timestamp ts = new Timestamp(date.getTime());
            writer.append("\nSuccesful login: " + user + " at "+ ts);
            writer.flush();
            writer.close();
        } catch  (IOException e) {
            System.out.println("Error: " + e);
        };
    };

    private void failedLogIn(String user) throws IOException {
        try {
            String fileName = "login_activity.txt";
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true));
            Date date = new Date();
            Timestamp ts = new Timestamp(date.getTime());
            writer.append("\nWRONG login: " + user + " at "+ ts);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            System.out.println("Error: " + e);
        };
    };
}
