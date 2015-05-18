package cz.muni.fi.pb138.kartoteka.gui;

import cz.muni.fi.pb138.kartoteka.entities.Film;
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

    private Film film = new Film();

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
                film = null;
                closeDialog();
            }
        });

        okButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                film.setName(nameTextField.getText());
                film.setYear(yearTextField.getText());
                film.setRating(ratingTextField.getText());
                film.setDirector(directorTextField.getText());
                film.setDescription(descriptionTextField.getText());
                closeDialog();
            }
        });
    }

    /**
     * Getter for film
     * @return film
     */
    public Film getFilm() {
        return film;
    }

    /**
     * Sets the film and fills inputs
     * @param film film to be updated
     */
    public void updateSetUp(Film film) {
        this.film = film;
        nameTextField.setText(film.getName());
        yearTextField.setText(film.getYear());
        ratingTextField.setText(film.getRating());
        directorTextField.setText(film.getDirector());
        descriptionTextField.setText(film.getDescription());
    }

    /**
     * Closes dialog
     */
    private void closeDialog() {
        ((Stage) root.getScene().getWindow()).close();
    }
}
