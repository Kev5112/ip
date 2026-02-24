package alterego.ui;

import alterego.AlterEgo;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainWindow extends AnchorPane {
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox dialogContainer;
    @FXML
    private TextField userInput;
    @FXML
    private Button sendButton;

    private AlterEgo alterEgo;
    private Stage stage;

    private Image userImage = new Image(this.getClass().getResourceAsStream("/images/TralaleroTralala.png"));
    private Image alterEgoImage = new Image(this.getClass().getResourceAsStream("/images/TungTungTungSahur.png"));

    @FXML
    public void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());

        dialogContainer.getChildren().add(
                DialogBox.getAlterEgoDialog(Ui.hello(), alterEgoImage)
        );
    }

    public void setAlterEgo(AlterEgo a) {
        alterEgo = a;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    private void handleUserInput() throws InterruptedException {
        String input = userInput.getText();
        String response = alterEgo.getResponse(input);

        dialogContainer.getChildren().addAll(
                DialogBox.getUserDialog(input, userImage),
                DialogBox.getAlterEgoDialog(response, alterEgoImage)
        );

        userInput.clear();

        if (input.equals("bye")) {
            Thread.sleep(1000);
            stage.close();
        }
    }
}
