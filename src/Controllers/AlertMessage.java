/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;

import java.util.Optional;

/**
 * @author Lloyd King
 */
public class AlertMessage {

    public static void infoWindow(int code, String name) {
        if (code != 2) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Confirmed");
            alert.setHeaderText(null);
            alert.setContentText(name + " has been deleted!");
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("There was an error!");
        }
    }

    public static boolean confirmationWindow(String name) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete part");
        alert.setHeaderText("Are you sure you want to delete: " + name);
        alert.setContentText("Click ok to confirm");
        Optional<ButtonType> result = alert.showAndWait();
        return result.get() == ButtonType.OK;
    }

    public static boolean cancel() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Cancel");
        alert.setHeaderText("Are you sure you want to cancel?");
        alert.setContentText("Click ok to confirm");
        Optional<ButtonType> result = alert.showAndWait();
        return result.get() == ButtonType.OK;
    }

    public static void errorPart(int code, TextField field) {
        fieldError(field);

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error adding part");
        alert.setHeaderText("Cannot add part");
        switch (code) {
            case 1: {
                alert.setContentText("Field is empty!");
                break;
            }
            case 2: {
                alert.setContentText("Ooops, you forgot to select In House/OutSourced!");
                break;
            }
            case 3: {
                alert.setContentText("Invalid format!");
                break;
            }
            case 4: {
                alert.setContentText("Name is invalid!");
                break;
            }
            case 5: {
                alert.setContentText("Value cannot be negative or zero!!");
                break;
            }
            case 6: {
                alert.setContentText("Inventory cannot be lower than min!");
                break;
            }
            case 7: {
                alert.setContentText("Inventory cannot be greater than max!");
                break;
            }
            case 8: {
                alert.setContentText("Min cannot be higher than max!");
                break;
            }
            case 9: {
                alert.setContentText("Machine ID must be a number");
                break;
            }
            default: {
                alert.setContentText("Unknown error!");
                break;
            }
        }
        alert.showAndWait();
    }

    public static void errorProduct(int code, TextField field) {
        fieldError(field);

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error adding product");
        alert.setHeaderText("Cannot add product");
        switch (code) {
            case 1: {
                alert.setContentText("Field is empty!");
                break;
            }
            case 2: {
                alert.setContentText("Part is already is associated with this product!");
                break;
            }
            case 3: {
                alert.setContentText("Invalid format!");
                break;
            }
            case 4: {
                alert.setContentText("Name is invalid!");
                break;
            }
            case 5: {
                alert.setContentText("Value cannot be negative!");
                break;
            }
            case 6: {
                alert.setContentText("Product cost cannot be lower than it's parts!");
                break;
            }
            case 7: {
                alert.setContentText("Product must have at least one part!");
                break;
            }
            case 8: {
                alert.setContentText("Inventory cannot be lower than min!");
                break;
            }
            case 9: {
                alert.setContentText("Inventory cannot be greater than max!");
                break;
            }
            case 10: {
                alert.setContentText("Min cannot be greater than max!");
                break;
            }
            default: {
                alert.setContentText("Unknown error!");
                break;
            }
        }
        alert.showAndWait();
    }

    private static void fieldError(TextField field) {
        if (field != null) {
            field.setStyle("-fx-border-color: red");
        }
    }
}
