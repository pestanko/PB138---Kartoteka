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
 * Add Film dialog controller
 *
 * @author Dominik Labuda
 */
public class AddFilmController implements Initializable {

    private String name;
    private String year;
    private String rating;
    private String director;
    private String description;

    @FXML
    private Parent root;
    @FXML
    private Button okButton;
    @FXML
    private Button cancelButton;
    @FXML
    private TextField nameTextField;
    @FXML
    private TextField yearTextField;
    @FXML
    private TextField ratingTextField;
    @FXML
    private TextField directorTextField;
    @FXML
    private TextField descriptionTextField;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cancelButton.setCancelButton(true);
        okButton.setDefaultButton(true);

        cancelButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                name = null;
                year = null;
                rating = null;
                director = null;
                description = null;
                closeDialog();
            }
        });

        okButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                name = nameTextField.getText();
                year = yearTextField.getText();
                rating = ratingTextField.getText();
                director = directorTextField.getText();
                description = descriptionTextField.getText();
                closeDialog();
            }
        });
    }

    /**
     * Getter for name
     * @return movie name
     */
    public String getName() {
        return name;
    }

    /**
     * Getter for year
     * @return movie year
     */
    public String getYear() {
        return year;
    }

    /**
     * Getter for rating
     * @return movie rating
     */
    public String getRating() {
        return rating;
    }

    /**
     * Getter for director
     * @return movie director
     */
    public String getDirector() {
        return director;
    }

    /**
     * Getter for description
     * @return movie description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Setter for name input field
     * @param name movie name
     */
    public void setNameText(String name) {
        nameTextField.setText(name);
    }

    /**
     * Setter for year input field
     * @param year movie year
     */
    public void setYearText(String year) {
        yearTextField.setText(year);
    }

    /**
     * Setter for rating input field
     * @param rating movie rating
     */
    public void setRatingText(String rating) {
        ratingTextField.setText(rating);
    }

    /**
     * Setter for director input field
     * @param director movie director
     */
    public void setDirectorText(String director) {
        directorTextField.setText(director);
    }

    /**
     * Setter for description input field
     * @param description movie description
     */
    public void setDescriptionText(String description) {
        descriptionTextField.setText(description);
    }

    /**
     * Closes dialog
     */
    private void closeDialog() {
        ((Stage) root.getScene().getWindow()).close();
    }
}
