package cz.muni.fi.pb138.kartoteka.gui;

import cz.muni.fi.pb138.kartoteka.entities.Category;
import cz.muni.fi.pb138.kartoteka.entities.Film;
import cz.muni.fi.pb138.kartoteka.exceptions.CategoryException;
import cz.muni.fi.pb138.kartoteka.exceptions.FilmException;
import cz.muni.fi.pb138.kartoteka.loaders.FileManager;
import cz.muni.fi.pb138.kartoteka.loaders.FileManagerImpl;
import cz.muni.fi.pb138.kartoteka.managers.KartotekaManager;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Peter Zaoral on 8.5.2015.
 */
public class Controller implements Initializable {
    private FileManager fm = new FileManagerImpl();
    private ObservableList<String> list = FXCollections.observableArrayList();
    private KartotekaManager kart;
    private String openedFilePath;
    private boolean docSaved = true;

    @FXML
    private Parent root;
    @FXML
    private TabPane tabPane;
    @FXML
    private Label statusLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    /**
     * Adds new category
     * @param event
     * @throws IOException when FXML is not available
     */
    @FXML
     public void addCategoryAction(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/addCategoryDialog.fxml"));
        Parent root = loader.load();
        Stage newStage = new Stage();
        newStage.setTitle("Add Category");
        newStage.setScene(new Scene(root, 250, 125));
        newStage.initModality(Modality.WINDOW_MODAL);
        newStage.initOwner(this.root.getScene().getWindow());
        newStage.showAndWait();

        AddCategoryController controller = loader.getController();
        String categoryName = controller.getCategoryName();

        if (categoryName != null) {
            try {
                Category category = new Category(categoryName);
                kart.addCategory(category);
                addTab(categoryName);
                docSaved = false;
                statusLabel.setText("Status: Category " + category.getName() + " was added");
            } catch (CategoryException e) {
                e.printStackTrace();
                statusLabel.setText("Status: Exception was thrown while creating new category.");
            }
        } else {
            statusLabel.setText("Status: Category name dialog was closed.");
        }
    }

    /**
     * Updates existing category
     * @param event
     * @throws IOException when FXML is not available
     */
    @FXML
    public void updateCategoryAction(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/addCategoryDialog.fxml"));
        Parent root = loader.load();
        Stage newStage = new Stage();
        newStage.setTitle("Update Category");
        newStage.setScene(new Scene(root, 250, 125));
        newStage.initModality(Modality.WINDOW_MODAL);
        newStage.initOwner(this.root.getScene().getWindow());

        Tab selectedTab = tabPane.getSelectionModel().getSelectedItem();

        AddCategoryController controller = loader.getController();
        controller.setCategoryNameText(selectedTab.getText());
        newStage.showAndWait();

        String categoryName = controller.getCategoryName();

        if (categoryName != null) {

                Category category = kart.getCategory(selectedTab.getText());
                category.setName(categoryName);
                selectedTab.setText(categoryName);
                docSaved = false;
                statusLabel.setText("Status: Category " + category.getName() + " was updated");
        } else {
            statusLabel.setText("Status: Category name dialog was closed.");
        }
    }

    /**
     * Deletes currently opened category
     * @param event
     */
    @FXML
    public void deleteCategoryAction(ActionEvent event) {
        try {
            Tab selectedTab = tabPane.getSelectionModel().getSelectedItem();
            kart.deleteCategory(kart.getCategory(selectedTab.getText()));
            tabPane.getTabs().remove(selectedTab);
            docSaved = false;
        } catch (CategoryException e) {
            e.printStackTrace();
            statusLabel.setText("Status: Exception was thrown while deleting the category.");
        }
    }

    /**
     * Adds new Film
     * @param event
     * @throws IOException when FXML is not available
     */
    @FXML
    public void addFilmAction(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/addFilmDialog.fxml"));
        Parent root = loader.load();
        Stage newStage = new Stage();
        newStage.setTitle("Add Movie");
        newStage.setScene(new Scene(root, 640, 225));
        newStage.initModality(Modality.WINDOW_MODAL);
        newStage.initOwner(this.root.getScene().getWindow());
        newStage.showAndWait();

        AddFilmController controller = loader.getController();
        String name = controller.getName();
        String year = controller.getYear();
        String director = controller.getDirector();
        String rating = controller.getRating();
        String description = controller.getDescription();

        if (name != null || year != null || director != null || rating != null || description != null) {
            try {
                Film newFilm = new Film(name, director, year, description, rating);
                Tab selectedTab = tabPane.getSelectionModel().getSelectedItem();
                kart.getCategory(selectedTab.getText()).addFilm(newFilm);
                ((TableView) selectedTab.getContent()).getItems().add(newFilm);

                docSaved = false;
                statusLabel.setText("Status: Movie \"" + name + "\" was added");
            } catch (FilmException e) {
                e.printStackTrace();
                statusLabel.setText("Status: Exception was thrown while creating new movie.");
            }
        }
    }

