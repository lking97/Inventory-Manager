package SQLiteDatabase;

import Model.InHouse;
import Model.OutSourced;
import Model.Part;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PartDAO {


    public static ObservableList<Part> allParts = FXCollections.observableArrayList();
    public void addPart(Part part) {
        String sql = "INSERT INTO Parts(name, price, stock, min, max, type, machineID, companyName) VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, part.getName());
            pstmt.setDouble(2, part.getPrice());
            pstmt.setInt(3, part.getStock());
            pstmt.setInt(4, part.getMin());
            pstmt.setInt(5, part.getMax());
            if (part instanceof InHouse) {
                pstmt.setString(6, "InHouse");
                pstmt.setInt(7, ((InHouse) part).getMachineID());
                pstmt.setNull(8, Types.VARCHAR); // Assuming null for companyName
            } else if (part instanceof OutSourced) {
                pstmt.setString(6, "OutSourced");
                pstmt.setNull(7, Types.INTEGER); // Assuming null for machineID
                pstmt.setString(8, ((OutSourced) part).getCompanyName());
            }
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public boolean isPartIdTaken(int partId) {
        String sql = "SELECT COUNT(id) FROM Parts WHERE id = ?";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, partId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public int findNextAvailableId() {
        String sql = "SELECT MAX(id) FROM Parts";
        try (Connection conn = DatabaseConnection.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                int maxId = rs.getInt(1); // Get the maximum productID
                if (rs.wasNull()) {
                    return 1; // If no products, start with ID 1
                }
                return maxId + 1; // Otherwise, increment the highest ID by 1
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return 1; // Default to 1 if no products are found or in case of an error
    }


    public static List<Part> getAllParts() {
        String sql = "SELECT * FROM Parts";
        List<Part> parts = new ArrayList<>();

        try (Connection conn = DatabaseConnection.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                double price = rs.getDouble("price");
                int stock = rs.getInt("stock");
                int min = rs.getInt("min");
                int max = rs.getInt("max");
                String type = rs.getString("type");

                Part part;
                if ("InHouse".equals(type)) {
                    int machineId = rs.getInt("machineID");
                    part = new InHouse(id, name, price, stock, min, max, machineId);
                } else {
                    String companyName = rs.getString("companyName");
                    part = new OutSourced(id, name, price, stock, min, max, companyName);
                }
                parts.add(part);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return parts;
    }

    public void updatePart(Part part) {
        String sql = "UPDATE Parts SET name = ?, price = ?, stock = ?, min = ?, max = ?, type = ?, machineID = ?, companyName = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, part.getName());
            pstmt.setDouble(2, part.getPrice());
            pstmt.setInt(3, part.getStock());
            pstmt.setInt(4, part.getMin());
            pstmt.setInt(5, part.getMax());
            if (part instanceof InHouse) {
                pstmt.setString(6, "InHouse");
                pstmt.setInt(7, ((InHouse) part).getMachineID());
                pstmt.setNull(8, Types.VARCHAR); // Assuming null for companyName
            } else if (part instanceof OutSourced) {
                pstmt.setString(6, "OutSourced");
                pstmt.setNull(7, Types.INTEGER); // Assuming null for machineID
                pstmt.setString(8, ((OutSourced) part).getCompanyName());
            }
            pstmt.setInt(9, part.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    public void deletePart(int partId) {
        String sql = "DELETE FROM Parts WHERE id = ?";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, partId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public ObservableList<Part> lookUpPart(String partNameToLookUp) {
        if (!allParts.isEmpty()) {
            ObservableList searchPartsList = FXCollections.observableArrayList();
            for (Part p : getAllParts()) {
                if (p.getName().contains(partNameToLookUp)) {
                    searchPartsList.add(p);
                }
            }
            return searchPartsList;
        }
        return null;
    }

    public Part lookUpPart(int partToLookUp) {
        if (!allParts.isEmpty()) {
            for (int i = 0; i < allParts.size(); i++) {
                if (allParts.get(i).getId() == partToLookUp) {
                    return allParts.get(i);
                }
            }

        }
        return null;
    }

    public int partListSize() {
        return allParts.size();
    }

}

