import java.util.List;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class CheckOutPage extends Application {

    private ObservableList<Product> cart; // The cart items

    public CheckOutPage(ObservableList<Product> cart) {
        this.cart = cart;
    }

  



    @SuppressWarnings("unchecked")
    @Override
    public void start(Stage primaryStage) {
        // Create root layout
        AnchorPane root = new AnchorPane();
        root.setPrefSize(800, 600);

        // Set background using UIHelper
        UIHelper.setBackground(root, "file:src/bg.jpg");

        // Title Label
        Label titleLabel = UIHelper.createStyledLabel("Checkout");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: white;");
        titleLabel.setLayoutX(350);
        titleLabel.setLayoutY(20);

        // Table to display cart items
        TableView<Product> cartTable = new TableView<>();
        cartTable.setLayoutX(50);
        cartTable.setLayoutY(80);
        cartTable.setPrefWidth(700);
        cartTable.setPrefHeight(300);

        TableColumn<Product, String> nameColumn = new TableColumn<>("Product Name");
        nameColumn.setCellValueFactory(data -> data.getValue().getNameProperty());
        nameColumn.setPrefWidth(450);

        TableColumn<Product, Double> priceColumn = new TableColumn<>("Price");
        priceColumn.setCellValueFactory(data -> data.getValue().getPriceProperty().asObject());
        priceColumn.setPrefWidth(200);

        cartTable.getColumns().addAll(nameColumn, priceColumn);
        cartTable.setItems(cart);
        cartTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Total Price
        double total = cart.stream().mapToDouble(Product::getPrice).sum();
        Label totalLabel = UIHelper.createStyledLabel("Total: $" + total);
        totalLabel.setLayoutX(50);
        totalLabel.setLayoutY(400);

        // Payment Options
        Label paymentLabel = UIHelper.createStyledLabel("Select Payment Method:");
        paymentLabel.setLayoutX(50);
        paymentLabel.setLayoutY(450);

        ComboBox<String> paymentMethods = new ComboBox<>();
        paymentMethods.getItems().addAll("Credit Card", "PayPal", "Cash on Delivery");
        paymentMethods.setValue("Credit Card");
        paymentMethods.setLayoutX(250);
        paymentMethods.setLayoutY(450);

        // Confirm Checkout Button
        Button confirmButton = UIHelper.createStyledButton("Confirm Checkout");
        confirmButton.setLayoutX(550);
        confirmButton.setLayoutY(500);
        confirmButton.setOnAction(e -> {
            if (paymentMethods.getValue() != null) {
                // Handle successful checkout
                Alert successAlert = new Alert(Alert.AlertType.INFORMATION, "Payment method: " + paymentMethods.getValue() + "\nCheckout successful!", ButtonType.OK);
                successAlert.showAndWait();

       // Update product availability in the database
       ProductService productService = new ProductService();
       productService.markProductsAsUnavailable(cart);

       // Update product availability at runtime
       for (Product product : cart) {
           product.setAvailability(false);
       }

       
                // Clear the cart
                cart.clear();

                // Redirect to products page
                new ViewProductsPage(cart).start(primaryStage);
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Please select a payment method.", ButtonType.OK);
                alert.showAndWait();
            }
        });

        // Back Button
        Button backButton = UIHelper.createBackButton(primaryStage, new CartPage(cart));
        backButton.setLayoutX(50);
        backButton.setLayoutY(500);

        // Add all elements to the root layout
        root.getChildren().addAll(titleLabel, cartTable, totalLabel, paymentLabel, paymentMethods, confirmButton, backButton);

        // Set up the scene
        Scene scene = new Scene(root);
        primaryStage.setTitle("Checkout");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
