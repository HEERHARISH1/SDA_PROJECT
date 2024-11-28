import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class OwnerModePage extends Application {
    @Override
    public void start(Stage primaryStage) {
        // Root layout
        VBox root = new VBox(20); // Vertical layout with spacing
        root.setPrefSize(600, 400);
        root.setAlignment(Pos.CENTER); // Center align components

        // Set background using UIHelper
        UIHelper.setBackground(root, "file:src/bg.jpg");

        // Title Label
        Label titleLabel = new Label("Welcome to Owner Mode!");
        titleLabel.setFont(UIHelper.getTitleFont());
        titleLabel.setStyle("-fx-text-fill: #ffffff;"); // White text for visibility

        // Buttons
        Button addProductBtn = UIHelper.createStyledButton("Add New Product");
        Button viewRentalsBtn = UIHelper.createStyledButton("View Active Rentals");
        Button rentalHistoryBtn = UIHelper.createStyledButton("Rental History");
        Button viewFeedbackBtn = UIHelper.createStyledButton("View Feedback");
        Button backButton = UIHelper.createBackButton(primaryStage, new ModeSelectionPage("owner@example.com")); // Example email

        // Button Actions
        addProductBtn.setOnAction(e -> AddProductWindow.display(primaryStage)); // Navigate to Add Product
        viewRentalsBtn.setOnAction(e -> ActiveRentalsWindow.display(primaryStage)); // Navigate to Active Rentals
        rentalHistoryBtn.setOnAction(e -> RentalHistoryWindow.display(primaryStage)); // Navigate to Rental History
        viewFeedbackBtn.setOnAction(e -> {
            FeedbackWindow feedbackWindow = new FeedbackWindow();
            feedbackWindow.display(primaryStage); // Navigate to Feedback
        });
        backButton.setOnAction(e -> {
            ModeSelectionPage modeSelectionPage = new ModeSelectionPage("owner@example.com");
            modeSelectionPage.start(primaryStage); // Navigate back to mode selection
        });

        // Add elements to layout
        root.getChildren().addAll(titleLabel, addProductBtn, viewRentalsBtn, rentalHistoryBtn, viewFeedbackBtn, backButton);

        // Create scene and set the stage
        Scene scene = new Scene(root);
        primaryStage.setTitle("Owner Mode");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
