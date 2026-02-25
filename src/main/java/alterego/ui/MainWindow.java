package alterego.ui;

import alterego.AlterEgo;
import alterego.task.TaskList;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

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
    }

    public void setAlterEgo(AlterEgo a) {
        alterEgo = a;
        showWelcomeMessage();
    }

    private void showWelcomeMessage() {
        assert alterEgo != null : "Alter Ego hasn't been initialised?";
        dialogContainer.getChildren().add(
                DialogBox.getAlterEgoDialog(Ui.decorate(Ui.hello()
                                + (alterEgo.getLoadStatus() != null ? "\n" + alterEgo.getLoadStatus() : "")),
                        alterEgoImage)
        );
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
                DialogBox.getAlterEgoDialog(Ui.decorate(response), alterEgoImage)
        );

        userInput.clear();

        if (input.equals("bye")) {
            PauseTransition delay = new PauseTransition(Duration.seconds(1.5));
            delay.setOnFinished(event -> stage.close());
            delay.play();
        }
    }
}
