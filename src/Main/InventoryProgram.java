package Main;

import SQLiteDatabase.DatabaseConnection;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *FUTURE ENHANCEMENT: Expand control of duplicated parts within products.
 *                    Potentially restrict which parts can be duplicated.
 *
 * @author Lloyd King
 */
public class InventoryProgram extends Application {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        // Initialize database connection
        DatabaseConnection.connect();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/LoginScreen.fxml"));
        Controllers.LoginController controller = new Controllers.LoginController();
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
}
