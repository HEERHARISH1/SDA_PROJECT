import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;

public class ManageProductsPage extends Application {

    private TableView<Product> productTable = new TableView<>();
    private ObservableList<Product> productList = FXCollections.observableArrayList();

    @SuppressWarnings("unchecked")
    @Override
    public void start(Stage primaryStage) {
        VBox root = new VBox(15);
        root.setPrefSize(800, 600);

        // Title Label
        Label titleLabel = new Label("Manage Products");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        // Set up the product table
        TableColumn<Product, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));

        TableColumn<Product, String> descriptionColumn = new TableColumn<>("Description");
        descriptionColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDescription()));

        TableColumn<Product, String> priceColumn = new TableColumn<>("Price");
        priceColumn.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getPrice())));

        TableColumn<Product, String> availabilityColumn = new TableColumn<>("Availability");
        availabilityColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().isAvailability() ? "Available" : "Unavailable"));

        TableColumn<Product, String> ownerColumn = new TableColumn<>("Owner");
        ownerColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getImageUrl())); // Using ownerName field

        productTable.getColumns().addAll(nameColumn, descriptionColumn, priceColumn, availabilityColumn, ownerColumn);
        productTable.setItems(productList);

        // Buttons
        Button refreshButton = new Button("Refresh");
        Button deleteButton = new Button("Delete Product");
        Button backButton = new Button("Back");

        // Button Actions
        refreshButton.setOnAction(e -> fetchProducts());
        deleteButton.setOnAction(e -> deleteProduct());
        backButton.setOnAction(e -> {
            AdminDashboard adminDashboard = new AdminDashboard();
            adminDashboard.start(primaryStage);
        });

        root.getChildren().addAll(titleLabel, productTable, refreshButton, deleteButton, backButton);

        // Fetch products on page load
        fetchProducts();

        Scene scene = new Scene(root);
        primaryStage.setTitle("Manage Products");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Fetch products from the ProductService
    private void fetchProducts() {
        productList.clear();
        ProductService productService = new ProductService();
        List<Product> products = productService.getAllActiveProducts();
        productList.addAll(products); // Populate the TableView
    }

    // Delete selected product
    private void deleteProduct() {
        Product selectedProduct = productTable.getSelectionModel().getSelectedItem();
        if (selectedProduct == null) {
            UIHelper.showAlert(Alert.AlertType.WARNING, "Error", "No Product Selected", "Please select a product to delete.");
            return;
        }

        ProductService productService = new ProductService();
        boolean success = productService.deleteProduct(selectedProduct.getId());

        if (success) {
            productList.remove(selectedProduct); // Remove product from the table
            UIHelper.showAlert(Alert.AlertType.INFORMATION, "Success", "Product Deleted", "The product was deleted successfully.");
        } else {
            UIHelper.showAlert(Alert.AlertType.ERROR, "Error", "Deletion Failed", "Failed to delete the product. Please try again.");
        }
    }
}
