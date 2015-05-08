package sample;

import cz.muni.fi.pb138.kartoteka.entities.Category;
import cz.muni.fi.pb138.kartoteka.exceptions.CategoryException;
import cz.muni.fi.pb138.kartoteka.loaders.FileManager;
import cz.muni.fi.pb138.kartoteka.loaders.FileManagerImpl;
import cz.muni.fi.pb138.kartoteka.managers.KartotekaManager;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 * Created by Peter Zaoral on 8.5.2015.
 */
public class Controller implements Initializable {
    private FileManager fm = new FileManagerImpl();
    private ObservableList<String> list = FXCollections.observableArrayList();
    private KartotekaManager kart;
    private String openedFilePath;
    private boolean docChanged = false;

    @FXML
    public Parent root;
    @FXML
    private ComboBox<String> comboBox;
    @FXML
    private Label statusLabel;
    @FXML
    private TextField addCategoryTextField;
    @FXML
    public Label label;
    @FXML
    public Button saveButton;
    @FXML
    public Button addButton;

    @FXML
    public void addButtonAction(ActionEvent event)
    {
        try {
            Category category = new Category(addCategoryTextField.getText());
            kart.addCategory(category);
            list.add(category.getName());
            comboBox.setItems(list);
            docChanged = true;
        } catch (CategoryException e) {
            e.printStackTrace();
        }

    }

    @FXML
    public void openFile(ActionEvent event) {
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
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (kart != null)
            {
                list.addAll(kart.getCategories().stream().map(Category::getName).collect(Collectors.toList()));
            }
            comboBox.setItems(list);
        }
    }

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

    @FXML
    public void saveChangesAction(ActionEvent event) {
        saveFile(openedFilePath);
    }

    private void saveFile(String path) {
        try {
            fm.save(path, kart);
            statusLabel.setText("Status: Changes saved");
        } catch (Exception e) {
            e.printStackTrace();
            statusLabel.setText("Status: Exception was thrown during saving. Try again.");
        }
    }

    @FXML
    public void closeApp(ActionEvent event) {
        Platform.exit();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}