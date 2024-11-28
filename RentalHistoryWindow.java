import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class RentalHistoryWindow {

    @SuppressWarnings("unchecked")
    public static void display(Stage primaryStage) {
        // Create a new stage
        Stage window = new Stage();
        window.setTitle("Rental History");

        // Root layout
        VBox root = new VBox(20);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.CENTER);

        // Apply consistent background
        UIHelper.setBackground(root, "file:src/bg.jpg");

        // Title Label
        Label titleLabel = new Label("Rental History");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #ffffff;");

        // TableView for rental history
        TableView<Rental> table = new TableView<>();

        // Define table columns
        TableColumn<Rental, Integer> rentalIdColumn = new TableColumn<>("Rental ID");
        rentalIdColumn.setCellValueFactory(cellData -> cellData.getValue().rentalIdProperty().asObject());

        TableColumn<Rental, Integer> productIdColumn = new TableColumn<>("Product ID");
        productIdColumn.setCellValueFactory(cellData -> cellData.getValue().productIdProperty().asObject());

        TableColumn<Rental, String> startDateColumn = new TableColumn<>("Start Date");
        startDateColumn.setCellValueFactory(cellData -> cellData.getValue().startDateProperty());

        TableColumn<Rental, String> endDateColumn = new TableColumn<>("End Date");
        endDateColumn.setCellValueFactory(cellData -> cellData.getValue().endDateProperty());

        TableColumn<Rental, String> statusColumn = new TableColumn<>("Status");
        statusColumn.setCellValueFactory(cellData -> cellData.getValue().statusProperty());

        // Add columns to the table
        table.getColumns().addAll(rentalIdColumn, productIdColumn, startDateColumn, endDateColumn, statusColumn);

        // Populate table with data
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT rental_id, product_id, start_date, end_date, status FROM Rentals WHERE status IN ('Approved', 'Returned')";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                table.getItems().add(new Rental(
                        rs.getInt("rental_id"),
                        rs.getInt("product_id"),
                        rs.getString("start_date"),
                        rs.getString("end_date"),
                        rs.getString("status")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
            UIHelper.showAlert(Alert.AlertType.ERROR, "Database Error", "Error loading rental history.", "Please try again later.");
        }

        // Set table width policy
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Back Button
        Button backButton = UIHelper.createBackButton(primaryStage, new OwnerModePage());
        backButton.setStyle("-fx-background-color: #ff4d4d; -fx-text-fill: white;");

        // Add elements to the root layout
        root.getChildren().addAll(titleLabel, table, backButton);

        // Set the scene
        Scene scene = new Scene(root, 800, 600);
        window.setScene(scene);
        window.show();
    }

    public void start(Stage primaryStage) {
        throw new UnsupportedOperationException("Unimplemented method 'start'");
    }
}
