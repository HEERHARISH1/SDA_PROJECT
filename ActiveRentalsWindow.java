import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ActiveRentalsWindow {

    @SuppressWarnings("unchecked")
    public static void display(Stage primaryStage) {
        // Root layout
        AnchorPane root = new AnchorPane();
        root.setPrefSize(800, 600);

        // Set background using UIHelper
        UIHelper.setBackground(root, "file:src/bg.jpg");

        // Title Label
        Label titleLabel = new Label("Active Rentals");
        titleLabel.setFont(UIHelper.getTitleFont());
        titleLabel.setStyle("-fx-text-fill: #ffffff;");
        titleLabel.setLayoutX(300);
        titleLabel.setLayoutY(30);

        // TableView for Active Rentals
        TableView<Rental> table = new TableView<>();
        table.setLayoutX(50);
        table.setLayoutY(100);
        table.setPrefSize(700, 400);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Define columns
        TableColumn<Rental, Integer> idColumn = new TableColumn<>("Rental ID");
        idColumn.setCellValueFactory(cellData -> cellData.getValue().rentalIdProperty().asObject());

        TableColumn<Rental, Integer> productIdColumn = new TableColumn<>("Product ID");
        productIdColumn.setCellValueFactory(cellData -> cellData.getValue().productIdProperty().asObject());

        TableColumn<Rental, String> startDateColumn = new TableColumn<>("Start Date");
        startDateColumn.setCellValueFactory(cellData -> cellData.getValue().startDateProperty());

        TableColumn<Rental, String> endDateColumn = new TableColumn<>("End Date");
        endDateColumn.setCellValueFactory(cellData -> cellData.getValue().endDateProperty());

        TableColumn<Rental, String> statusColumn = new TableColumn<>("Status");
        statusColumn.setCellValueFactory(cellData -> cellData.getValue().statusProperty());

        table.getColumns().addAll(idColumn, productIdColumn, startDateColumn, endDateColumn, statusColumn);

        // Populate table with data from the database
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT rental_id, product_id, start_date, end_date, status FROM Rentals WHERE status = 'Pending'";
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
        }

        // Back Button
        Button backButton = UIHelper.createBackButton(primaryStage, new OwnerModePage());
        backButton.setLayoutX(350);
        backButton.setLayoutY(520);

        // Add all elements to the root layout
        root.getChildren().addAll(titleLabel, table, backButton);

        // Create the scene and show the stage
        Scene scene = new Scene(root);
        primaryStage.setTitle("Active Rentals");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
