package cz.muni.fi.pb138.kartoteka.gui;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

/**
 * Main form controller
 *
 * @author Peter Stanko
 * @author Dominik Labuda
 * @author Peter Zaoral
 * @version 2015-05-26
 */

public class AlertBox {
    /**
     * Displays error dialog
     * @param title dialog's title
     * @param message error message
     */

    public static void displayError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error Dialog");
        alert.setHeaderText(title);
        alert.setContentText(message);

        alert.showAndWait();
    }

    /**
     * Displays confirmation dialog
     * @param title dialog's title
     * @param message confirmation message
     * @return button selection as result
     */
    public static Optional<ButtonType> displayConfirmation(String title, String message)
    {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText(title);
        alert.setContentText(message);
        return alert.showAndWait();
    }





}