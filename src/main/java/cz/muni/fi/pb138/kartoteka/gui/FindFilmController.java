package cz.muni.fi.pb138.kartoteka.gui;

import cz.muni.fi.pb138.kartoteka.entities.Category;
import cz.muni.fi.pb138.kartoteka.entities.Film;
import cz.muni.fi.pb138.kartoteka.helpers.Tuple;
import cz.muni.fi.pb138.kartoteka.managers.KartotekaManager;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.controlsfx.control.TextFields;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Find film dialog controller
 *
 * @author Peter Stanko
 * @author Dominik Labuda
 * @author Peter Zaoral
 * @version 2015-06-07
 */
public class FindFilmController implements Initializable {

    /**
     * Result from dialog
     */
    private Tuple<Long, Long> result;

    /**
     * List of ids of found films
     */
    private List<Tuple<Long, Long>> ids;

    /**
     * Movie database
     */
    private KartotekaManager kartoteka;

    /**
     * Root
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
     * Table view
     */
    @FXML
    private TableView tableView;

    /**
     * Status label
     */
    @FXML
    private Label statusLabel;

    /**
     * Filter text field item from ControlsFX library
     */
    @FXML
    private TextField filterTextField = TextFields.createSearchField();

    /**
     * NewUI HBox
     */
    @FXML
    private HBox filterHBox;

    /**
     * Initializes the UI
     * @param location location
     * @param resources resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Filter HBox initialization
        filterTextField.setPromptText(resources.getString("prompt.start_typing"));
        filterHBox.setHgrow(filterTextField, Priority.ALWAYS);
        filterHBox.getChildren().add(filterTextField);

        tableView.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
        tableView.setPlaceholder(new Text(resources.getString("empty_tableview")));

        TableColumn<Film, String> nameCol = new TableColumn<>(resources.getString("movie_name"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Film, String> yearCol = new TableColumn<>(resources.getString("year"));
        yearCol.setCellValueFactory(new PropertyValueFactory<>("year"));

        TableColumn<Film, String> ratingCol = new TableColumn<>(resources.getString("rating"));
        ratingCol.setCellValueFactory(new PropertyValueFactory<>("rating"));

        TableColumn<Film, String> directorCol = new TableColumn<>(resources.getString("director"));
        directorCol.setCellValueFactory(new PropertyValueFactory<>("director"));

        TableColumn<Film, String> descriptonCol = new TableColumn<>(resources.getString("description"));
        descriptonCol.setCellValueFactory(new PropertyValueFactory<>("description"));

        tableView.getColumns().addAll(nameCol, yearCol, ratingCol, directorCol, descriptonCol);

        filterTextField.textProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                String filter = filterTextField.textProperty().get();

                if (filter.isEmpty()) {
                    tableView.setItems(FXCollections.observableArrayList());
                    return;
                }

                ObservableList<Film> tableItems = FXCollections.observableArrayList();
                ids = new ArrayList<>();

                for (Category category : kartoteka.getCategories()) {
                    for (Film film : category.getFilms()) {
                        if (film.getName().toLowerCase().contains(filter.toLowerCase())) {
                            tableItems.add(film);
                            ids.add(new Tuple<>(category.getId(), film.getId()));
                        }
                    }
                }

                tableView.setItems(tableItems);
            }
        });

        cancelButton.setCancelButton(true);
        cancelButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                closeDialog();
            }
        });

        okButton.setDefaultButton(true);
        okButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                if (tableView.getSelectionModel().getSelectedItem() == null) {
                    statusLabel.setText(resources.getString("error.message.no_movie_selected"));
                    return;
                }

                result = ids.get(tableView.getSelectionModel().getSelectedIndex());
                closeDialog();
            }
        });
    }

    /**
     * Setter for {@link FindFilmController#kartoteka}
     * @param kartoteka movie database
     */
    public void setKartoteka(KartotekaManager kartoteka) {
        this.kartoteka = kartoteka;
    }

    /**
     * Getter for {@link FindFilmController#result}
     * @return result
     */
    public Tuple<Long, Long> getResult() {
        return result;
    }

    /**
     * Closes dialog
     */
    private void closeDialog() {
        ((Stage) root.getScene().getWindow()).close();
    }
}
