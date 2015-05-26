package cz.muni.fi.pb138.kartoteka.gui;

import cz.muni.fi.pb138.kartoteka.entities.Category;
import cz.muni.fi.pb138.kartoteka.entities.Film;
import cz.muni.fi.pb138.kartoteka.exceptions.CategoryException;
import cz.muni.fi.pb138.kartoteka.exceptions.FilmException;
import cz.muni.fi.pb138.kartoteka.loaders.FileManager;
import cz.muni.fi.pb138.kartoteka.loaders.FileManagerImpl;
import cz.muni.fi.pb138.kartoteka.managers.KartotekaManager;
import cz.muni.fi.pb138.kartoteka.managers.KartotekaManagerImpl;
import javafx.application.Platform;
import javafx.collections.FXCollections;
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
import javafx.stage.StageStyle;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Main form controller
 *
 * @author Peter Stanko
 * @author Dominik Labuda
 * @author Peter Zaoral
 * @version 2015-05-18
 */
public class Controller implements Initializable {
    /**
     * Application file manager
     */
    private FileManager fm = new FileManagerImpl();

    /**
     * Kartoteka manager
     */
    private KartotekaManager kart;

    /**
     * Currently opened/operated absolute file path
     */
    private String openedFilePath;

    /**
     * Saves whether the changes have been saved
     */
    private boolean docSaved = true;

    /**
     * Main panel
     */
    @FXML
    private Parent root;

    /**
     * Panel with tabs
     */
    @FXML
    private TabPane tabPane;

    /**
     * Status label at the bottom
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
    }

    /**
     * Creates, saves and opens new {@link org.odftoolkit.simple.Document}
     * @param event action event
     */
    @FXML
    public void newDocumentAction(ActionEvent event) {
        KartotekaManager manager = new KartotekaManagerImpl();

        try {
            manager.addCategory(new Category("Filmy"));

            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save Document");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("ODS Files", "*.ods"));

            File selectedFile = fileChooser.showSaveDialog(root.getScene().getWindow());

            FileManager fm = new FileManagerImpl();
            openedFilePath = selectedFile.getAbsolutePath();
            fm.save(openedFilePath, manager);

            openFile();
        } catch (CategoryException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Adds new {@link Category}
     * @param event action event
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
        newStage.initStyle(StageStyle.UTILITY);
        newStage.setResizable(false);
        newStage.initOwner(this.root.getScene().getWindow());
        newStage.showAndWait();

        AddCategoryController controller = loader.getController();
        Category createdCategory = controller.getCategory();

        if (createdCategory != null) {
            try {
                kart.addCategory(createdCategory);
                addTab(createdCategory.getName());
                docSaved = false;
                statusLabel.setText("Status: Category " + createdCategory.getName() + " was added");
            } catch (CategoryException e) {
                e.printStackTrace();
                statusLabel.setText("Status: Exception was thrown while creating new category.");
            }
        } else {
            statusLabel.setText("Status: Category name dialog was closed.");
        }
    }

    /**
     * Updates existing {@link Category}
     * @param event action event
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
        newStage.initStyle(StageStyle.UTILITY);
        newStage.setResizable(false);
        newStage.initOwner(this.root.getScene().getWindow());

        Tab selectedTab = tabPane.getSelectionModel().getSelectedItem();

        AddCategoryController controller = loader.getController();
        controller.updateSetUp(kart.getCategory(selectedTab.getText()));
        newStage.showAndWait();


        if (controller.getCategory() != null) {
            selectedTab.setText(controller.getCategory().getName());
            docSaved = false;
            statusLabel.setText("Status: Category " + selectedTab.getText() + " was updated");
        } else {
            statusLabel.setText("Status: Category name dialog was closed.");
        }
    }

    /**
     * Deletes currently opened {@link Category}
     * @param event action event
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
     * Adds new {@link Film}
     * @param event action event
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
        newStage.initStyle(StageStyle.UTILITY);
        newStage.setResizable(false);
        newStage.initOwner(this.root.getScene().getWindow());
        newStage.showAndWait();

        AddFilmController controller = loader.getController();
        Film createdFilm = controller.getFilm();

        if (createdFilm != null) {
            try {
                Tab selectedTab = tabPane.getSelectionModel().getSelectedItem();
                kart.getCategory(selectedTab.getText()).addFilm(createdFilm);
                ((TableView) selectedTab.getContent()).getItems().add(createdFilm);

                docSaved = false;
                statusLabel.setText("Status: Movie \"" + createdFilm.getName() + "\" was added");
            } catch (FilmException e) {
                e.printStackTrace();
                statusLabel.setText("Status: Exception was thrown while creating new movie.");
            }
        } else {
            statusLabel.setText("Status: Movie creation was cancelled.");
        }
    }

    /**
     * Updates {@link Film}
     * @param event action event
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
        newStage.initStyle(StageStyle.UTILITY);
        newStage.setResizable(false);
        newStage.initOwner(this.root.getScene().getWindow());

        Tab selectedTab = tabPane.getSelectionModel().getSelectedItem();
        Film selectedFilm = ((TableView<Film>)selectedTab.getContent()).getSelectionModel().getSelectedItem();

        AddFilmController controller = loader.getController();
        controller.updateSetUp(selectedFilm);

        newStage.showAndWait();

        if (controller.getFilm() != null) {
            ((TableView) selectedTab.getContent()).getItems().clear();
            ((TableView) selectedTab.getContent()).setItems(FXCollections.observableArrayList(kart.getCategory(selectedTab.getText()).getFilms()));

            docSaved = false;
            statusLabel.setText("Status: Movie \"" + controller.getFilm().getName() + "\" was updated");
        } else {
            statusLabel.setText("Status: Movie update was cancelled");
        }
    }

    /**
     * Deletes currently selected {@link Film}
     * @param event action event
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
     * @param event action event
     */
    @FXML
    public void openFileAction(ActionEvent event) {
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
            openFile();
        }
    }

    /**
     * Saves the file to chosen path
     * @param event action event
     */
    @FXML
    public void saveAsAction(ActionEvent event) {
        //File Chooser dialog setup
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
     * @param event action event
     */
    @FXML
    public void saveChangesAction(ActionEvent event) {
        saveFile(openedFilePath);
    }

    /**
     * Closes application
     * @param event action event
     */
    @FXML
    public void closeAppAction(ActionEvent event) {
        if (!docSaved){
            statusLabel.setText("Status: Changes were not saved.");
            return;
        }
        Platform.exit();
    }

    // Private (helper) methods

    /**
     * Opens a file
     */
    private void openFile() {
        try {
            kart = fm.load(openedFilePath);
            statusLabel.setText("Status: File \"" + openedFilePath + "\" was successfully opened.");
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
     * Adds tabs to the {@link Controller#tabPane}
     * @param name tab name
     */
    private void addTab(String name) {
        Tab tab = new Tab(name);
        TableView<Film> tableView = new TableView<>();
        tableView.setColumnResizePolicy(param -> true);

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