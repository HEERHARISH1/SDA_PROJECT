import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class ViewProductsPage extends Application {

    private List<Product> cart; // Cart to store selected products
    private Button cartButton = new Button(); // Button to display cart size
    private int loggedInUserId;
    // No-argument constructor (required for JavaFX)
    public ViewProductsPage() {
        this.cart = new ArrayList<>(); // Initialize an empty cart
    }

    // Constructor to accept cart as a parameter
    public ViewProductsPage(List<Product> cart) {
        this.cart = cart; // Use the passed cart
    }
    @SuppressWarnings("unused")
    private void handleRentProduct(Stage primaryStage, Product product) {
        ProductService productService = new ProductService();
        boolean rented = productService.markProductAsRented(product.getId()); // Mark product as rented
    
        if (rented) {
            // Add rental entry
            boolean rentalAdded = productService.addRental(product.getId(), loggedInUserId, "2024-11-23", "2024-11-30"); // Example dates
            if (rentalAdded) {
                UIHelper.showAlert(Alert.AlertType.INFORMATION, "Success", "Rental Successful", "Product rented successfully!");
                cart.remove(product); // Remove from cart if it was there
                start(primaryStage); // Refresh page
            } else {
                UIHelper.showAlert(Alert.AlertType.ERROR, "Error", "Rental Failed", "Failed to add rental details.");
            }
        } else {
            UIHelper.showAlert(Alert.AlertType.ERROR, "Error", "Rental Failed", "Product is no longer available.");
        }
    }
    
    @Override
    public void start(Stage primaryStage) {
        // Ensure cart is initialized
        if (cart == null) {
            cart = new ArrayList<>();
        }

        // Root Layout (VBox with spacing)
        VBox root = new VBox(20);
        root.setPadding(new Insets(20));

        // Apply background using UIHelper
        UIHelper.setBackground(root, "file:src/bg.jpg");

        // Title Label
        Label titleLabel = new Label("Available Products");
        titleLabel.setFont(UIHelper.getTitleFont());
        titleLabel.setStyle("-fx-text-fill: white;"); // White text for better visibility
        root.getChildren().add(titleLabel);

        // Product List GridPane
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20));
        grid.setHgap(15);
        grid.setVgap(20);

        // Fetch products from the service
        ProductService productService = new ProductService();
        List<Product> products = productService.getAllActiveProducts();

        int row = 0; // Row tracker for the grid layout
        for (Product product : products) {
            // Product Image
            ImageView productImage = new ImageView();
            try {
                productImage.setImage(new Image(product.getImageUrl(), 100, 100, false, true));
            } catch (IllegalArgumentException e) {
                UIHelper.showAlert(Alert.AlertType.ERROR, "Image Error", null, "Image not found for product: " + product.getName());
                productImage.setImage(new Image("file:src/default_image.png", 100, 100, false, true)); // Fallback image
            }

            // Product Details
            Label productName = UIHelper.createStyledLabel("Name: " + product.getName());
            Label productPrice = UIHelper.createStyledLabel("Price: $" + product.getPrice());

            // Add to Cart Button
            Button addToCartButton = UIHelper.createStyledButton("Add to Cart");
            addToCartButton.setOnAction(e -> {
                if (!cart.contains(product)) {
                    cart.add(product); // Add product to cart
                    updateCartButton();
                    UIHelper.showAlert(Alert.AlertType.INFORMATION, "Success", null, product.getName() + " added to cart.");
                } else {
                    UIHelper.showAlert(Alert.AlertType.WARNING, "Already Added", null, product.getName() + " is already in the cart.");
                }
            });

            // Add components to the grid row
            grid.add(productImage, 0, row);
            grid.add(productName, 1, row);
            grid.add(productPrice, 2, row);
            grid.add(addToCartButton, 3, row);
            row++;
        }

        // Wrap GridPane in a ScrollPane
        ScrollPane scrollPane = new ScrollPane(grid);
        scrollPane.setFitToWidth(true);
        root.getChildren().add(scrollPane);

        // Cart Button (Navigate to Cart Page)
        cartButton.setText("Cart (" + cart.size() + ")");
        cartButton.setFont(UIHelper.getLabelFont());
        cartButton.setOnAction(e -> {
            CartPage cartPage = new CartPage(cart); // Pass the cart to CartPage
            cartPage.start(primaryStage);
        });

        // Back Button
        Button backButton = UIHelper.createBackButton(primaryStage, new UserModePage("'email'"));

        // Add buttons at the bottom
        HBox buttonBox = new HBox(20, backButton, cartButton);
        root.getChildren().add(buttonBox);

        // Scene and Stage Setup
        Scene scene = new Scene(root, 800, 600);
        primaryStage.setTitle("View Products");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Update the cart button text dynamically
    private void updateCartButton() {
        cartButton.setText("Cart (" + cart.size() + ")");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
