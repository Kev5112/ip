package alterego;

import alterego.ui.Main;
import javafx.application.Application;

/**
 * Entry point for the AlterEgo application when packaged as a JAR file.
 * Launches the JavaFX application by delegating to the Main class.
 */
public class Launcher {
    /**
     * The main entry point of the application.
     * Launches the JavaFX application
     * @param args Command-line arguments passed to the application
     */
    public static void main(String[] args) {
        Application.launch(Main.class, args);
    }
}
