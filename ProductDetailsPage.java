import javafx.application.Application;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

public class ProductDetailsPage extends Application {

    private Product product;
    private List<Product> cart;
    public ProductDetailsPage(Product product,List<Product> cart) {
        this.product = product;
        this.cart = cart;
    }

    @Override
    public void start(Stage primaryStage) {
        // Initialize the UI elements properly by using the helper function
        VBox root = createProductDetailsPage(primaryStage, product);

        // Scene Setup
        Scene scene = new Scene(root, 600, 400);
        primaryStage.setTitle("Product Details");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // New function to create the Product Details page layout
    private VBox createProductDetailsPage(Stage primaryStage, Product product) {
        VBox root = new VBox(20);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.CENTER);  // Center the content

        // Set background using UIHelper (ensure UIHelper is available)
        UIHelper.setBackground(root, "file:src/bg.jpg");

        // Title
        Label titleLabel = new Label("Product Details");
        titleLabel.setFont(UIHelper.getTitleFont());
        titleLabel.setStyle("-fx-text-fill: #ffffff;");  // White text for contrast

        // Product Details
        Label productName = UIHelper.createStyledLabel("Name: " + product.getName());
        Label productDescription = UIHelper.createStyledLabel("Description: " + product.getDescription());
        Label productPrice = UIHelper.createStyledLabel("Price: $" + product.getPrice());

        // Fetch product owner details (name and email) from the database
        String ownerName = "";
        String ownerEmail = "";
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT u.username, u.email FROM users u JOIN products p ON u.id = p.owner_id WHERE p.product_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, product.getId());
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                ownerName = rs.getString("username");
                ownerEmail = rs.getString("email");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Owner Details
        Label ownerNameLabel = UIHelper.createStyledLabel("Owner: " + ownerName);
        Label ownerEmailLabel = UIHelper.createStyledLabel("Owner Email: " + ownerEmail);

        // Back Button
        Button backButton = UIHelper.createStyledButton("Back");
        backButton.setOnAction(e -> {
            CartPage cartPage = new CartPage(cart); // You can pass actual cart data if needed
            cartPage.start(primaryStage); // Go back to cart page
        });

        // Add all elements to the root layout
        root.getChildren().addAll(titleLabel, productName, productDescription, productPrice, ownerNameLabel, ownerEmailLabel, backButton);

        return root;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
