package sample;

import cz.muni.fi.pb138.kartoteka.entities.Category;
import cz.muni.fi.pb138.kartoteka.exceptions.CategoryException;
import cz.muni.fi.pb138.kartoteka.loaders.FileManager;
import cz.muni.fi.pb138.kartoteka.loaders.FileManagerImpl;
import cz.muni.fi.pb138.kartoteka.managers.KartotekaManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 * Created by Peter Zaoral on 8.5.2015.
 */
public class Controller implements Initializable {
    FileManager fm = new FileManagerImpl();
    ObservableList<String> list = FXCollections.observableArrayList();
    KartotekaManager kart;

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
        } catch (CategoryException e) {
            e.printStackTrace();
        }

    }
    @FXML
    private void saveChangesAction(ActionEvent event)
    {
        try {
            fm.save("dokument.ods", kart);
            statusLabel.setText("Status: Changes saved");
        } catch (Exception e) {
            e.printStackTrace();
            statusLabel.setText("Status: Exception was thrown during saving. Try again.");
        }
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            kart = fm.load("dokument.ods");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (kart!= null )
        {
            list.addAll(kart.getCategories().stream().map(Category::getName).collect(Collectors.toList()));
        }
        comboBox.setItems(list);
    }
}