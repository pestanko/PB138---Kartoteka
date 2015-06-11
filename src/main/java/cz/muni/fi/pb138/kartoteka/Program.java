package cz.muni.fi.pb138.kartoteka;

import cz.muni.fi.pb138.kartoteka.gui.AlertBox;
import cz.muni.fi.pb138.kartoteka.gui.Controller;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ResourceBundle;

/**
 * Main initialization class
 *
 * @author Peter Stanko
 * @author Dominik Labuda
 * @author Peter Zaoral
 * @version 2015-06-04
 */
public class Program extends Application {

    /**
     * Class logger
     */
    private final static Logger logger = LoggerFactory.getLogger(Program.class);

    /**
     * Loader fot the main UI window
     */
    private FXMLLoader loader;

    /**
     * Starts the javafx UI
     * @param primaryStage Main {@link Stage}
     */
    @Override
    public void start(Stage primaryStage) {
        try {
            ResourceBundle texts = ResourceBundle.getBundle("texts");
            loader = new FXMLLoader(getClass().getResource("/fxml/NewUI.fxml"),texts);
            Parent root = loader.load();
            primaryStage.setTitle("PB138 - Kartoteka");
            primaryStage.setScene(new Scene(root, 1280, 720));
            primaryStage.setOnCloseRequest(event -> {
                    event.consume();
                    ((Controller) loader.getController()).unsavedChanges();
                }
            );
            primaryStage.show();
        } catch (Throwable e) {
            AlertBox.displayError("Error dialog", "Oops something went wrong ...");
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
