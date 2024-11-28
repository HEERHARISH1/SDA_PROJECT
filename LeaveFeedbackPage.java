import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LeaveFeedbackPage {
    private int loggedInUserId = 1; // Hardcoded renter_id for testing
    private int renterId;
    public LeaveFeedbackPage(int renterId) {
        this.renterId = renterId;
    }


    public void start(Stage primaryStage) {
        // Root Layout
        AnchorPane root = new AnchorPane();
        root.setPrefSize(600, 400);

        // Title Label
        Label titleLabel = new Label("Leave Feedback");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        titleLabel.setLayoutX(200);
        titleLabel.setLayoutY(20);

        // ComboBox to select products
        ComboBox<Product> productComboBox = new ComboBox<>();
        productComboBox.setPromptText("Select a product");
        productComboBox.setLayoutX(200);
        productComboBox.setLayoutY(80);

        // Rating Input
        Label ratingLabel = new Label("Rating (1-5):");
        ratingLabel.setLayoutX(200);
        ratingLabel.setLayoutY(140);

        TextField ratingField = new TextField();
        ratingField.setPromptText("Enter rating");
        ratingField.setLayoutX(200);
        ratingField.setLayoutY(160);

        // Comment Input
        Label commentLabel = new Label("Comment:");
        commentLabel.setLayoutX(200);
        commentLabel.setLayoutY(200);

        TextArea commentArea = new TextArea();
        commentArea.setPromptText("Enter comment");
        commentArea.setLayoutX(200);
        commentArea.setLayoutY(220);
        commentArea.setPrefWidth(200);
        commentArea.setPrefHeight(100);

        // Submit Button
        Button submitButton = new Button("Submit Feedback");
        submitButton.setLayoutX(200);
        submitButton.setLayoutY(340);
        submitButton.setOnAction(e -> {
            Product selectedProduct = productComboBox.getValue();
            if (selectedProduct != null) {
                int rating = Integer.parseInt(ratingField.getText());
                String comment = commentArea.getText();
                saveFeedback(selectedProduct.getId(), rating, comment);
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Feedback submitted!");
                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Please select a product.");
                alert.showAndWait();
            }
        });

        // Back Button
        Button backButton = new Button("Back");
        backButton.setLayoutX(400);
        backButton.setLayoutY(340);
        backButton.setOnAction(e -> {
            UserModePage userModePage = new UserModePage();
            userModePage.start(primaryStage); // Navigate back to the user mode page
        });

        // Populate products into ComboBox
        populateProductComboBox(productComboBox);

        // Add elements to the layout
        root.getChildren().addAll(titleLabel, productComboBox, ratingLabel, ratingField, commentLabel, commentArea, submitButton, backButton);

        // Set the scene
        Scene scene = new Scene(root);
        primaryStage.setTitle("Leave Feedback");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void populateProductComboBox(ComboBox<Product> productComboBox) {
        String sql = "SELECT p.product_id, p.name, p.description " +
        "FROM Rentals r " +
        "JOIN Products p ON r.product_id = p.product_id " +
        "WHERE r.renter_id = ?;";


        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, loggedInUserId); // Use the hardcoded renter_id
            ResultSet rs = stmt.executeQuery();

            ObservableList<Product> products = FXCollections.observableArrayList();
            while (rs.next()) {
                int productId = rs.getInt("product_id");
                String name = rs.getString("name");
                String description = rs.getString("description");
                products.add(new Product(productId, name, description));
            }

            productComboBox.setItems(products);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveFeedback(int productId, int rating, String comment) {
        String sql = "INSERT INTO Feedback (product_id, rating, comment) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, productId);
            stmt.setInt(2, rating);
            stmt.setString(3, comment);
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
