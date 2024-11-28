import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.util.List;

public class CartPage extends Application {

    private Stage primaryStage;
    private List<Product> cart;

    public CartPage(List<Product> cart) {
        this.cart = cart;
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;

        // Root Layout
        AnchorPane root = new AnchorPane();
        root.setPrefSize(800, 600);

        // Title Label
        Label titleLabel = new Label("Your Cart");
        titleLabel.setFont(UIHelper.getTitleFont());
        titleLabel.setLayoutX(350);
        titleLabel.setLayoutY(20);

        // Cart Table
        TableView<Product> cartTable = new TableView<>();
        cartTable.setItems(FXCollections.observableArrayList(cart));
        cartTable.setLayoutX(50);
        cartTable.setLayoutY(60);
        cartTable.setPrefSize(700, 400);

        // Add columns to the cartTable
        TableColumn<Product, String> nameColumn = new TableColumn<>("Product Name");
        nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        TableColumn<Product, Double> priceColumn = new TableColumn<>("Price");
        priceColumn.setCellValueFactory(cellData -> cellData.getValue().getPriceProperty().asObject());

        cartTable.getColumns().addAll(nameColumn, priceColumn);

        // Make product names clickable as hyperlinks
        nameColumn.setCellFactory(col -> {
            TableCell<Product, String> cell = new TableCell<>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        Hyperlink hyperlink = new Hyperlink(item);
                        hyperlink.setOnAction(e -> {
                            Product selectedProduct = getTableView().getItems().get(getIndex());
                            showProductDetails(primaryStage, selectedProduct);  // Navigate to product details page
                        });
                        setGraphic(hyperlink);  // Set the hyperlink in the table cell
                    }
                }
            };
            return cell;
        });

        // Back Button
        Button backButton = new Button("Back");
        backButton.setLayoutX(50);
        backButton.setLayoutY(500);
        backButton.setOnAction(e -> {
            new UserModePage().start(primaryStage);
        });

        // Checkout Button
        Button checkoutButton = new Button("Checkout");
        checkoutButton.setLayoutX(150);
        checkoutButton.setLayoutY(500);
        checkoutButton.setOnAction(e -> handleCheckout());

        // Add elements to root
        root.getChildren().addAll(titleLabel, cartTable, backButton, checkoutButton);

        // Set up the scene
        Scene scene = new Scene(root);
        primaryStage.setTitle("Cart");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Show Product Details Page when a product is clicked
    private void showProductDetails(Stage primaryStage, Product product) {
        ProductDetailsPage detailsPage = new ProductDetailsPage(product, cart);
        detailsPage.start(primaryStage);
    }

    // Handle checkout process
    private void handleCheckout() {
        RentalService rentalService = new RentalService();

        for (Product product : cart) {
            boolean rentalAdded = rentalService.addRental(product.getId(), getLoggedInUserId(), "2024-11-01", "2024-11-07");

            if (rentalAdded) {
                ProductService productService = new ProductService();
                productService.markProductUnavailable(product.getId());
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to rent the product: " + product.getName());
                alert.showAndWait();
                return;
            }
        }

        cart.clear();
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Checkout successful! Products have been rented.");
        alert.showAndWait();
        new UserModePage().start(primaryStage);
    }

    private int getLoggedInUserId() {
        return 1; // Replace this with actual logic to fetch the logged-in user's ID
    }

    public static void main(String[] args) {
        launch(args);
    }
}
