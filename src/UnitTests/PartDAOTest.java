package UnitTests;

import Model.InHouse;
import Model.Part;
import SQLiteDatabase.DatabaseConnection;
import SQLiteDatabase.PartDAO;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;

class PartDAOTest {

    @Test
    public void testAddPart() throws SQLException {
        Connection mockConnection = Mockito.mock(Connection.class);
        PreparedStatement mockStatement = Mockito.mock(PreparedStatement.class);

        Mockito.when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);

        try (MockedStatic<DatabaseConnection> mockedStatic = Mockito.mockStatic(DatabaseConnection.class)) {
            mockedStatic.when(DatabaseConnection::connect).thenReturn(mockConnection);

            PartDAO dao = new PartDAO();
            Part mockPart = new InHouse(1, "Test Part", 0.0, 0, 0, 0, 0); // Example part, adjust as needed.

            dao.addPart(mockPart);

            Mockito.verify(mockConnection).prepareStatement(anyString());
            Mockito.verify(mockStatement).executeUpdate();
        }
    }

    @Test
    public void testDeletePart() throws SQLException {
        Connection mockConnection = Mockito.mock(Connection.class);
        PreparedStatement mockStatement = Mockito.mock(PreparedStatement.class);

        Mockito.when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);

        try (MockedStatic<DatabaseConnection> mockedStatic = Mockito.mockStatic(DatabaseConnection.class)) {
            mockedStatic.when(DatabaseConnection::connect).thenReturn(mockConnection);

            PartDAO dao = new PartDAO();
            int partIdToDelete = 1; // Example part ID to delete.

            dao.deletePart(partIdToDelete);

            String expectedSQL = "DELETE FROM Parts WHERE id = ?";
            Mockito.verify(mockConnection).prepareStatement(eq(expectedSQL));
            Mockito.verify(mockStatement).setInt(1, partIdToDelete);
            Mockito.verify(mockStatement).executeUpdate();
        }
    }
}
