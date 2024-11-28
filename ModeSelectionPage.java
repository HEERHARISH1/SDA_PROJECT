import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class ModeSelectionPage extends Application {
    private String loggedInEmail;
    private String username;
    private int userId;
    // Constructor to pass the logged-in user's email
    public ModeSelectionPage(String email) {
        this.loggedInEmail = email;
    }
    public ModeSelectionPage(String username, int userId) {
        this.username = username;
        this.userId = userId;
    }
    @Override
    public void start(Stage primaryStage) {
        VBox root = new VBox(20); // Vertical layout with spacing
        root.setPrefSize(600, 400);
        root.setAlignment(javafx.geometry.Pos.CENTER); // Center alignment

        // Set background using UIHelper
        UIHelper.setBackground(root, "file:src/bg.jpg");

        // Welcome Label
        Label welcomeLabel = new Label("Welcome! Logged in as: " + loggedInEmail);
        welcomeLabel.setFont(UIHelper.getLabelFont());
        welcomeLabel.setStyle("-fx-text-fill: white;"); // White text for better visibility

        // Mode Selection Label
        Label modeLabel = new Label("Select your mode:");
        modeLabel.setFont(UIHelper.getTitleFont());
        modeLabel.setStyle("-fx-text-fill: #ffcc00;"); // Golden-yellow text

        // User Mode Button
        Button userModeButton = UIHelper.createStyledButton("Enter User Mode");
        userModeButton.setOnAction(e -> {
            UserModePage userModePage = new UserModePage();
            userModePage.start(primaryStage); // Navigate to User Mode page
        });

        // Owner Mode Button
        Button ownerModeButton = UIHelper.createStyledButton("Enter Owner Mode");
        ownerModeButton.setOnAction(e -> {
            OwnerModePage ownerModePage = new OwnerModePage();
            ownerModePage.start(primaryStage); // Navigate to Owner Mode page
        });

        // Logout Button
        Button logoutButton = UIHelper.createStyledButton("Logout");
        logoutButton.setStyle("-fx-background-color: #d9534f; -fx-text-fill: white;"); // Red logout button
        logoutButton.setOnAction(e -> {
            LoginPage loginPage = new LoginPage();
            loginPage.start(primaryStage); // Navigate back to Login Page
        });

        // Add all components to the root layout
        root.getChildren().addAll(welcomeLabel, modeLabel, userModeButton, ownerModeButton, logoutButton);

        // Create the scene and set it on the stage
        Scene scene = new Scene(root);
        primaryStage.setTitle("Mode Selection");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
