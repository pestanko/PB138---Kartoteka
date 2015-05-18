package cz.muni.fi.pb138.kartoteka.gui;

import cz.muni.fi.pb138.kartoteka.entities.Category;
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

    private Category category = new Category("");

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
                category = null;
                closeDialog();
            }
        });

        // Closes dialog and returns name
        okButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                category.setName(categoryNameTextField.getText());
                closeDialog();
            }
        });
    }

    /**
     * Getter for category
     * @return category
     */
    public Category getCategory() {
        return category;
    }

    /**
     * Sets the category and fill the input
     * @param category category to be updated
     */
    public void updateSetUp(Category category) {
        this.category = category;
        categoryNameTextField.setText(category.getName());
    }

    /**
     * Closes dialog
     */
    private void closeDialog() {
        ((Stage) root.getScene().getWindow()).close();
    }
}
