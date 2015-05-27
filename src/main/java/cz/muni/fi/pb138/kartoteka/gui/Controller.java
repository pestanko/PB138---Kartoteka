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
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import javax.swing.*;
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
 * @version 2015-05-26
 */
public class Controller implements Initializable {
    /**
     * Application file manager
     */
    private FileManager fm = new FileManagerImpl();

    /**
     * Class logger
     */
    final static org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(Controller.class);

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
     * Close menu item
     */
    @FXML
    private MenuItem closeCmd;

    /**
     * Save menu item
     */
    @FXML
    private MenuItem saveCmd;

    /**
     * Save as menu item
     */
    @FXML
    private MenuItem saveAsCmd;

    /**
     * New document menu item
     */
    @FXML
    private MenuItem newDocumentCmd;

    /**
     * Open menu item
     */
    @FXML
    private MenuItem openCmd;

    /**
     * Add category menu item
     */
    @FXML
    private MenuItem addCategoryCmd;

    /**
     * Update category menu item
     */
    @FXML
    private MenuItem updateCategoryCmd;

    /**
     * Delete category menu item
     */
    @FXML
    private MenuItem deleteCategoryCmd;

    /**
     * Add movie menu item
     */
    @FXML
    private MenuItem addMovieCmd;

    /**
     * Update movie menu item
     */
    @FXML
    private MenuItem updateMovieCmd;

    /**
     * Delete movie menu item
     */
    @FXML
    private MenuItem deleteMovieCmd;

    /**
     * Initializes the UI
     * Sets the key shortcuts to the menu items
     * @param location location
     * @param resources resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Menu shortcuts initialization
        closeCmd.setAccelerator(new KeyCodeCombination(KeyCode.Q, KeyCombination.CONTROL_DOWN));
        saveCmd.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN));
        saveAsCmd.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN, KeyCombination.SHIFT_DOWN));
        newDocumentCmd.setAccelerator(new KeyCodeCombination(KeyCode.N, KeyCombination.CONTROL_DOWN));
        openCmd.setAccelerator(new KeyCodeCombination(KeyCode.O, KeyCombination.CONTROL_DOWN));
        addCategoryCmd.setAccelerator(new KeyCodeCombination(KeyCode.F1, KeyCombination.CONTROL_DOWN));
        updateCategoryCmd.setAccelerator(new KeyCodeCombination(KeyCode.F2, KeyCombination.CONTROL_DOWN));
        deleteCategoryCmd.setAccelerator(new KeyCodeCombination(KeyCode.F3, KeyCombination.CONTROL_DOWN));
        addMovieCmd.setAccelerator(new KeyCodeCombination(KeyCode.F4, KeyCombination.CONTROL_DOWN));
        updateMovieCmd.setAccelerator(new KeyCodeCombination(KeyCode.F5, KeyCombination.CONTROL_DOWN));
        deleteMovieCmd.setAccelerator(new KeyCodeCombination(KeyCode.F6, KeyCombination.CONTROL_DOWN));
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
            logger.error("New document exception",e);
        } catch (Exception e) {
            logger.error("New document exception",e);
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
                logger.error("Add category: ", e);
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
            logger.error("Delete Category", e);
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
                logger.error("Add Film:", e);
                JOptionPane.showMessageDialog(null, "Category is not empty => cannot remove category.");
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
            logger.error("Delete film action", e);
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
        if (openedFilePath == null) {
            JOptionPane.showMessageDialog(null, "There's nothing to save");
        } else {
            saveFile(openedFilePath);
        }
    }

    /**
     * Closes application
     * @param event action event
     */
    @FXML
    public void closeAppAction(ActionEvent event) {
        if (!docSaved){
            int reply = JOptionPane.showConfirmDialog(null,
                    "You haven't saved changes in your file.\nAre you sure you want to exit?",
                    "Exit Application",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);
            if (reply == JOptionPane.YES_OPTION) {
                statusLabel.setText("Status: Changes were not saved.");
            } else {
                return;
            }
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
        } catch (UnsupportedOperationException ex)
        {
            JOptionPane.showMessageDialog(null,"Unsupported format.");
        }catch (Exception e) {
            logger.error("Open File", e);
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
            logger.error("Save File: ", e);
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