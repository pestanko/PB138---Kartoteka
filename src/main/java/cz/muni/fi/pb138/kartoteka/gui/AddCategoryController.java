package cz.muni.fi.pb138.kartoteka.gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Add Category dialog controller
 *
 * @author Dominik Labuda
 */
public class AddCategoryController implements Initializable {

    private String categoryName;

    @FXML
    private Parent root;
    @FXML
    private Button okButton;
    @FXML
    private Button cancelButton;
    @FXML
    private TextField categoryNameTextField;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cancelButton.setCancelButton(true);
        okButton.setDefaultButton(true);

        // Closes dialog and does not return anything
        cancelButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                categoryName = null;
                closeDialog();
            }
        });

        // Closes dialog and returns name
        okButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                categoryName = categoryNameTextField.getText();
                closeDialog();
            }
        });
    }

    /**
     * Getter for category name
     * @return category name
     */
    public String getCategoryName() {
        return categoryName;
    }

    /**
     * Setter for category name input field
     * @param name category name
     */
    public void setCategoryNameText(String name) {
        categoryNameTextField.setText(name);
    }

    /**
     * Closes dialog
     */
    private void closeDialog() {
        ((Stage) root.getScene().getWindow()).close();
    }
}
