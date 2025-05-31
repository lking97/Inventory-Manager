package SQLiteDatabase;

import Model.Part;
import Model.Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static SQLiteDatabase.PartDAO.allParts;

public class ProductDAO {

    public static ObservableList<Product> allProducts = FXCollections.observableArrayList();
    public void addProduct(Product product) {
        String sql = "INSERT INTO Products(productID, name, price, inStock, min, max) VALUES(?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, product.getProductID());
            pstmt.setString(2, product.getName());
            pstmt.setDouble(3, product.getPrice());
            pstmt.setInt(4, product.getInStock());
            pstmt.setInt(5, product.getMin());
            pstmt.setInt(6, product.getMax());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public int findNextAvailableId() {
        String sql = "SELECT MAX(productID) FROM Products";
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


    public ObservableList<Product> getAllProducts() {
        String sql = "SELECT * FROM Products";
        List<Product> products = new ArrayList<>();

        try (Connection conn = DatabaseConnection.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Product product = new Product(
                        rs.getInt("productID"),
                        rs.getString("name"),
                        rs.getDouble("price"),
                        rs.getInt("inStock"),
                        rs.getInt("min"),
                        rs.getInt("max"));
                // Retrieve associated parts for each product
                List<Part> associatedParts = getPartsForProduct(product.getProductID());
                product.setAssociatedParts(new ArrayList<>(associatedParts)); // Assuming setAssociatedParts accepts a List<Part>

                products.add(product);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return FXCollections.observableArrayList(products);
    }

    public void updateProduct(Product product) {
        String sql = "UPDATE Products SET name = ?, price = ?, inStock = ?, min = ?, max = ? WHERE productID = ?";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, product.getName());
            pstmt.setDouble(2, product.getPrice());
            pstmt.setInt(3, product.getInStock());
            pstmt.setInt(4, product.getMin());
            pstmt.setInt(5, product.getMax());
            pstmt.setInt(6, product.getProductID());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void deleteProduct(int productId) {
        String sql = "DELETE FROM Products WHERE productID = ?";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, productId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addPartToProduct(int productId, int partId) {
        String sqlLink = "INSERT INTO ProductsParts (productID, partID) VALUES (?, ?)";

        String sqlCheckStock = "SELECT stock FROM Parts WHERE id = ?";
        Connection conn = null;
        try {
            conn = DatabaseConnection.connect();
            PreparedStatement pstmtCheckStock = conn.prepareStatement(sqlCheckStock);

            // Check current stock level
            pstmtCheckStock.setInt(1, partId);
            ResultSet rs = pstmtCheckStock.executeQuery();
            if (rs.next()) {
                int currentStock = rs.getInt("stock");
                if (currentStock <= 0) {
                    // Handle the case where stock is not sufficient (e.g., throw an exception or return)
                    System.out.println("Insufficient stock for part ID: " + partId);
                    return;
                }
            } else {
                // Handle the case where part does not exist
                System.out.println("Part ID not found: " + partId);
                return;
            }

            // ... continue with linking the part to the product and updating the stock ...

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            // ... error handling ...
        }

        String sqlUpdateStock = "UPDATE Parts SET stock = stock - 1 WHERE id = ?";


        try {
            conn = DatabaseConnection.connect();
            PreparedStatement pstmtLink = conn.prepareStatement(sqlLink);
            PreparedStatement pstmtUpdateStock = conn.prepareStatement(sqlUpdateStock);

            conn.setAutoCommit(false); // Start transaction

            // Link the part to the product
            pstmtLink.setInt(1, productId);
            pstmtLink.setInt(2, partId);
            pstmtLink.executeUpdate();

            // Update the stock level of the part
            pstmtUpdateStock.setInt(1, partId);
            pstmtUpdateStock.executeUpdate();

            conn.commit(); // Commit transaction
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            if (conn != null) {
                try {
                    conn.rollback(); // Rollback transaction in case of error
                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true); // Reset auto-commit
                    conn.close(); // Close connection
                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        }
    }



    public void removePartFromProduct(int productId, int partId) {
        String sqlUnlink = "DELETE FROM ProductsParts WHERE productID = ? AND partID = ?";
        String sqlUpdateStock = "UPDATE Parts SET stock = stock + 1 WHERE id = ?";

        Connection conn = null;
        try {
            conn = DatabaseConnection.connect();
            PreparedStatement pstmtUnlink = conn.prepareStatement(sqlUnlink);
            PreparedStatement pstmtUpdateStock = conn.prepareStatement(sqlUpdateStock);

            conn.setAutoCommit(false); // Start transaction

            // Remove the link between the part and the product
            pstmtUnlink.setInt(1, productId);
            pstmtUnlink.setInt(2, partId);
            pstmtUnlink.executeUpdate();

            // Update the stock level of the part
            pstmtUpdateStock.setInt(1, partId);
            pstmtUpdateStock.executeUpdate();

            conn.commit(); // Commit transaction
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            try {
                if (conn != null) {
                    conn.rollback(); // Rollback transaction in case of error
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        } finally {
            try {
                if (conn != null) {
                    conn.setAutoCommit(true); // Reset auto-commit
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    public List<Part> getPartsForProduct(int productId) {
        String sql = "SELECT Parts.* FROM Parts INNER JOIN ProductsParts ON Parts.id = ProductsParts.partID WHERE ProductsParts.productID = ?";
        List<Part> parts = new ArrayList<>();

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, productId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Part part = new Part(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getDouble("price"),
                        rs.getInt("stock"),
                        rs.getInt("min"),
                        rs.getInt("max"));
                parts.add(part);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return parts;
    }

    public int productListSize() {
        return allProducts.size();
    }

    public Product lookUpProduct(int productToSearch) {
        if (!allProducts.isEmpty()) {
            for (int i = 0; i < allProducts.size(); i++) {
                if (allProducts.get(i).getProductID() == productToSearch) {
                    return allProducts.get(i);
                }
            }
        }
        return null;
    }

    public ObservableList<Part> lookUpProduct(String productNameToLookUp) {
        return null;
    }
    public static ObservableList<Part> getAllParts() {
        return allParts;
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



}
