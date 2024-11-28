import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class UIHelper {

    // Set the background for any AnchorPane layout
    public static void setBackground(AnchorPane pane, String imagePath) {
        BackgroundImage backgroundImage = new BackgroundImage(
                new Image(imagePath, 600, 400, false, true),
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                BackgroundSize.DEFAULT
        );
        pane.setBackground(new Background(backgroundImage));
    }

    // Set the background for any Pane layout
    public static void setBackground(Pane pane, String imagePath) {
        BackgroundImage backgroundImage = new BackgroundImage(
                new Image(imagePath, pane.getPrefWidth(), pane.getPrefHeight(), false, true),
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                BackgroundSize.DEFAULT
        );
        pane.setBackground(new Background(backgroundImage));
    }

    // Create a styled label
    public static Label createStyledLabel(String text) {
        Label label = new Label(text);
        label.setFont(Font.font("Tahoma", FontWeight.BOLD, 14));
        return label;
    }

    // Create a styled button
    public static Button createStyledButton(String text) {
        Button button = new Button(text);
        button.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        button.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-padding: 10 20 10 20; " +
                        "-fx-background-radius: 10;");
        button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: #45a049; -fx-text-fill: white;"));
        button.setOnMouseExited(e -> button.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;"));
        return button;
    }

    // Create a back button with a previous Scene
    public static Button createBackButton(Stage primaryStage, Scene previousScene) {
        Button backButton = createStyledButton("Back");
        backButton.setOnAction(e -> primaryStage.setScene(previousScene)); // Navigate back to the previous Scene
        return backButton;
    }

    // Create a back button with a previous Page
    public static Button createBackButton(Stage primaryStage, Application previousPage) {
        Button backButton = createStyledButton("Back");
        backButton.setOnAction(e -> {
            try {
                previousPage.start(primaryStage); // Navigate back to the previous Page
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        return backButton;
    }

    public static void showAlert(Alert.AlertType alertType, String title, String header, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
    // Utility method to get Title font
    public static Font getTitleFont() {
        return Font.font("Verdana", FontWeight.BOLD, 30);
    }

    // Utility method to get Label font
    public static Font getLabelFont() {
        return Font.font("Tahoma", 14);
    }
}