    /**
     * Updates Film
     * @param event
     * @throws IOException when FXML is not available
     */
    @FXML
    public void updateFilmAction(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/addFilmDialog.fxml"));
        Parent root = loader.load();
        Stage newStage = new Stage();
        newStage.setTitle("Update Movie");
        newStage.setScene(new Scene(root, 640, 225));
        newStage.initModality(Modality.WINDOW_MODAL);
        newStage.initOwner(this.root.getScene().getWindow());

        Tab selectedTab = tabPane.getSelectionModel().getSelectedItem();
        Film selectedFilm = ((TableView<Film>)selectedTab.getContent()).getSelectionModel().getSelectedItem();
        AddFilmController controller = loader.getController();

        controller.setNameText(selectedFilm.getName());
        controller.setYearText(selectedFilm.getYear());
        controller.setRatingText(selectedFilm.getRating());
        controller.setDirectorText(selectedFilm.getDirector());
        controller.setDescriptionText(selectedFilm.getDescription());

        newStage.showAndWait();

        String name = controller.getName();
        String year = controller.getYear();
        String director = controller.getDirector();
        String rating = controller.getRating();
        String description = controller.getDescription();

        if (name != null || year != null || director != null || rating != null || description != null) {
                selectedFilm.setName(name);
                selectedFilm.setYear(year);
                selectedFilm.setRating(rating);
                selectedFilm.setDirector(director);
                selectedFilm.setDescription(description);

            ((TableView) selectedTab.getContent()).getItems().clear();
                ((TableView) selectedTab.getContent()).setItems(FXCollections.observableArrayList(kart.getCategory(selectedTab.getText()).getFilms()));

                docSaved = false;
                statusLabel.setText("Status: Movie \"" + name + "\" was updated");

        }
    }

    /**
     * Deletes currently opened film
     * @param event
     */
    @FXML
    public void deleteFilmAction(ActionEvent event) {
        try {
            Tab selectedTab = tabPane.getSelectionModel().getSelectedItem();
            Film selectedFilm = ((TableView<Film>) selectedTab.getContent()).getSelectionModel().getSelectedItem();
            kart.getCategory(selectedTab.getText()).deleteFilm((int) selectedFilm.getId());
            ((TableView) selectedTab.getContent()).getItems().remove(selectedFilm);
            docSaved = false;
        } catch (FilmException e) {
            e.printStackTrace();
            statusLabel.setText("Status: Exception was thrown while deleting the movie.");
        }
    }

    /**
     * Opens a file using FileChooser
     * @param event
     */
    @FXML
    public void openFile(ActionEvent event) {
        tabPane.getTabs().clear();
        // FileChooser dialog setup
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Document");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("ODS Files", "*.ods"),
                new FileChooser.ExtensionFilter("All Files", "*.*"));

        File selectedFile = fileChooser.showOpenDialog(root.getScene().getWindow());
        if (selectedFile != null) {
            openedFilePath = selectedFile.getAbsolutePath();
            try {
                kart = fm.load(openedFilePath);
                statusLabel.setText("Status: File \"" + selectedFile.getName() + "\" was successfully opened.");
            } catch (Exception e) {
                e.printStackTrace();
                statusLabel.setText("Status: An exception was thrown while opening a file");
            }
            if (kart != null)
            {
                for(Category category : kart.getCategories()) {
                    addTab(category.getName());
                }
            }
        }
    }

    /**
     * Saves the file to chosen path
     * @param event
     */
    @FXML
    public void saveAsAction(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Document");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("ODS Files", "*.ods"));
        File selectedFile = fileChooser.showSaveDialog(root.getScene().getWindow());
        if (selectedFile != null) {
            saveFile(selectedFile.getAbsolutePath());
        }
    }

    /**
     * Saves the file to its current path
     * @param event
     */
    @FXML
    public void saveChangesAction(ActionEvent event) {
        saveFile(openedFilePath);
    }

    /**
     * Closes application
     * @param event
     */
    @FXML
    public void closeApp(ActionEvent event) {
        if (!docSaved){
            statusLabel.setText("Status: Changes were not saved.");
            return;
        }
        Platform.exit();
    }

    // Private (helper) methods

    /**
     * Saves the file
     * @param path file path
     */
    private void saveFile(String path) {
        try {
            fm.save(path, kart);
            docSaved = true;
            statusLabel.setText("Status: Changes saved");
        } catch (Exception e) {
            e.printStackTrace();
            statusLabel.setText("Status: Exception was thrown during saving. Try again.");
        }
    }

    /**
     * Adds tabs to the TabPane
     * @param name tab name
     */
    private void addTab(String name) {
        Tab tab = new Tab(name);
        TableView<Film> tableView = new TableView<>();

        TableColumn<Film, String> nameCol = new TableColumn<>("Film name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Film, String> yearCol = new TableColumn<>("Year");
        yearCol.setCellValueFactory(new PropertyValueFactory<>("year"));

        TableColumn<Film, String> ratingCol = new TableColumn<>("Rating");
        ratingCol.setCellValueFactory(new PropertyValueFactory<>("rating"));

        TableColumn<Film, String> directorCol = new TableColumn<>("Director");
        directorCol.setCellValueFactory(new PropertyValueFactory<>("director"));

        TableColumn<Film, String> descriptonCol = new TableColumn<>("Description");
        descriptonCol.setCellValueFactory(new PropertyValueFactory<>("description"));

        tableView.getColumns().add(nameCol);
        tableView.getColumns().add(yearCol);
        tableView.getColumns().add(ratingCol);
        tableView.getColumns().add(directorCol);
        tableView.getColumns().add(descriptonCol);

        tableView.setItems(FXCollections.observableArrayList(kart.getCategory(name).getFilms()));

        tab.setContent(tableView);
        tabPane.getTabs().add(tab);
    }
}