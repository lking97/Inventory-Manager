/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import Model.InHouse;
import Model.OutSourced;
import SQLiteDatabase.PartDAO;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * FXML Controller class
 *
 * @author Lloyd King
 */
public class AddPartController implements Initializable {
    private PartDAO partDao;

    @FXML
    private RadioButton inHouseRadio;
    @FXML
    private RadioButton outSourcedRadio;
    @FXML
    private TextField id;
    @FXML
    private TextField name;
    @FXML
    private TextField count;
    @FXML
    private TextField price;
    @FXML
    private TextField max;
    @FXML
    private TextField company;
    @FXML
    private Label companyLabel;
    @FXML
    private TextField min;

    public AddPartController() {
        this.partDao = new PartDAO();

    }

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        generatePartID();
        resetFields();
    }

    private void resetFields() {
        name.setText("Part Name");
        count.setText("Inv Count");
        price.setText("Part Price");
        min.setText("Min");
        max.setText("Max");
        company.setText("Machine ID");
        companyLabel.setText("Machine ID");
        inHouseRadio.setSelected(true);
    }

    private void generatePartID() {
        int nextAvailableId = partDao.findNextAvailableId();
        id.setText(Integer.toString(nextAvailableId));
    }

    private boolean verifyIfTaken(Integer num) {
        return partDao.isPartIdTaken(num);
    }
    @FXML
    private void selectInHouse(MouseEvent event) {
        companyLabel.setText("Machine ID");
        company.setText("Machine ID");
    }

    @FXML
    private void selectOutSourced(MouseEvent event) {
        companyLabel.setText("Company Name");
        company.setText("Company Name");
    }

    @FXML
    private void idDisabled(MouseEvent event) {
    }

    @FXML
    private void cancelAddPart(MouseEvent event) {
        boolean cancel = AlertMessage.cancel();
        if (cancel) {
            mainScreen(event);
        }
    }

    @FXML
    private void saveAddPart(MouseEvent event) {
        resetFieldsStyle();
        boolean end = false;
        TextField[] fieldCount = {count, price, min, max};
        if (inHouseRadio.isSelected() || outSourcedRadio.isSelected()) {
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
            if (name.getText().trim().isEmpty() || name.getText().trim().toLowerCase().equals("part name")) {
                AlertMessage.errorPart(4, name);
                return;
            }
            if (Integer.parseInt(min.getText().trim()) > Integer.parseInt(max.getText().trim())) {
                AlertMessage.errorPart(8, min);
                return;
            }
            if (Integer.parseInt(count.getText().trim()) < Integer.parseInt(min.getText().trim())) {
                AlertMessage.errorPart(6, count);
                return;
            }
            if (Integer.parseInt(count.getText().trim()) > Integer.parseInt(max.getText().trim())) {
                AlertMessage.errorPart(7, count);
                return;
            }
            if (end) {
                return;
            } else if (company.getText().trim().isEmpty() || company.getText().trim().toLowerCase().equals("company name")) {
                AlertMessage.errorPart(3, company);
                return;

            } else if (inHouseRadio.isSelected() && !company.getText().trim().matches("[0-9]*")) {
                AlertMessage.errorPart(9, company);
                return;
            } else if (inHouseRadio.isSelected()) {
                addInHouse();

            } else if (outSourcedRadio.isSelected()) {
                addOutSourced();

            }

        } else {
            AlertMessage.errorPart(2, null);
            return;

        }
        mainScreen(event);
    }

    private void addInHouse() {
        partDao.addPart(new InHouse(Integer.parseInt(id.getText().trim()), name.getText().trim(),
                Double.parseDouble(price.getText().trim()), Integer.parseInt(count.getText().trim()),
                Integer.parseInt(min.getText().trim()), Integer.parseInt(max.getText().trim()), (Integer.parseInt(company.getText().trim()))));

    }

    private void addOutSourced() {
        partDao.addPart(new OutSourced(Integer.parseInt(id.getText().trim()), name.getText().trim(),
                Double.parseDouble(price.getText().trim()), Integer.parseInt(count.getText().trim()),
                Integer.parseInt(min.getText().trim()), Integer.parseInt(max.getText().trim()), company.getText().trim()));

    }

    private void resetFieldsStyle() {
        name.setStyle("-fx-border-color: lightgray");
        count.setStyle("-fx-border-color: lightgray");
        price.setStyle("-fx-border-color: lightgray");
        min.setStyle("-fx-border-color: lightgray");
        max.setStyle("-fx-border-color: lightgray");
        company.setStyle("-fx-border-color: lightgray");
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
            e.printStackTrace();
        }
    }


    private boolean checkValue(TextField field) {
        boolean error = false;
        try {
            if (field.getText().trim().isEmpty() | field.getText().trim() == null) {
                AlertMessage.errorPart(1, field);
                return true;
            }
            if (field == price && Double.parseDouble(field.getText().trim()) < 0) {
                AlertMessage.errorPart(5, field);
                error = true;
            }
        } catch (Exception e) {
            error = true;
            AlertMessage.errorPart(3, field);
            System.out.println(e);

        }
        return error;
    }

    private boolean checkType(TextField field) {

        if (field == price & !field.getText().trim().matches("\\d+(\\.\\d+)?")) {
            AlertMessage.errorPart(3, field);
            return true;
        }
        if (field != price & !field.getText().trim().matches("[0-9]*")) {
            AlertMessage.errorPart(3, field);
            return true;
        }
        return false;
    }

}
