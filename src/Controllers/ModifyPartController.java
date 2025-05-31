/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import Model.InHouse;
import Model.OutSourced;
import Model.Part;
import SQLiteDatabase.PartDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class ModifyPartController implements Initializable {

    private Part part;
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
    private TextField price;
    @FXML
    private TextField count;
    @FXML
    private TextField min;
    @FXML
    private TextField max;
    @FXML
    private TextField company;
    @FXML
    private Label companyLabel;
    @FXML
    private Button modifyPartSaveButton;

    public ModifyPartController(Part part) {
        this.partDao = new PartDAO();
        this.part = part;
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setData();
    }

    @FXML
    private void clearTextField(MouseEvent event
    ) {
        Object source = event.getSource();
        TextField field = (TextField) source;
        field.setText("");
    }



    @FXML
    private void selectInHouse(MouseEvent event
    ) {
        companyLabel.setText("Machine ID");

    }

    @FXML
    private void selectOutSourced(MouseEvent event
    ) {
        companyLabel.setText("Company Name");

    }

    @FXML
    private void idDisabled(MouseEvent event
    ) {
    }

    private void setData() {

        if (part instanceof InHouse) {

            InHouse part1 = (InHouse) part;
            inHouseRadio.setSelected(true);
            companyLabel.setText("Machine ID");
            this.name.setText(part1.getName());
            this.id.setText((Integer.toString(part1.getId())));
            this.count.setText((Integer.toString(part1.getStock())));
            this.price.setText((Double.toString(part1.getPrice())));
            this.min.setText((Integer.toString(part1.getMin())));
            this.max.setText((Integer.toString(part1.getMax())));
            this.company.setText((Integer.toString(part1.getMachineID())));

        }

        if (part instanceof OutSourced) {

            OutSourced part2 = (OutSourced) part;
            outSourcedRadio.setSelected(true);
            companyLabel.setText("Company Name");
            this.name.setText(part2.getName());
            this.id.setText((Integer.toString(part2.getId())));
            this.count.setText((Integer.toString(part2.getStock())));
            this.price.setText((Double.toString(part2.getPrice())));
            this.min.setText((Integer.toString(part2.getMin())));
            this.max.setText((Integer.toString(part2.getMax())));
            this.company.setText(part2.getCompanyName());
        }
    }

    @FXML
    private void saveModifyPart(MouseEvent event
    ) {
        resetFieldsStyle();
        boolean end = false;
        TextField[] fieldCount = {count, price, min, max};
        if (inHouseRadio.isSelected() || outSourcedRadio.isSelected()) {
            for (int i = 0; i < fieldCount.length; i++) {
                boolean valueError = checkValue(fieldCount[i]);
                if (valueError) {
                    end = true;
                    break;
                }
                boolean typeError = checkType(fieldCount[i]);
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
            } else if (outSourcedRadio.isSelected() && company.getText().trim().isEmpty()) {
                AlertMessage.errorPart(1, company);
                return;
            } else if (inHouseRadio.isSelected() && !company.getText().matches("[0-9]*")) {
                AlertMessage.errorPart(9, company);
                return;

            } else if (inHouseRadio.isSelected() & part instanceof InHouse) {
                updateItemInHouse();

            } else if (inHouseRadio.isSelected() & part instanceof OutSourced) {
                updateItemInHouse();
            } else if (outSourcedRadio.isSelected() & part instanceof OutSourced) {
                updateItemOutSourced();
            } else if (outSourcedRadio.isSelected() & part instanceof InHouse) {
                updateItemOutSourced();
            }

        } else {
            AlertMessage.errorPart(2, null);
            return;

        }
        mainScreen(event);
    }

    @FXML
    private void cancelModifyPart(MouseEvent event
    ) {
        boolean cancel = cancel();
        if (cancel) {
            mainScreen(event);
        } else {
            return;
        }
    }

    private void mainScreen(MouseEvent event) {
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

    private void updateItemInHouse() {
        InHouse updatedPart = new InHouse(Integer.parseInt(id.getText().trim()), name.getText().trim(),
                Double.parseDouble(price.getText().trim()), Integer.parseInt(count.getText().trim()),
                Integer.parseInt(min.getText().trim()),
                Integer.parseInt(max.getText().trim()), Integer.parseInt(company.getText().trim()));
        partDao.updatePart(updatedPart); // Using DAO to update the part
    }
    private void updateItemOutSourced() {
        OutSourced updatedPart = new OutSourced(Integer.parseInt(id.getText().trim()), name.getText().trim(),
                Double.parseDouble(price.getText().trim()), Integer.parseInt(count.getText().trim()),
                Integer.parseInt(min.getText().trim()), Integer.parseInt(max.getText().trim()), company.getText().trim());
        partDao.updatePart(updatedPart); // Using DAO to update the part
    }


    private boolean checkValue(TextField field) {
        boolean error = false;
        try {
            if (field.getText().trim().isEmpty() || field.getText().trim() == null) {
                AlertMessage.errorPart(1, field);
                return true;
            }
            if (field == price && Double.parseDouble(field.getText().trim()) <= 0.0) {
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

    private void resetFieldsStyle() {
        name.setStyle("-fx-border-color: lightgray");
        count.setStyle("-fx-border-color: lightgray");
        price.setStyle("-fx-border-color: lightgray");
        min.setStyle("-fx-border-color: lightgray");
        max.setStyle("-fx-border-color: lightgray");
        company.setStyle("-fx-border-color: lightgray");

    }

    private boolean cancel() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Cancel");
        alert.setHeaderText("Are you sure you want to cancel?");
        alert.setContentText("Click ok to confirm");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            return true;
        } else {
            return false;
        }
    }

}
