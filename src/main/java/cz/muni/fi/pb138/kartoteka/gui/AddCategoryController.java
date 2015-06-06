package cz.muni.fi.pb138.kartoteka.gui;

import cz.muni.fi.pb138.kartoteka.entities.Category;
import cz.muni.fi.pb138.kartoteka.managers.KartotekaManager;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Add Category dialog controller
 *
 * @author Peter Stanko
 * @author Dominik Labuda
 * @author Peter Zaoral
 * @version 2015-06-06
 */
public class AddCategoryController implements Initializable {

    /**
     * Current category
     */
    private Category category;

    /**
     * Kartoteka
     */
    private KartotekaManager kartoteka;

    /**
     * Main panel
     */
    @FXML
    private Parent root;

    /**
     * OK button
     */
    @FXML
    private Button okButton;

    /**
     * Cancel button
     */
    @FXML
    private Button cancelButton;

    /**
     * Text field for name
     */
    @FXML
    private TextField categoryNameTextField;

    /**
     * Status label
     */
    @FXML
    private Label statusLabel;

    /**
     * Initializes the UI
     * @param location location
     * @param resources resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cancelButton.setCancelButton(true);
        okButton.setDefaultButton(true);

        cancelButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                closeDialog();
            }
        });

        okButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String categoryName = categoryNameTextField.getText();

                // Validate
                if (categoryName.isEmpty()) {
                    statusLabel.setText(resources.getString("dialog.category.empty_name"));
                    return;
                } else if (kartoteka.containsCategory(categoryName)) {
                    statusLabel.setText(String.format(resources.getString("dialog.category.already_exists"), categoryName));
                    return;
                }

                category = new Category();
                category.setName(categoryName);
                closeDialog();
            }
        });
    }

    /**
     * Getter for {@link AddCategoryController#category}
     * @return category
     */
    public Category getCategory() {
        return category;
    }

    /**
     * Sets the {@link AddCategoryController#category} and fill the input
     * @param category category to be updated
     */
    public void updateSetUp(Category category, KartotekaManager kartoteka) {
        this.category = category;
        this.kartoteka = kartoteka;
        categoryNameTextField.setText(category.getName());
    }

    /**
     * Setter for {@link AddCategoryController#kartoteka}
     * @param kartoteka kartoteka manager
     */
    public void setKartoteka(KartotekaManager kartoteka) {
        this.kartoteka = kartoteka;
    }

    /**
     * Closes dialog
     */
    private void closeDialog() {
        ((Stage) root.getScene().getWindow()).close();
    }
}
