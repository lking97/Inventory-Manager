package SQLiteDatabase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:sqlite:Inventory.db";

    public static Connection connect() {
        Connection conn = null;
        try {
            // Load the JDBC driver
            Class.forName("org.sqlite.JDBC");
            // Establish the connection
            conn = DriverManager.getConnection(URL);
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }
}
