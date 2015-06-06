package cz.muni.fi.pb138.kartoteka.gui;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;

import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Main form controller
 *
 * @author Peter Stanko
 * @author Dominik Labuda
 * @author Peter Zaoral
 * @version 2015-06-06
 */

public class AlertBox {

    /**
     * Resource bundle of text serving to internationalization
     */
    private static ResourceBundle texts = ResourceBundle.getBundle("texts");

    /**
     * Displays error dialog
     * @param title dialog's title
     * @param message error message
     */
    public static void displayError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(texts.getString("error"));
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
        alert.setTitle(texts.getString("confirmation"));
        alert.setHeaderText(title);
        alert.setContentText(message);
        ((Button) alert.getDialogPane().lookupButton(ButtonType.CANCEL)).setText(texts.getString("cancel"));
        return alert.showAndWait();
    }
}