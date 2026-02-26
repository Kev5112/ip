package alterego.ui;

import java.io.IOException;

import alterego.AlterEgo;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * The main entry point for the JavaFX application.
 * Initializes the primary stage and loads the main window FXML.
 * Sets up the AlterEgo chatbot instance and connects it to the UI controller.
 */
public class Main extends Application {
    private AlterEgo alterEgo = new AlterEgo("./data/alterego.AlterEgo.txt", "./data/alterego.AlterEgoContacts.txt");

    /**
     * Starts the JavaFX application by setting up the primary stage.
     * Loads the MainWindow FXML, configures window properties,
     * and connects the UI controller with the AlterEgo backend.
     * @param stage The primary stage for this JavaFX application
     */
    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/MainWindow.fxml"));
            AnchorPane ap = fxmlLoader.load();
            Scene scene = new Scene(ap, 800, 600);
            stage.setScene(scene);
            stage.setTitle("AlterEgo");
            stage.setMinHeight(400);
            stage.setMinWidth(600);
            MainWindow controller = fxmlLoader.getController();
            controller.setAlterEgo(alterEgo);
            controller.setStage(stage);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
