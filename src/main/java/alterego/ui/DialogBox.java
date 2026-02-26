package alterego.ui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;

import java.io.IOException;
import java.util.Collections;

/**
 * Represents a dialog box in the chat interface.
 * Each dialog box contains a message label and a profile picture.
 */
public class DialogBox extends HBox {
    @FXML
    private Label dialog;
    @FXML
    private ImageView displayPicture;

    /**
     * Creates a new dialog box with the specified message and profile image.
     * @param s The message text to display
     * @param i The profile image to show
     */
    public DialogBox(String s, Image i) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MainWindow.class.getResource("/view/DialogBox.fxml"));
            fxmlLoader.setController(this);
            fxmlLoader.setRoot(this);
            fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        dialog.setText(s);
        displayPicture.setImage(i);
        dialog.setFont(Font.font("Monospaced", javafx.scene.text.FontWeight.BOLD, 13));
    }

    /**
     * Flips the dialog box layout for AlterEgo messages.
     * Reverses child nodes, sets left alignment, and applies gray bubble styling.
     */
    private void flip() {
        ObservableList<Node> tmp = FXCollections.observableArrayList(this.getChildren());
        Collections.reverse(tmp);
        getChildren().setAll(tmp);
        setAlignment(Pos.TOP_LEFT);
        this.setSpacing(20);
        dialog.setStyle("-fx-background-color: #E0E0E0; -fx-background-radius: 15; -fx-padding: 10");
    }

    /**
     * Creates a dialog box for user messages.
     * User messages appear on the right side
     * @param s The user's message text
     * @param i The user's profile image
     * @return A dialog box for user messages
     */
    public static DialogBox getUserDialog(String s, Image i) {
        var db = new DialogBox(s, i);
        db.setAlignment(Pos.TOP_RIGHT);
        db.setSpacing(20);
        db.dialog.setStyle("-fx-background-color: #E0E0E0; -fx-background-radius: 15; -fx-padding: 10;");
        return db;
    }

    /**
     * Creates a dialog box for AlterEgo messages.
     * AlterEgo messages appear on the left side
     * @param s The AlterEgo's message text
     * @param i The AlterEgo's profile image
     * @return A dialog box for AlterEgo messages
     */
    public static DialogBox getAlterEgoDialog(String s, Image i) {
        var db = new DialogBox(s, i);
        db.flip();
        return db;
    }
}
