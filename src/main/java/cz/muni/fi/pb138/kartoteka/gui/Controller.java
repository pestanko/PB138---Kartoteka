package cz.muni.fi.pb138.kartoteka.gui;

import cz.muni.fi.pb138.kartoteka.entities.Category;
import cz.muni.fi.pb138.kartoteka.entities.Film;
import cz.muni.fi.pb138.kartoteka.exceptions.CategoryException;
import cz.muni.fi.pb138.kartoteka.exceptions.FilmException;
import cz.muni.fi.pb138.kartoteka.helpers.Tuple;
import cz.muni.fi.pb138.kartoteka.loaders.FileManager;
import cz.muni.fi.pb138.kartoteka.loaders.FileManagerImpl;
import cz.muni.fi.pb138.kartoteka.managers.KartotekaManager;
import cz.muni.fi.pb138.kartoteka.managers.KartotekaManagerImpl;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
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
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import org.controlsfx.control.TextFields;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Main form controller
 *
 * @author Peter Stanko
 * @author Dominik Labuda
 * @author Peter Zaoral
 * @version 2015-06-07
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
     * Resource bundle of text serving to internationalization
     */
    private ResourceBundle texts = ResourceBundle.getBundle("texts");

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
     * Find movie menu item
     */
    @FXML
    private MenuItem findMovieCmd;

    /**
     * Refresh menu item
     */
    @FXML
    private MenuItem refreshCmd;

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
     * Sets the key shortcuts to the menu items
     * @param location location
     * @param resources resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Filter HBox initialization
        filterTextField.setPromptText(texts.getString("prompt.start_typing"));
        filterHBox.setHgrow(filterTextField, Priority.ALWAYS);
        filterHBox.getChildren().addAll(filterTextField);
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
        findMovieCmd.setAccelerator(new KeyCodeCombination(KeyCode.F, KeyCombination.CONTROL_DOWN));
        refreshCmd.setAccelerator(new KeyCodeCombination(KeyCode.R, KeyCombination.CONTROL_DOWN));
    }

    /**
     * Creates, saves and opens new {@link org.odftoolkit.simple.Document}
     * @param event action event
     */
    @FXML
    public void newDocumentAction(ActionEvent event) {
        KartotekaManager manager = new KartotekaManagerImpl();

        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle(texts.getString("dialog.title.save_document"));
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter(texts.getString("dialog.filter.ods_files"), "*.ods"));

            File selectedFile = fileChooser.showSaveDialog(root.getScene().getWindow());

            if(selectedFile != null) {
                FileManager fm = new FileManagerImpl();
                openedFilePath = selectedFile.getAbsolutePath();
                fm.save(openedFilePath, manager);

                openFile();
                initFilter();
            }
        } catch (CategoryException e) {
            logger.error("New document exception", e);
        } catch (Exception e) {
            logger.error("New document exception", e);
        }
    }

    /**
     * Adds new {@link Category}
     * @param event action event
     * @throws IOException when FXML is not available
     */
    @FXML
    public void addCategoryAction(ActionEvent event) throws IOException {
        if (openedFilePath == null) {
            AlertBox.displayError(texts.getString("error.category"), texts.getString("error.message.no_spreadsheet"));
            return;
        }
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/addCategoryDialog.fxml"), texts);
        Parent root = loader.load();
        Stage newStage = new Stage();
        newStage.setTitle(texts.getString("add_category"));
        newStage.setScene(new Scene(root));
        newStage.initModality(Modality.WINDOW_MODAL);
        newStage.initStyle(StageStyle.UTILITY);
        newStage.initOwner(this.root.getScene().getWindow());

        AddCategoryController controller = loader.getController();
        controller.setKartoteka(kart);

        newStage.showAndWait();

        Category createdCategory = controller.getCategory();

        if (createdCategory != null) {
            try {
                kart.addCategory(createdCategory);
                addTab(createdCategory.getName());
                docSaved = false;
                statusLabel.setText(texts.getString("label.status2") + createdCategory.getName());
            } catch (CategoryException e) {
                logger.error("Add category: ", e);
                statusLabel.setText(texts.getString("label.status3"));
            } catch (NullPointerException e) {
                logger.error("KartotekaManager is null", e);
                AlertBox.displayError(texts.getString("error.oops_error"), texts.getString("error.message.no_spreadsheet"));
                statusLabel.setText(texts.getString("label.status3"));
            }
        } else {
            statusLabel.setText(texts.getString("label.status4"));
        }
    }

    /**
     * Updates existing {@link Category}
     * @param event action event
     * @throws IOException when FXML is not available
     */
    @FXML
    public void updateCategoryAction(ActionEvent event) throws IOException {
        Tab selectedTab = tabPane.getSelectionModel().getSelectedItem();

        if (selectedTab == null) {
            AlertBox.displayError(texts.getString("error.category"), texts.getString("error.message.no_category_chosen"));
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/addCategoryDialog.fxml"),texts);
            Parent root = loader.load();
            Stage newStage = new Stage();
            newStage.setTitle(texts.getString("update_category"));
            newStage.setScene(new Scene(root));
            newStage.initModality(Modality.WINDOW_MODAL);
            newStage.initStyle(StageStyle.UTILITY);
            newStage.initOwner(this.root.getScene().getWindow());

            AddCategoryController controller = loader.getController();
            controller.updateSetUp(kart.getCategory(selectedTab.getText()), kart);
            newStage.showAndWait();

            if (controller.getCategory() != null) {
                selectedTab.setText(controller.getCategory().getName());
                docSaved = false;
                statusLabel.setText(texts.getString("label.status5") + selectedTab.getText());
            } else {
                statusLabel.setText(texts.getString("label.status6"));
            }
        } catch (NullPointerException e) {
            logger.error("KartotekaManager is null", e);
            AlertBox.displayError(texts.getString("error.oops_error"), texts.getString("error.message.no_spreadsheet"));
            statusLabel.setText(texts.getString("label.status7"));
        }

    }

    /**
     * Deletes currently opened {@link Category}
     * @param event action event
     */
    @FXML
    public void deleteCategoryAction(ActionEvent event) {
        Tab selectedTab = tabPane.getSelectionModel().getSelectedItem();

        if (selectedTab == null) {
            AlertBox.displayError(texts.getString("error.category"), texts.getString("error.message.no_category_chosen"));
            return;
        }

        Optional<ButtonType> result = AlertBox.displayConfirmation(texts.getString("delete_category"),
                String.format(texts.getString("delete.category.sure"),selectedTab.getText()));

        if (result.get() == ButtonType.OK) {
            try {
                kart.deleteCategory(kart.getCategory(selectedTab.getText()));
                tabPane.getTabs().remove(selectedTab);
                docSaved = false;
            } catch (CategoryException e) {
                logger.error("Delete Category", e);
                statusLabel.setText(texts.getString("label.status8"));
            } catch (NullPointerException e) {
                logger.error("KartotekaManager or selected tab is null", e);
                AlertBox.displayError(texts.getString("error.oops_error"), texts.getString("error.message.no_spreadsheet"));
                statusLabel.setText(texts.getString("label.status8"));
            }
        }
    }

    /**
     * Adds new {@link Film}
     * @param event action event
     * @throws IOException when FXML is not available
     */
    @FXML
    public void addFilmAction(ActionEvent event) throws IOException {
        if (openedFilePath == null) {
            AlertBox.displayError(texts.getString("error.film"), texts.getString("error.message.no_spreadsheet"));
            return;
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/addFilmDialog.fxml"),texts);
        Parent root = loader.load();
        Stage newStage = new Stage();
        newStage.setTitle(texts.getString("add_movie"));
        newStage.setScene(new Scene(root));
        newStage.initModality(Modality.WINDOW_MODAL);
        newStage.initStyle(StageStyle.UTILITY);
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
                statusLabel.setText(String.format(texts.getString("label.status.movie_added"), createdFilm.getName()));
            } catch (FilmException e) {
                logger.error("Add Film: ", e);
                AlertBox.displayError(texts.getString("error.oops_error"), texts.getString("error.message.film_error"));
                statusLabel.setText(texts.getString("label.status9"));
            }
            catch (NullPointerException e) {
                logger.error("KartotekaManager or tab that was selected is null",e);
                AlertBox.displayError(texts.getString("error.oops_error"), texts.getString("error.message.film_error2"));
                statusLabel.setText(texts.getString("label.status10"));
            }
        } else {
            statusLabel.setText(texts.getString("label.status11"));
        }
    }

    /**
     * Changes {@link Film} category
     * @param event action event
     * @throws IOException when FXML is not available
     */
    @FXML
    public void changeFilmCategory(ActionEvent event) throws IOException {
        Tab selectedTab = tabPane.getSelectionModel().getSelectedItem();
        if (selectedTab == null) {
            AlertBox.displayError(texts.getString("error.category"), texts.getString("error.message.no_cat_no_spread"));
            return;
        }

        Film selectedFilm = ((TableView<Film>)selectedTab.getContent()).getSelectionModel().getSelectedItem();
        if (selectedFilm == null) {
            AlertBox.displayError(texts.getString("error.film"), texts.getString("error.message.no_movie_selected"));
            return;
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/changeCategoryDialog.fxml"),texts);
        Parent root = loader.load();
        Stage newStage = new Stage();
        newStage.setTitle(texts.getString("update_movie"));
        newStage.setScene(new Scene(root));
        newStage.initModality(Modality.WINDOW_MODAL);
        newStage.initStyle(StageStyle.UTILITY);
        newStage.initOwner(this.root.getScene().getWindow());

        ChangeCategoryController controller = loader.getController();
        controller.initializeData(kart, selectedFilm, kart.getCategory(selectedTab.getText()));

        int selectedTabIndex = tabPane.getSelectionModel().getSelectedIndex();

        newStage.showAndWait();
        if (controller.isOkPressed()) {
            docSaved = false;
            refreshTableData(null);
            tabPane.getSelectionModel().select(selectedTabIndex);
        }
    }

    /**
     * Updates {@link Film}
     * @param event action event
     * @throws IOException when FXML is not available
     */
    @FXML
    public void updateFilmAction(ActionEvent event) throws IOException {
        Tab selectedTab = tabPane.getSelectionModel().getSelectedItem();
        if (selectedTab == null) {
            AlertBox.displayError(texts.getString("error.category"), texts.getString("error.message.no_cat_no_spread"));
            return;
        }

        Film selectedFilm = ((TableView<Film>) selectedTab.getContent()).getSelectionModel().getSelectedItem();
        if (selectedFilm == null) {
            AlertBox.displayError(texts.getString("error.film"), texts.getString("error.message.no_movie_selected"));
            return;
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/addFilmDialog.fxml"), texts);
        Parent root = loader.load();
        Stage newStage = new Stage();
        newStage.setTitle(texts.getString("update_movie"));
        newStage.setScene(new Scene(root));
        newStage.initModality(Modality.WINDOW_MODAL);
        newStage.initStyle(StageStyle.UTILITY);
        newStage.initOwner(this.root.getScene().getWindow());
        try {
            AddFilmController controller = loader.getController();
            controller.updateSetUp(selectedFilm);

            newStage.showAndWait();

            if (controller.getFilm() != null) {
                ((TableView) selectedTab.getContent()).getItems().clear();
                ((TableView) selectedTab.getContent()).setItems(
                        FXCollections.observableArrayList(kart.getCategory(selectedTab.getText()).getFilms())
                );

                docSaved = false;
                statusLabel.setText(
                        String.format(texts.getString("label.status.movie_updated"),
                                controller.getFilm().getName())
                );
            } else {
                statusLabel.setText(texts.getString("label.status.movie_update_cancelled"));
            }
        } catch (NullPointerException e) {
            logger.error("KartotekaManager or tab that was selected is null", e);
            AlertBox.displayError(texts.getString("error.oops_error"), texts.getString("error.message.film_error2"));
            statusLabel.setText(texts.getString("label.status12"));
        }

    }

    /**
     * Deletes currently selected {@link Film}
     * @param event action event
     */
    @FXML
    public void deleteFilmAction(ActionEvent event) {
        Tab selectedTab = tabPane.getSelectionModel().getSelectedItem();
        if (selectedTab == null) {
            AlertBox.displayError(texts.getString("error.category"), texts.getString("error.message.no_cat_no_spread"));
            return;
        }

        Film selectedFilm = ((TableView<Film>) selectedTab.getContent()).getSelectionModel().getSelectedItem();
        if (selectedFilm == null) {
            AlertBox.displayError(texts.getString("error.film"), texts.getString("error.message.no_movie_selected"));
            return;
        }

        Optional<ButtonType> result = AlertBox.displayConfirmation(texts.getString("delete_movie"),
                String.format(texts.getString("dialog.film.delete"), selectedFilm.getName()));

        if (result.get() == ButtonType.OK) {

            try {
                kart.getCategory(selectedTab.getText()).deleteFilm((int) selectedFilm.getId());
                ((TableView) selectedTab.getContent()).getItems().remove(selectedFilm);
                docSaved = false;
            } catch (FilmException e) {
                logger.error("Delete film action", e);
                statusLabel.setText(texts.getString("label.status13"));
            } catch (NullPointerException e) {
                logger.error("KartotekaManager or tab that was selected is null", e);
                AlertBox.displayError(texts.getString("error.oops_error"), texts.getString("error.message.film_error2"));
                statusLabel.setText(texts.getString("label.status13"));
            }
        }
    }

    /**
     * Finds film in whole database
     * @param event action event
     * @throws IOException when FXML is not available
     */
    @FXML
    public void findFilmAction(ActionEvent event) throws IOException {
        if (openedFilePath == null) {
            AlertBox.displayError(texts.getString("error.film"), texts.getString("error.message.no_spreadsheet"));
            return;
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/findFilmDialog.fxml"), texts);
        Parent root = loader.load();
        Stage newStage = new Stage();
        newStage.setTitle(texts.getString("find_movie"));
        newStage.setScene(new Scene(root));
        newStage.initModality(Modality.WINDOW_MODAL);
        newStage.initStyle(StageStyle.UTILITY);
        newStage.initOwner(this.root.getScene().getWindow());

        FindFilmController controller = loader.getController();
        controller.setKartoteka(kart);

        newStage.showAndWait();

        Tuple<Long, Long> result = controller.getResult();

        if (result != null) {
            filterTextField.setText("");

            Category selectedCategory = kart.getCategory(result.getFirst());
            Film selectedFilm = selectedCategory.getFilm(result.getSecond());

            for (Tab tab : tabPane.getTabs()) {
                if (tab.getText().equals(selectedCategory.getName())) {
                    tabPane.getSelectionModel().select(tab);
                    ((TableView) tab.getContent()).getSelectionModel().select(selectedFilm);
                    break;
                }
            }
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
        fileChooser.setTitle(texts.getString("dialog.title.open_document"));
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter(texts.getString("dialog.filter.ods_files"), "*.ods"),
                new FileChooser.ExtensionFilter(texts.getString("dialog.filter.all_files"), "*.*"));

        File selectedFile = fileChooser.showOpenDialog(root.getScene().getWindow());
        if (selectedFile != null) {
            openedFilePath = selectedFile.getAbsolutePath();
            openFile();
            initFilter();
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
        fileChooser.setTitle(texts.getString("dialog.title.save_document"));
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter(texts.getString("dialog.filter.ods_files"), "*.ods"));
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
            AlertBox.displayError(texts.getString("error.file"), texts.getString("error.file.nothing_to_save"));
        } else {
            saveFile(openedFilePath);
        }
    }

    /**
     * Refreshes the {@link Controller#tabPane}
     * @param event action event
     */
    @FXML
    public void refreshTableData(ActionEvent event) {
        if (kart == null) {
            AlertBox.displayError(texts.getString("error.refresh"), texts.getString("error.message.refresh"));
            return;
        }

        filterTextField.setText("");
        tabPane.getTabs().clear();
        for(Category category : kart.getCategories()) {
            addTab(category.getName());
        }
    }

    /**
     * Closes application
     * @param event action event
     */
    @FXML
    public void closeAppAction(ActionEvent event) {
        unsavedChanges();
    }

    /**
     * Checks unsaved changes, if there aren't any or user allows it, app quits
     */
    public void unsavedChanges() {
        if (!docSaved){
            Optional<ButtonType> result = AlertBox.displayConfirmation(texts.getString("error.file.unsaved_changes"),
                    texts.getString("error.file.unsaved_changes_exit"));

            if (result.get() == ButtonType.OK) {
                statusLabel.setText(texts.getString("label.status14"));
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
            statusLabel.setText(String.format(texts.getString("label.status.file_opened"), openedFilePath));
        } catch (UnsupportedOperationException ex) {
            AlertBox.displayError(texts.getString("error.oops_error"), texts.getString("unsupported_format"));
        } catch (Exception e) {
            logger.error("Open File", e);
            statusLabel.setText(texts.getString("label.status15"));
        }

        refreshTableData(null);
    }

    /**
     * Saves the file
     * @param path file path
     */
    private void saveFile(String path) {
        try {
            fm.save(path, kart);
            docSaved = true;
            statusLabel.setText("Status: " + texts.getString("status.changes_saved"));
        } catch (Exception e) {
            logger.error("Save File: ", e);
            statusLabel.setText(texts.getString("label.status1"));
        }
    }

    /**
     * Adds tabs to the {@link Controller#tabPane}
     * @param name tab name
     */
    private void addTab(String name) {
        Tab tab = new Tab(name);
        TableView<Film> tableView = new TableView<>();
        tableView.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
        tableView.setPlaceholder(new Text(texts.getString("empty_tableview")));

        TableColumn<Film, String> nameCol = new TableColumn<>(texts.getString("movie_name"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Film, String> yearCol = new TableColumn<>(texts.getString("year"));
        //yearCol.setCellValueFactory(new PropertyValueFactory<>("year"));
        yearCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Film, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Film, String> param) {
                return new ReadOnlyObjectWrapper<String>(param.getValue().getYear());
            }
        });

        TableColumn<Film, String> ratingCol = new TableColumn<>(texts.getString("rating"));
        ratingCol.setCellValueFactory(new PropertyValueFactory<>("rating"));

        TableColumn<Film, String> directorCol = new TableColumn<>(texts.getString("director"));
        directorCol.setCellValueFactory(new PropertyValueFactory<>("director"));

        TableColumn<Film, String> descriptonCol = new TableColumn<>(texts.getString("description"));
        descriptonCol.setCellValueFactory(new PropertyValueFactory<>("description"));

        tableView.getColumns().addAll(nameCol, yearCol, ratingCol, directorCol, descriptonCol);

        tableView.setItems(FXCollections.observableArrayList(kart.getCategory(name).getFilms()));

        tab.setContent(tableView);
        tabPane.getTabs().add(tab);
    }

    /**
     * Initialize filter text field, method implements listener that monitoring changes in textField
     */
    private void initFilter() {
        filterTextField.textProperty().addListener(new InvalidationListener() {

            @Override
            public void invalidated(Observable o) {
                String filter = filterTextField.textProperty().get();
                for (Tab tab : tabPane.getTabs()) {
                    List<Film> films = kart.getCategory(tab.getText()).getFilms();
                    TableView tableView = ((TableView) tab.getContent());

                    if (filter.isEmpty()) {
                        tableView.setItems(FXCollections.observableArrayList(films));
                        return;
                    }

                    ObservableList<Film> tableItems = FXCollections.observableArrayList();
                    ObservableList<TableColumn<Film, ?>> cols = ((TableView) tab.getContent()).getColumns();
                    for (int i = 0; i < films.size(); i++) {
                        TableColumn col = null;

                        // Find movie column
                        for (TableColumn c : cols) {
                            if (c.getText().equals(texts.getString("movie_name"))) {
                                col = c;
                                break;
                            }
                        }
                        String cellValue = col.getCellData(films.get(i)).toString().toLowerCase();
                        if (cellValue.contains(filter.toLowerCase())) {
                            tableItems.add(films.get(i));
                        }
                    }
                    tableView.setItems(tableItems);
                }
            }
        });
    }
}