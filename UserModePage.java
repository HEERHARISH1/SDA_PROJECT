import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class UserModePage extends Application {
    private String loggedInEmail; // Store the logged-in email
    private int loggedInUserId; // Store the logged-in user's ID
    private String username;
    private List<Product> cart = new ArrayList<>(); // Cart for selected products
    public UserModePage(String email) {
        this.loggedInEmail = email;
        this.loggedInUserId = 1; // Default value if no userId is provided
    }
    // Constructor to pass the logged-in user's email and ID
    public UserModePage(String username, int userId) {
        this.loggedInEmail = username;
        this.loggedInUserId = userId;
    }

    public UserModePage() {
        // Default constructor
    }

    @Override
    public void start(Stage primaryStage) {
        // Root Layout
        AnchorPane root = new AnchorPane();
        root.setPrefSize(600, 400);

        // Apply background
        UIHelper.setBackground(root, "file:src/bg.jpg");

        // Title Label
        Label titleLabel = new Label("User Mode");
        titleLabel.setFont(UIHelper.getTitleFont());
        titleLabel.setStyle("-fx-text-fill: #ffffff;"); // White text for visibility
        titleLabel.setLayoutX(200);
        titleLabel.setLayoutY(40);

        // Welcome Message
        Label welcomeLabel = new Label("Welcome, " + (loggedInEmail != null ? loggedInEmail : "User") + "!");
        welcomeLabel.setFont(UIHelper.getLabelFont());
        welcomeLabel.setStyle("-fx-text-fill: #ffffff;");
        welcomeLabel.setLayoutX(200);
        welcomeLabel.setLayoutY(100);

        // Instructions Label
        Label instructionLabel = new Label("Choose an action below:");
        instructionLabel.setFont(UIHelper.getLabelFont());
        instructionLabel.setStyle("-fx-text-fill: #ffffff;");
        instructionLabel.setLayoutX(200);
        instructionLabel.setLayoutY(130);

        // Button to View Products
        Button viewProductsButton = UIHelper.createStyledButton("View Available Products");
        viewProductsButton.setLayoutX(200);
        viewProductsButton.setLayoutY(180);
        viewProductsButton.setOnAction(e -> {
            ViewProductsPage viewProductsPage = new ViewProductsPage(cart);
            viewProductsPage.start(primaryStage);
        });

        // Button to Leave Feedback
        Button leaveFeedbackButton = UIHelper.createStyledButton("Leave Feedback");
        leaveFeedbackButton.setLayoutX(200);
        leaveFeedbackButton.setLayoutY(230);
        leaveFeedbackButton.setOnAction(e -> {
            // Navigate to LeaveFeedbackPage
            LeaveFeedbackPage leaveFeedbackPage = new LeaveFeedbackPage(loggedInUserId);
            leaveFeedbackPage.start(primaryStage);
        });

        // Logout Button
        Button logoutButton = UIHelper.createStyledButton("Logout");
        logoutButton.setStyle("-fx-background-color: #d9534f; -fx-text-fill: white;"); // Red Logout button
        logoutButton.setLayoutX(200);
        logoutButton.setLayoutY(280);
        logoutButton.setOnAction(e -> {
            LoginPage loginPage = new LoginPage();
            loginPage.start(primaryStage);
        });

        // Add UI elements to the root layout
        root.getChildren().addAll(titleLabel, welcomeLabel, instructionLabel, viewProductsButton, leaveFeedbackButton, logoutButton);

        // Scene Setup
        Scene scene = new Scene(root);
        primaryStage.setTitle("User Mode");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
