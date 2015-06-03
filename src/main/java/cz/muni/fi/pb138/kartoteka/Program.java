package cz.muni.fi.pb138.kartoteka;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Main initialization class
 *
 * @author Peter Stanko
 * @author Dominik Labuda
 * @author Peter Zaoral
 * @version 2015-05-26
 */
public class Program extends Application {

    /**
     * Class logger
     */
    final static Logger logger = LoggerFactory.getLogger(Program.class);

    /**
     * Starts the javafx UI
     * @param primaryStage Main {@link Stage}
     */
    @Override
    public void start(Stage primaryStage) {
        try {
            ResourceBundle texts = ResourceBundle.getBundle("texts");
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/NewUI.fxml"),texts);
            primaryStage.setTitle("PB138 - Kartoteka");
            primaryStage.setScene(new Scene(root, 1280, 720));
            primaryStage.show();
        } catch (Throwable e) {
            JOptionPane.showMessageDialog(null, "Oops something went wrong ...");
            logger.error("Main Exception -> WRONG ! ", e);
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
