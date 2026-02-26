package alterego.ui;

import alterego.AlterEgo;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    private AlterEgo alterEgo = new AlterEgo("./data/alterego.AlterEgo.txt", "./data/alterego.AlterEgoContacts.txt");

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
