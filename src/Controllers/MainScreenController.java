/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import Model.InHouse;
import Model.OutSourced;
import Model.Part;
import Model.Product;
import SQLiteDatabase.PartDAO;
import SQLiteDatabase.ProductDAO;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * RUNTIME ERROR: If no part is selected when the user clicks the modify button.
 *                Corrected by preventing a null from being passed to ModifyPartController.
 *                Implemented in modifyPart().
 *
 * @author Lloyd King
 */
public class MainScreenController implements Initializable {

    private PartDAO partDao;
    private ProductDAO productDao;

    public MainScreenController() {
        this.partDao = new PartDAO();
        this.productDao = new ProductDAO();
    }


    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        generatePartsTable();
        generateProductsTable();

        // Listener for parts search box
        partSearchBox.textProperty().addListener((observable, oldValue, newValue) -> {
            searchForPart();
        });

        // Listener for products search box
        productSearchBox.textProperty().addListener((observable, oldValue, newValue) -> {
            searchForProduct();
        });
    }


    /**
     * Generates Price column in the table and assigns ALL the parts in the Inventory.
     */
    private void generatePartsTable() {
        partInventory.setAll(partDao.getAllParts());

        TableColumn<Part, Double> costCol = formatPrice();
        partsTable.getColumns().addAll(costCol);

        TableColumn<Part, String> specialColumn = new TableColumn<>("Machine ID/Vendor");
        specialColumn.setCellFactory(column -> {
            return new TableCell<Part, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setText(null);
                    } else {
                        Part part = getTableView().getItems().get(getIndex());
                        if (part instanceof InHouse) {
                            setText(String.valueOf(((InHouse) part).getMachineID()));
                        } else if (part instanceof OutSourced) {
                            setText(((OutSourced) part).getCompanyName());
                        }
                    }
                }
            };
        });

        partsTable.getColumns().add(specialColumn);

        partsTable.setItems(partInventory);
        partsTable.refresh();
    }


    /**
     * Generates Price column in the table and assigns ALL the products in the Inventory.
     */
    private void generateProductsTable() {
        productInventory.setAll(productDao.getAllProducts());

       TableColumn<Product, Double> costCol = formatPrice();
       productsTable.getColumns().addAll(costCol);

        productsTable.setItems(productInventory);
        productsTable.refresh();
    }

    /**
     * This is the box where the part search String will be input.
     */
    @FXML
    private TextField partSearchBox;

    /**
     This is the new way of doing the search. It uses the Inventory lookupPart(partName) method.
     */
    @FXML
    private void searchForPart() {
        List<Part> allParts = partDao.getAllParts();
        ObservableList<Part> partsFound = FXCollections.observableArrayList();

        String searchString = partSearchBox.getText().trim();
        for (Part part : allParts) {
            if (String.valueOf(part.getId()).contains(searchString) ||
                    part.getName().toLowerCase().contains(searchString.toLowerCase())) {
                partsFound.add(part);
            }
        }

        partsTable.setItems(partsFound);
    }



    @FXML
    void partSearchBoxKeyPressed(KeyEvent event) {

        if (partSearchBox.getText().isEmpty()) {
            partsTable.setItems(productDao.getAllParts());
        }

    }

    /**
     * This is the box where the product search String will be input.
     */
    @FXML
    private TextField productSearchBox;

    /**
     This is the old way of doing the search. It doesn't use the Inventory lookupProduct(partName) method.
     */
    @FXML
    private void searchForProduct() {
        List<Product> allProducts = productDao.getAllProducts();
        ObservableList<Product> productsFound = FXCollections.observableArrayList();

        String searchString = productSearchBox.getText().trim();
        for (Product product : allProducts) {
            if (String.valueOf(product.getProductID()).contains(searchString) ||
                    product.getName().toLowerCase().contains(searchString.toLowerCase())) {
                productsFound.add(product);
            }
        }

        productsTable.setItems(productsFound);
    }

    @FXML
    void productSearchBoxKeyPressed(KeyEvent event) {

        if (productSearchBox.getText().isEmpty()) {
            productsTable.setItems(productDao.getAllProducts());
        }
    }

    /**
     * This is the table where ALL the parts in the Inventory will be shown.
     */
    @FXML
    private TableView<Part> partsTable;
    /**
     * This is the table where ALL the products in the Inventory will be shown.
     */
    @FXML
    private TableView<Product> productsTable;
    private ObservableList<Part> partInventory = FXCollections.observableArrayList();
    private ObservableList<Product> productInventory = FXCollections.observableArrayList();
    private ObservableList<Part> partsInventorySearch = FXCollections.observableArrayList();
    private ObservableList<Product> productInventorySearch = FXCollections.observableArrayList();




    @FXML
    private void exitProgram(ActionEvent event
    ) {
        Platform.exit();
    }

    @FXML
    private void exitProgramButton(MouseEvent event
    ) {
        Platform.exit();
    }

    /**
     * Go to the Add Part Screen
     */
    @FXML
    private void addPart(MouseEvent event
    ) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/AddPart.fxml"));
            AddPartController controller = new AddPartController();

            loader.setController(controller);
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {

        }
    }

    /**
     * Go to the Modify Part Screen
     */
    @FXML
    private void modifyPart(MouseEvent event) {
        try {
            Part selected = partsTable.getSelectionModel().getSelectedItem();
            if (partInventory.isEmpty()) {
                errorWindow(1);
                return;
            }
            if (!partInventory.isEmpty() && selected == null) {
                errorWindow(2);
                return;
            } else {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/ModifyPart.fxml"));
                ModifyPartController controller = new ModifyPartController(selected);

                loader.setController(controller);
                Parent root = loader.load();
                Scene scene = new Scene(root);
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(scene);
                stage.setResizable(false);
                stage.show();
            }
        } catch (IOException e) {
            e.printStackTrace(); // It's good to print the stack trace for debugging
        }
    }



    @FXML
    private void deletePart(MouseEvent event) {
        Part removePart = partsTable.getSelectionModel().getSelectedItem();
        if (partInventory.isEmpty()) {
            errorWindow(1);
            return;
        }
        if (!partInventory.isEmpty() && removePart == null) {
            errorWindow(2);
            return;
        } else {
            boolean confirm = confirmationWindow(removePart.getName());
            if (!confirm) {
                return;
            }
            partDao.deletePart(removePart.getId()); // Using DAO to delete the part
            partInventory.remove(removePart);
            partsTable.refresh();
        }
    }


    /**
     * Go to the Add Product Screen
     */
    @FXML
    private void addProduct(MouseEvent event
    ) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/AddProduct.fxml"));
            AddProductController controller = new AddProductController();

            loader.setController(controller);
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();

        } catch (IOException e) {

        }
    }

    /**
     * Go to the Modify Product Screen
     */
    @FXML
    private void modifyProduct(MouseEvent event
    ) {
        try {
            Product productSelected = productsTable.getSelectionModel().getSelectedItem();
            if (productInventory.isEmpty()) {
                errorWindow(1);
                return;
            }
            if (!productInventory.isEmpty() && productSelected == null) {
                errorWindow(2);
                return;

            } else {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/ModifyProduct.fxml"));
                ModifyProductController controller = new ModifyProductController(productSelected);

                loader.setController(controller);
                Parent root = loader.load();
                Scene scene = new Scene(root);
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(scene);
                stage.setResizable(false);
                stage.show();
            }
        } catch (IOException e) {

        }
    }

    @FXML
    private void deleteProduct(MouseEvent event
    ) {
        boolean deleted = false;
        Product removeProduct = productsTable.getSelectionModel().getSelectedItem();
        if (productInventory.isEmpty()) {
            errorWindow(1);
            return;
        }
        if (!productInventory.isEmpty() && removeProduct == null) {
            errorWindow(2);
            return;
        }
        if (removeProduct.getPartsListSize() == 0) {
            boolean confirm = confirmDelete(removeProduct.getName());
            if (!confirm) {
                return;
            }
        } else {
            infoWindow(removeProduct.getName());
            return;
        }
        productDao.deleteProduct(removeProduct.getProductID());
        productInventory.remove(removeProduct);
        productsTable.setItems(productInventory);
        productsTable.refresh();
    }

    private void errorWindow(int code) {
        if (code == 1) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Empty Inventory!");
            alert.setContentText("There's nothing to select!");
            alert.showAndWait();
        }
        if (code == 2) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Invalid Selection");
            alert.setContentText("You must select an item!");
            alert.showAndWait();
        }

    }

    private boolean confirmationWindow(String name) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Delete part");
        alert.setHeaderText("Are you sure you want to delete: " + name);
        alert.setContentText("Click ok to confirm");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            return true;
        } else {
            return false;
        }
    }

    private boolean confirmDelete(String name) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Delete product");
        alert.setHeaderText("Are you sure you want to delete: " + name );
        alert.setContentText("Click ok to confirm");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            return true;
        } else {
            return false;
        }
    }

    private void infoWindow(String name) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Confirmed");
        alert.setHeaderText(null);
        alert.setContentText(name + " still has parts assigned to it and has NOT been deleted!");
        alert.showAndWait();
    }

    private <T> TableColumn<T, Double> formatPrice() {
        TableColumn<T, Double> costCol = new TableColumn("Price");
        costCol.setCellValueFactory(new PropertyValueFactory<>("Price"));
        // Format as currency
        costCol.setCellFactory((TableColumn<T, Double> column) -> {
            return new TableCell<T, Double>() {
                @Override
                protected void updateItem(Double item, boolean empty) {
                    if (!empty) {
                        setText("$" + String.format("%10.2f", item));
                    } else {
                        setText("");
                    }
                }
            };
        });
        return costCol;
    }

    @FXML
    private TextArea reportsTextArea;

    @FXML
    private void generateReport() {
        String report = getFormattedReportForCSV();
        reportsTextArea.setText(report);
    }



    @FXML
    private double calculateTotalPartsPrice() {
        double total = partDao.getAllParts().stream()
                .mapToDouble(part -> part.getPrice() * part.getStock())
                .sum();
        reportsTextArea.setText("Total Parts Price: $" + String.format("%.2f", total) + "\n");
        return total;
    }


    @FXML
    private double calculateTotalProductsPrice() {
        double total = productDao.getAllProducts().stream()
                .mapToDouble(product -> product.getPrice() * product.getInStock())
                .sum();
        reportsTextArea.setText("Total Products Price: $" + String.format("%.2f", total) + "\n");
        return total;
    }

    @FXML
    private void showOutsourcedParts() {
        Map<String, Long> vendorCounts = partDao.getAllParts().stream()
                .filter(p -> p instanceof OutSourced)
                .map(p -> ((OutSourced) p).getCompanyName())
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        StringBuilder sb = new StringBuilder("Outsourced Parts Count by Vendor:\n");
        vendorCounts.forEach((vendor, count) -> sb.append(vendor).append(": ").append(count).append("\n"));

        reportsTextArea.setText(sb.toString());
    }


    @FXML
    private void showTotalStockCount() {
        int totalPartsCount = partDao.getAllParts().stream().mapToInt(Part::getStock).sum();
        int totalProductsCount = productDao.getAllProducts().stream().mapToInt(Product::getInStock).sum();

        reportsTextArea.setText("Total Parts Count: " + totalPartsCount + "\nTotal Products Count: " + totalProductsCount);
    }

    @FXML
    private void exportToCSV() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Report");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        File file = fileChooser.showSaveDialog(null);

        if (file != null) {
            try (PrintWriter writer = new PrintWriter(file)) {
                String reportData = getFormattedReportForCSV();
                writer.println(reportData);
            } catch (IOException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Could not save file");
                alert.setContentText("An error occurred while saving the file.");
                alert.showAndWait();
            }
        }
    }

    private String getFormattedReportForCSV() {
        StringBuilder reportBuilder = new StringBuilder();

        // Add Title and Date-Time Stamp
        reportBuilder.append("Inventory Report\n");
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        reportBuilder.append("Generated on: ").append(now.format(formatter)).append("\n\n");

        // Define column widths based on the expected maximum length of data in each column
        int idWidth = 10;
        int nameWidth = 25;
        int priceWidth = 15;
        int stockWidth = 10;

        // Add Parts Data
        reportBuilder.append("Parts:\n");
        String headerFormat = "%-" + idWidth + "s %-" + nameWidth + "s %-" + priceWidth + "s %-" + stockWidth + "s\n";
        reportBuilder.append(String.format(headerFormat, "ID", "Name", "Price", "Stock"));

        String rowFormat = "%-" + idWidth + "d %-" + nameWidth + "s %-" + priceWidth + ".2f %-" + stockWidth + "d\n";
        for (Part part : partDao.getAllParts()) {
            reportBuilder.append(String.format(rowFormat,
                    part.getId(),
                    part.getName(),
                    part.getPrice(),
                    part.getStock()));
        }

        // Add Products Data
        reportBuilder.append("\nProducts:\n");
        reportBuilder.append(String.format(headerFormat, "ID", "Name", "Price", "Stock"));
        for (Product product : productDao.getAllProducts()) {
            reportBuilder.append(String.format(rowFormat,
                    product.getProductID(),
                    product.getName(),
                    product.getPrice(),
                    product.getInStock()));
        }

        return reportBuilder.toString();
    }


    private Map<String, Integer> getOutsourcedPartsCount() {
        Map<String, Integer> countMap = new HashMap<>();
        for (Part part : partDao.getAllParts()) {
            if (part instanceof OutSourced) {
                OutSourced outsourcedPart = (OutSourced) part;
                String vendorName = outsourcedPart.getCompanyName();
                countMap.merge(vendorName, part.getStock(), Integer::sum);
            }
        }
        return countMap;
    }


}
