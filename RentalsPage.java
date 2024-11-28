import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class RentalsPage {

    private TableView<Rental> rentalsTable = new TableView<>(); // Table for rentals
    private TableView<Rental> detailsTable = new TableView<>(); // Table for product details

    public void start(Stage primaryStage) {
        // Root layout
        VBox root = new VBox(10);
        root.setPadding(new Insets(10));

        // Rentals Table
      //  configureRentalsTable();
        loadRentals();

        // Details Table
        configureDetailsTable();

        // Back Button
        Button backButton = new Button("Back to Rentals List");
        backButton.setDisable(true);
        backButton.setOnAction(e -> {
            rentalsTable.setVisible(true);
            detailsTable.setVisible(false);
            backButton.setDisable(true);
        });

        // Event for selecting a rental
        rentalsTable.setRowFactory(tv -> {
            TableRow<Rental> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getClickCount() == 1) {
                    Rental selectedRental = row.getItem();
                    showRentalDetails(selectedRental);
                    rentalsTable.setVisible(false);
                    detailsTable.setVisible(true);
                    backButton.setDisable(false);
                }
            });
            return row;
        });

        root.getChildren().addAll(rentalsTable, detailsTable, backButton);

        // Scene setup
        Scene scene = new Scene(root, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Rentals Page");
        primaryStage.show();
    }

    // private void configureRentalsTable() {
    //     // Columns for Rentals Table
    //     TableColumn<Rental, String> nameColumn = new TableColumn<>("Product Name");
    //     nameColumn.setCellValueFactory(cellData -> cellData.getValue().productNameProperty());

    //     TableColumn<Rental, Double> priceColumn = new TableColumn<>("Price");
    //     priceColumn.setCellValueFactory(cellData -> cellData.getValue().priceProperty().asObject());

    //     rentalsTable.getColumns().addAll(nameColumn, priceColumn);
    //     rentalsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    // }

    @SuppressWarnings("unchecked")
    private void configureDetailsTable() {
        // Columns for Details Table
        TableColumn<Rental, String> detailColumn = new TableColumn<>("Detail");
       // detailColumn.setCellValueFactory(cellData -> cellData.getValue().productNameProperty());

        TableColumn<Rental, String> valueColumn = new TableColumn<>("Value");
        valueColumn.setCellValueFactory(cellData -> cellData.getValue().statusProperty());

        detailsTable.getColumns().addAll(detailColumn, valueColumn);
        detailsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        detailsTable.setVisible(false); // Initially hidden
    }

    private void loadRentals() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT rental_id, product_id, name AS product_name, price, start_date, end_date, status FROM Rentals";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                rentalsTable.getItems().add(new Rental(
                        rs.getInt("rental_id"),
                        rs.getInt("product_id"),
                        rs.getString("start_date"),
                        rs.getString("end_date"),
                        rs.getString("status")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showRentalDetails(Rental rental) {
        detailsTable.getItems().clear();
        detailsTable.getItems().add(rental);
    }
}
