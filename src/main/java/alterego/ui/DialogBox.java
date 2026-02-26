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

public class DialogBox extends HBox {
    @FXML
    private Label dialog;
    @FXML
    private ImageView displayPicture;

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

    private void flip() {
        ObservableList<Node> tmp = FXCollections.observableArrayList(this.getChildren());
        Collections.reverse(tmp);
        getChildren().setAll(tmp);
        setAlignment(Pos.TOP_LEFT);
        this.setSpacing(20);
        dialog.setStyle("-fx-background-color: #E0E0E0; -fx-background-radius: 15; -fx-padding: 10");
    }

    public static DialogBox getUserDialog(String s, Image i) {
        var db = new DialogBox(s, i);
        db.setAlignment(Pos.TOP_RIGHT);
        db.setSpacing(20);
        db.dialog.setStyle("-fx-background-color: #E0E0E0; -fx-background-radius: 15; -fx-padding: 10;");
        return db;
    }

    public static DialogBox getAlterEgoDialog(String s, Image i) {
        var db = new DialogBox(s, i);
        db.flip();
        return db;
    }
}
