import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.geometry.Insets;

public class AddProductWindow {

    private static final int loggedInOwnerId = 2; // Example owner ID, replace with dynamic value

    public static void display(Stage primaryStage) {
        // Root Layout (AnchorPane for flexibility)
        AnchorPane root = new AnchorPane();
        root.setPrefSize(600, 500);

        // Set background using UIHelper
        UIHelper.setBackground(root, "file:src/bg.jpg");

        // Title Label
        Label titleLabel = UIHelper.createStyledLabel("Add New Product");
        titleLabel.setStyle("-fx-font-size: 26px; -fx-font-weight: bold; -fx-text-fill: #ffffff;");
        titleLabel.setLayoutX(220);
        titleLabel.setLayoutY(30);

        // Form Layout (GridPane)
        GridPane formGrid = new GridPane();
        formGrid.setPadding(new Insets(20));
        formGrid.setHgap(15);
        formGrid.setVgap(15);
        formGrid.setAlignment(Pos.CENTER);

        // Form Fields
        Label nameLabel = UIHelper.createStyledLabel("Product Name:");
        TextField nameField = new TextField();
        nameField.setPromptText("Enter product name");

        Label descLabel = UIHelper.createStyledLabel("Description:");
        TextField descField = new TextField();
        descField.setPromptText("Enter product description");

        Label priceLabel = UIHelper.createStyledLabel("Price:");
        TextField priceField = new TextField();
        priceField.setPromptText("Enter product price");

        Label categoryLabel = UIHelper.createStyledLabel("Category:");
        TextField categoryField = new TextField();
        categoryField.setPromptText("Enter product category");

        // Buttons
        Button submitButton = UIHelper.createStyledButton("Submit");
        Button cancelButton = UIHelper.createBackButton(primaryStage, new OwnerModePage()); // Back button

        // Add components to GridPane
        formGrid.add(nameLabel, 0, 0);
        formGrid.add(nameField, 1, 0);
        formGrid.add(descLabel, 0, 1);
        formGrid.add(descField, 1, 1);
        formGrid.add(priceLabel, 0, 2);
        formGrid.add(priceField, 1, 2);
        formGrid.add(categoryLabel, 0, 3);
        formGrid.add(categoryField, 1, 3);
        formGrid.add(submitButton, 0, 4);
        formGrid.add(cancelButton, 1, 4);

        // Position the form on the screen
        AnchorPane.setTopAnchor(formGrid, 100.0);
        AnchorPane.setLeftAnchor(formGrid, 50.0);
        AnchorPane.setRightAnchor(formGrid, 50.0);

        root.getChildren().addAll(titleLabel, formGrid);

        // Button Actions
        submitButton.setOnAction(e -> {
            String name = nameField.getText().trim();
            String description = descField.getText().trim();
            String priceText = priceField.getText().trim();
            String category = categoryField.getText().trim();

            // Validation
            if (name.isEmpty() || description.isEmpty() || priceText.isEmpty() || category.isEmpty()) {
                UIHelper.showAlert(Alert.AlertType.WARNING, "Validation Error", "Missing Fields", "Please fill out all fields.");
                return;
            }

            try {
                double price = Double.parseDouble(priceText);
                OwnerController.addNewProduct(name, description, price, category, loggedInOwnerId);

                // Success message
                
                UIHelper.showAlert(Alert.AlertType.INFORMATION, "Success", "Product Added", "The product has been successfully added.");
                OwnerModePage ownerModePage = new OwnerModePage();
                ownerModePage.start(primaryStage); // Navigate to Owner Mode Page
                
            } catch (NumberFormatException ex) {
                UIHelper.showAlert(Alert.AlertType.ERROR, "Validation Error", "Invalid Price", "Price must be a valid number.");
            } catch (Exception ex) {
                // Catch potential errors from the database or controller
                UIHelper.showAlert(Alert.AlertType.ERROR, "Error", "Unexpected Error", "Failed to add the product. Please try again.");
                ex.printStackTrace();
            }
        });

        // Create and show the scene
        Scene scene = new Scene(root);
        primaryStage.setTitle("Add Product");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
