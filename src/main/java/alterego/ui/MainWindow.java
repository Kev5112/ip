package alterego.ui;

import alterego.AlterEgo;
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

/**
 * Represents the controller class for the main application window.
 * Manages user interactions, displays dialog boxes, and coordinates
 * between the UI components and the AlterEgo backend.
 */
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

    /**
     * Initializes the controller after FXML loading.
     * Binds the scroll pane to automatically scroll down as new messages appear,
     * and disables the send button when the input field is empty.
     */
    @FXML
    public void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
        sendButton.disableProperty().bind(userInput.textProperty().isEmpty());
    }

    /**
     * Sets the AlterEgo instance and displays the welcome message.
     * @param a The AlterEgo instance to use for processing commands
     */
    public void setAlterEgo(AlterEgo a) {
        alterEgo = a;
        showWelcomeMessage();
    }

    /**
     * Displays the welcome message when the application starts.
     * Includes any load status warnings from the data files.
     */
    private void showWelcomeMessage() {
        assert alterEgo != null : "Alter Ego hasn't been initialised?";
        dialogContainer.getChildren().add(
                DialogBox.getAlterEgoDialog(Ui.decorate(Ui.hello()
                                + (alterEgo.getLoadStatus() != null ? "\n" + alterEgo.getLoadStatus() : "")),
                        alterEgoImage)
        );
    }

    /**
     * Sets the primary stage reference for window operations.
     * @param stage The primary stage of the application
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Handles user input when the send button is clicked or Enter is pressed.
     */
    @FXML
    private void handleUserInput() {
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
