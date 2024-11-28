import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.List;

public class ViewActiveProductsPage_u extends Application {

    private List<Product> products;

    public ViewActiveProductsPage_u(List<Product> products) {
        this.products = products;
    }

    @Override
    public void start(Stage primaryStage) {
        // Root Layout
        VBox root = new VBox(20);
        root.setPadding(new Insets(20));

        // Apply Background
        UIHelper.setBackground(root, "file:src/bg.jpg");

        // Title
        Label titleLabel = new Label("Active Products");
        titleLabel.setFont(UIHelper.getTitleFont());
        titleLabel.setStyle("-fx-text-fill: #ffffff;"); // White text for contrast
        root.getChildren().add(titleLabel);

        // Product Table
        TableView<Product> table = new TableView<>();
        configureTable(table);
        table.getItems().addAll(products);

        // Add row selection event
        table.setOnMouseClicked(event -> {
            Product selectedProduct = table.getSelectionModel().getSelectedItem();
            if (selectedProduct != null) {
                showProductDetails(primaryStage, selectedProduct);
            }
        });

        root.getChildren().add(table);

        

        // Back Button
        Button backButton = UIHelper.createBackButton(primaryStage, new UserModePage("user@example.com"));
        root.getChildren().add(backButton);

        // Scene and Stage
        Scene scene = new Scene(root, 800, 600);
        primaryStage.setTitle("Active Products");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Configure Product Table
    @SuppressWarnings("unchecked")
    private void configureTable(TableView<Product> table) {
        TableColumn<Product, String> nameColumn = new TableColumn<>("Product Name");
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().getNameProperty());

        TableColumn<Product, Double> priceColumn = new TableColumn<>("Price");
        priceColumn.setCellValueFactory(cellData -> cellData.getValue().getPriceProperty().asObject());

        table.getColumns().addAll(nameColumn, priceColumn);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }



    
    // private void showProductDetails(Stage primaryStage, Product product) {
    //     // Open the Product Details Page
    //     ProductDetailsPage productDetailsPage = new ProductDetailsPage(product);
    //     productDetailsPage.start(primaryStage);  // Launch product details page with the selected product
    // }
    
    // Show Product Details
    private void showProductDetails(Stage primaryStage, Product product) {
        VBox detailsLayout = new VBox(20);
        detailsLayout.setPadding(new Insets(20));
        detailsLayout.setSpacing(15);

        // Apply Background
        UIHelper.setBackground(detailsLayout, "file:src/bg.jpg");

        // Title
        Label titleLabel = new Label("Product Details");
        titleLabel.setFont(UIHelper.getTitleFont());
        titleLabel.setStyle("-fx-text-fill: #ffffff;"); // White text for contrast
        detailsLayout.getChildren().add(titleLabel);

        // Product Information
        detailsLayout.getChildren().add(UIHelper.createStyledLabel("Name: " + product.getName()));
        detailsLayout.getChildren().add(UIHelper.createStyledLabel("Owner: " + product.getOwnerName()));
        detailsLayout.getChildren().add(UIHelper.createStyledLabel("Description: " + product.getDescription()));
        detailsLayout.getChildren().add(UIHelper.createStyledLabel("Price: $" + product.getPrice()));

        // Buttons
        Button backButton = UIHelper.createStyledButton("Back");
        backButton.setOnAction(e -> start(primaryStage)); // Go back to the main page

        Button rentButton = UIHelper.createStyledButton("Rent Item");
        rentButton.setOnAction(e -> handleRentProduct(primaryStage, product));

        detailsLayout.getChildren().addAll(rentButton, backButton);

        // Scene and Stage
        Scene scene = new Scene(detailsLayout, 800, 600);
        primaryStage.setTitle("Product Details");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Handle Rent Product Action
    private void handleRentProduct(Stage primaryStage, Product product) {
        ProductService productService = new ProductService();
        boolean rented = productService.markProductAsRented(product.getId()); // Mark product as rented
        if (rented) {
            UIHelper.showAlert(Alert.AlertType.INFORMATION, "Success", "Rent Confirmed", "The product has been rented successfully.");
            start(primaryStage); // Reload the active products page
        } else {
            UIHelper.showAlert(Alert.AlertType.ERROR, "Rent Failed", null, "Failed to rent the product. Please try again.");
        }
    }
    

    public static void main(String[] args) {
        launch(args);
    }
}
