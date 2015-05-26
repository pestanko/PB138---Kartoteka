package cz.muni.fi.pb138.kartoteka;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Main initialization class
 *
 * @author Peter Stanko
 * @author Dominik Labuda
 * @author Peter Zaoral
 * @version 2015-05-18
 */
public class Program extends Application {

    /**
     * Starts the javafx UI
     * @param primaryStage Main {@link Stage}
     */
    @Override
    public void start(Stage primaryStage) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/NewUI.fxml"));
            primaryStage.setTitle("PB138 - Kartoteka");
            primaryStage.setScene(new Scene(root, 1280, 720));
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Main method
     * @param args command line args
     */
    public static void main(String[] args) {
        launch(args);
    }
}
