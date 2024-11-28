import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ViewReportsPage extends Application {

    private TableView<Feedback> feedbackTable = new TableView<>();
    private ObservableList<Feedback> feedbackList = FXCollections.observableArrayList();
    private ComboBox<Integer> filterByRatingBox = new ComboBox<>();

    @Override
    public void start(Stage primaryStage) {
        // Root Layout
        VBox root = new VBox(20);
        root.setPadding(new Insets(20));

        // Apply background using UIHelper
        UIHelper.setBackground(root, "file:src/bg.jpg");

        // Title Label
        Label titleLabel = new Label("View Reports");
        titleLabel.setFont(UIHelper.getTitleFont());
        titleLabel.setStyle("-fx-text-fill: #ffffff;"); // White text for visibility

        // Feedback Table
        configureFeedbackTable();

        // Filter Box
        filterByRatingBox.setPromptText("Filter by Rating");
        filterByRatingBox.getItems().addAll(1, 2, 3, 4, 5);
        Button filterButton = UIHelper.createStyledButton("Apply Filter");
        Button clearFilterButton = UIHelper.createStyledButton("Clear Filter");

        filterButton.setOnAction(e -> applyRatingFilter());
        clearFilterButton.setOnAction(e -> {
            filterByRatingBox.setValue(null);
            fetchFeedback(); // Reload all feedback
        });

        HBox filterBox = new HBox(10, new Label("Filter Feedbacks by Rating:"), filterByRatingBox, filterButton, clearFilterButton);

        // Action Buttons
        Button refreshButton = UIHelper.createStyledButton("Refresh");
        Button backButton = UIHelper.createBackButton(primaryStage, new AdminDashboard());

        refreshButton.setOnAction(e -> fetchFeedback());

        HBox buttonBox = new HBox(20, refreshButton, backButton);

        // Add components to the root
        root.getChildren().addAll(titleLabel, filterBox, new ScrollPane(feedbackTable), buttonBox);

        // Load feedback data on startup
        fetchFeedback();

        // Scene Setup
        Scene scene = new Scene(root, 800, 600);
        primaryStage.setTitle("View Reports");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Configure Feedback Table
    @SuppressWarnings("unchecked")
    private void configureFeedbackTable() {
        TableColumn<Feedback, String> idColumn = new TableColumn<>("Feedback ID");
        idColumn.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getFeedbackId())));

        TableColumn<Feedback, String> productIdColumn = new TableColumn<>("Product ID");
        productIdColumn.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getProductId())));

        TableColumn<Feedback, String> ratingColumn = new TableColumn<>("Rating");
        ratingColumn.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getRating())));

        TableColumn<Feedback, String> commentColumn = new TableColumn<>("Comment");
        commentColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getComment()));

        feedbackTable.getColumns().addAll(idColumn, productIdColumn, ratingColumn, commentColumn);
        feedbackTable.setItems(feedbackList);
        feedbackTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    // Fetch All Feedback from the Database
    private void fetchFeedback() {
        feedbackList.clear();
        String query = "SELECT * FROM feedback";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                int feedbackId = resultSet.getInt("feedback_id");
                int productId = resultSet.getInt("product_id");
                int rating = resultSet.getInt("rating");
                String comment = resultSet.getString("comment");

                feedbackList.add(new Feedback(feedbackId, productId, rating, comment));
            }
        } catch (SQLException e) {
            UIHelper.showAlert(Alert.AlertType.ERROR, "Database Error", "Error loading feedbacks.", e.getMessage());
            e.printStackTrace();
        }
    }

    // Apply Filter by Rating
    private void applyRatingFilter() {
        Integer filterRating = filterByRatingBox.getValue();
        if (filterRating == null) {
            UIHelper.showAlert(Alert.AlertType.WARNING, "No Filter Selected", "Please select a rating to filter.", null);
            return;
        }

        feedbackList.clear();
        String query = "SELECT * FROM feedback WHERE rating = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, filterRating);

            ResultSet resultSet = statement.executeQuery();
            if (!resultSet.isBeforeFirst()) {
                UIHelper.showAlert(Alert.AlertType.INFORMATION, "No Results", "No feedback found for the selected rating.", null);
                return;
            }

            while (resultSet.next()) {
                int feedbackId = resultSet.getInt("feedback_id");
                int productId = resultSet.getInt("product_id");
                int rating = resultSet.getInt("rating");
                String comment = resultSet.getString("comment");

                feedbackList.add(new Feedback(feedbackId, productId, rating, comment));
            }
        } catch (SQLException e) {
            UIHelper.showAlert(Alert.AlertType.ERROR, "Database Error", "Error applying filter.", e.getMessage());
            e.printStackTrace();
        }
    }

    // Feedback Class
    public static class Feedback {
        private int feedbackId;
        private int productId;
        private int rating;
        private String comment;

        public Feedback(int feedbackId, int productId, int rating, String comment) {
            this.feedbackId = feedbackId;
            this.productId = productId;
            this.rating = rating;
            this.comment = comment;
        }

        public int getFeedbackId() {
            return feedbackId;
        }

        public int getProductId() {
            return productId;
        }

        public int getRating() {
            return rating;
        }

        public String getComment() {
            return comment;
        }
    }
}
