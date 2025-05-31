/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import Model.Part;
import Model.Product;
import SQLiteDatabase.PartDAO;
import SQLiteDatabase.ProductDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * FXML Controller class
 *
 * @author Lloyd King
 */
public class AddProductController implements Initializable {

    private ProductDAO productDAO;
    private PartDAO partDao;

    @FXML
    private TextField productID;
    @FXML
    private TextField name;
    @FXML
    private TextField price;
    @FXML
    private TextField count;
    @FXML
    private TextField min;
    @FXML
    private TextField max;
    @FXML
    private TextField search;
    @FXML
    private TableView<Part> partSearchTable;
    @FXML
    private TableView<Part> assocPartsTable;
    private ObservableList<Part> partsInventory = FXCollections.observableArrayList();
    private ObservableList<Part> partsInventorySearch = FXCollections.observableArrayList();
    private ObservableList<Part> assocPartList = FXCollections.observableArrayList();

    public AddProductController() {
        this.productDAO = new ProductDAO();
        this.partDao = new PartDAO();

    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        generateProductID();
        populateSearchTable();
    }

    private void generateProductID() {
        int nextAvailableId = productDAO.findNextAvailableId();
        productID.setText(Integer.toString(nextAvailableId));
    }

    private boolean generateNum(Integer num) {
        Part match = productDAO.lookUpPart (num);
        return match != null;
    }


    private void populateSearchTable() {
        partsInventory.setAll(partDao.getAllParts());

        TableColumn<Part, Double> costCol = formatPrice();
        partSearchTable.getColumns().addAll(costCol);

        partSearchTable.setItems(partsInventory);
        partSearchTable.refresh();
    }

    @FXML
    private void searchForPart(MouseEvent event) {
        List<Part> allParts = partDao.getAllParts();
        ObservableList<Part> partsFound = FXCollections.observableArrayList();

        String searchString = search.getText().trim();
        for (Part part : allParts) {
            if (String.valueOf(part.getId()).contains(searchString) ||
                    part.getName().toLowerCase().contains(searchString.toLowerCase())) {
                partsFound.add(part);
            }
        }

        partSearchTable.setItems(partsFound);
    }

    @FXML
    private void addPart(MouseEvent event) {
        Part addPart = partSearchTable.getSelectionModel().getSelectedItem();
        boolean repeatedItem = false;

        if (addPart != null) {
            int id = addPart.getId();
            for (int i = 0; i < assocPartList.size(); i++) {
                if (assocPartList.get(i).getId() == id) {
                    AlertMessage.errorProduct(2, null);
                    repeatedItem = true;
                }
            }

            if (!repeatedItem) {
                assocPartList.add(addPart);

            }

            TableColumn<Part, Double> costCol = formatPrice();
            assocPartsTable.getColumns().addAll(costCol);

            assocPartsTable.setItems(assocPartList);
        }
    }

    @FXML
    private void saveAddProduct(MouseEvent event
    ) {
        resetFieldsStyle();
        boolean end = false;
        TextField[] fieldCount = {count, price, min, max};
        double minCost = 0;
        for (int i = 0; i < assocPartList.size(); i++) {
            minCost += assocPartList.get(i).getPrice();
        }
        if (name.getText().trim().isEmpty() || name.getText().trim().toLowerCase().equals("part name")) {
            AlertMessage.errorProduct(4, name);
            return;
        }
        for (TextField fieldCount1 : fieldCount) {
            boolean valueError = checkValue(fieldCount1);
            if (valueError) {
                end = true;
                break;
            }
            boolean typeError = checkType(fieldCount1);
            if (typeError) {
                end = true;
                break;
            }
        }
        if (Integer.parseInt(min.getText().trim()) > Integer.parseInt(max.getText().trim())) {
            AlertMessage.errorProduct(10, min);
            return;
        }
        if (Integer.parseInt(count.getText().trim()) < Integer.parseInt(min.getText().trim())) {
            AlertMessage.errorProduct(8, count);
            return;
        }
        if (Integer.parseInt(count.getText().trim()) > Integer.parseInt(max.getText().trim())) {
            AlertMessage.errorProduct(9, count);
            return;
        }
        if (Double.parseDouble(price.getText().trim()) < minCost) {
            AlertMessage.errorProduct(6, price);
            return;
        }
        if (assocPartList.isEmpty()) {
            AlertMessage.errorProduct(7, null);
            return;
        }

        saveProduct();
        mainScreen(event);

    }

    @FXML
    private void cancelAddProduct(MouseEvent event
    ) {
        boolean cancel = AlertMessage.cancel();
        if (cancel) {
            mainScreen(event);
        }
    }

    @FXML
    private void deletePart(MouseEvent event
    ) {
        Part removePart = assocPartsTable.getSelectionModel().getSelectedItem();
        boolean deleted = false;
        if (removePart != null) {
            boolean remove = AlertMessage.confirmationWindow(removePart.getName());
            if (remove) {
                assocPartList.remove(removePart);
                assocPartsTable.refresh();
            }
        } else {
            return;
        }
        if (deleted) {
            AlertMessage.infoWindow(1, removePart.getName());
        } else {
            AlertMessage.infoWindow(2, "");
        }

    }

    private void saveProduct() {
        Product product = new Product(Integer.parseInt(productID.getText().trim()), name.getText().trim(), Double.parseDouble(price.getText().trim()),
                Integer.parseInt(count.getText().trim()), Integer.parseInt(min.getText().trim()), Integer.parseInt(max.getText().trim()));
        for (int i = 0; i < assocPartList.size(); i++) {
            product.addAssociatedPart(assocPartList.get(i));
        }

        productDAO.addProduct(product);

    }

    private void fieldError(TextField field) {
        if (field != null) {
            field.setStyle("-fx-border-color: red");
        }
    }

    private void resetFieldsStyle() {
        name.setStyle("-fx-border-color: lightgray");
        count.setStyle("-fx-border-color: lightgray");
        price.setStyle("-fx-border-color: lightgray");
        min.setStyle("-fx-border-color: lightgray");
        max.setStyle("-fx-border-color: lightgray");

    }

    private void mainScreen(Event event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/MainScreen.fxml"));
            MainScreenController controller = new MainScreenController();

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

    private boolean checkValue(TextField field) {
        boolean error = false;
        try {
            if (field.getText().trim().isEmpty() || field.getText().trim() == null) {
                AlertMessage.errorProduct(1, field);
                return true;
            }
            if (field == price && Double.parseDouble(field.getText().trim()) < 0) {
                AlertMessage.errorProduct(5, field);
                error = true;
            }
        } catch (NumberFormatException e) {
            error = true;
            AlertMessage.errorProduct(3, field);
            System.out.println(e);

        }
        return error;
    }

    private boolean checkType(TextField field) {

        if (field == price & !field.getText().trim().matches("\\d+(\\.\\d+)?")) {
            AlertMessage.errorProduct(3, field);
            return true;
        }
        if (field != price & !field.getText().trim().matches("[0-9]*")) {
            AlertMessage.errorProduct(3, field);
            return true;
        }
        return false;

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
                    }
                    else{
                        setText("");
                    }
                }
            };
        });
        return costCol;
    }

}
