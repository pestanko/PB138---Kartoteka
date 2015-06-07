package cz.muni.fi.pb138.kartoteka.gui;

import cz.muni.fi.pb138.kartoteka.entities.Category;
import cz.muni.fi.pb138.kartoteka.entities.Film;
import cz.muni.fi.pb138.kartoteka.exceptions.FilmException;
import cz.muni.fi.pb138.kartoteka.managers.KartotekaManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Change category dialog controller
 *
 * @author Peter Stanko
 * @author Dominik Labuda
 * @author Peter Zaoral
 * @version 2015-05-18
 */
public class ChangeCategoryController implements Initializable {

    /**
     * Kartoteka
     */
    private KartotekaManager kartoteka;

    /**
     * Current category of the film
     */
    private Category currentCategory;

    /**
     * Current film
     */
    private Film currentFilm;

    /**
     *  True if dialog OK has been pressed
     */
    private boolean okPressed = false;

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
     * Label showing current category name
     */
    @FXML
    private Label currentCategoryLabel;

    /**
     * ComboBox with categories
     */
    @FXML
    private ComboBox categoryComboBox;

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

                if (categoryComboBox.getSelectionModel().getSelectedItem() == null) {
                    statusLabel.setText(resources.getString("error.message.no_category_chosen"));
                    return;
                }

                try {
                    currentCategory.deleteFilm(currentFilm.getId());
                    Category newCategory = kartoteka.getCategory((String)categoryComboBox.getSelectionModel().getSelectedItem());
                    newCategory.addFilm(currentFilm);
                    okPressed = true;
                } catch (FilmException e) {
                    e.printStackTrace();
                }
                closeDialog();
            }
        });
    }

    /**
     * Initializes the dialog data
     * @param kartoteka kartoteka
     * @param currentFilm current film
     * @param currentCategory current category
     */
    public void initializeData(KartotekaManager kartoteka, Film currentFilm, Category currentCategory) {
        this.kartoteka = kartoteka;
        this.currentFilm = currentFilm;
        this.currentCategory = currentCategory;
        ObservableList<String> categoryNames = FXCollections.observableArrayList();
        kartoteka.getCategories().forEach(category -> categoryNames.add(category.getName()));
        categoryNames.remove(currentCategory.getName());

        currentCategoryLabel.setText(currentCategory.getName());
        categoryComboBox.setItems(categoryNames);
    }

    public boolean isOkPressed() {
        return okPressed;
    }

    /**
     * Closes dialog
     */
    private void closeDialog() {
        ((Stage) root.getScene().getWindow()).close();
    }
}
