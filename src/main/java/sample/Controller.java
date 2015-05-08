package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;


import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Peter Zaoral on 8.5.2015.
 */
public class Controller implements Initializable {

    @FXML
    private ComboBox<String> comboBox;
    @FXML
    private Label label;
    @FXML
    private void buttonAction(ActionEvent event)
    {
        label.setText("Selected value "+ comboBox.getValue());
    }
    ObservableList<String> list = FXCollections.observableArrayList("Value-1","Value-2","Value-3","Value-4","Value-5","Value-6");

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        comboBox.setItems(list);
    }
}