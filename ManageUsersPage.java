import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ManageUsersPage extends Application {

    private TableView<User> userTable = new TableView<>();
    private ObservableList<User> userList = FXCollections.observableArrayList();

    
    public void start(Stage primaryStage) {
        VBox root = new VBox(15); // Spacing between elements
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.CENTER); // Center-align elements

        // Set background
        UIHelper.setBackground(root, "file:src/bg.jpg");

        // Title Label
        Label titleLabel = new Label("Manage Users");
        titleLabel.setFont(UIHelper.getTitleFont());
        titleLabel.setStyle("-fx-text-fill: #ffffff;"); // White text color

        // Set up the user table
        TableColumn<User, String> usernameColumn = new TableColumn<>("Username");
        usernameColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getUsername()));

        TableColumn<User, String> emailColumn = new TableColumn<>("Email");
        emailColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getEmail()));

        TableColumn<User, String> roleColumn = new TableColumn<>("Role");
        roleColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getRole()));

        userTable.getColumns().addAll(usernameColumn, emailColumn, roleColumn);
        userTable.setItems(userList);
        userTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY); // Auto-resize columns

        // Buttons
        Button refreshButton = UIHelper.createStyledButton("Refresh");
        Button deleteButton = UIHelper.createStyledButton("Delete User");
        Button backButton = UIHelper.createBackButton(primaryStage, new AdminDashboard());

        // Button Actions
        refreshButton.setOnAction(e -> fetchUsers());
        deleteButton.setOnAction(e -> deleteUser());
        backButton.setOnAction(e -> {
            AdminDashboard adminDashboard = new AdminDashboard();
            adminDashboard.start(primaryStage);
        });

        // Add elements to root layout
        root.getChildren().addAll(titleLabel, userTable, refreshButton, deleteButton, backButton);

        // Load user data on startup
        fetchUsers();

        // Create and set the scene
        Scene scene = new Scene(root, 800, 600);
        primaryStage.setTitle("Manage Users");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Fetch users from the database
    private void fetchUsers() {
        userList.clear();
        String query = "SELECT id, username, email, role FROM users";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String username = resultSet.getString("username");
                String email = resultSet.getString("email");
                String role = resultSet.getString("role");
                userList.add(new User(id, username, email, role));
            }
        } catch (SQLException e) {
            UIHelper.showAlert(Alert.AlertType.ERROR, "Error", "Database Error", "Failed to fetch users.");
            e.printStackTrace();
        }
    }

    // Delete selected user
    private void deleteUser() {
        User selectedUser = userTable.getSelectionModel().getSelectedItem();
        if (selectedUser == null) {
            UIHelper.showAlert(Alert.AlertType.WARNING, "No User Selected", "Action Required", "Please select a user to delete.");
            return;
        }

        if ("ADMIN".equals(selectedUser.getRole())) {
            UIHelper.showAlert(Alert.AlertType.ERROR, "Action Denied", "Operation Failed", "Cannot delete a user with ADMIN role.");
            return;
        }

        String query = "DELETE FROM users WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, selectedUser.getId());
            statement.executeUpdate();
            userList.remove(selectedUser);
            UIHelper.showAlert(Alert.AlertType.INFORMATION, "Success", "User Deleted", "The selected user was deleted successfully.");
        } catch (SQLException e) {
            UIHelper.showAlert(Alert.AlertType.ERROR, "Error", "Database Error", "Failed to delete user.");
            e.printStackTrace();
        }
    }

    // User class to hold table data
    public static class User {
        private int id;
        private String username;
        private String email;
        private String role;

        public User(int id, String username, String email, String role) {
            this.id = id;
            this.username = username;
            this.email = email;
            this.role = role;
        }

        public int getId() {
            return id;
        }

        public String getUsername() {
            return username;
        }

        public String getEmail() {
            return email;
        }

        public String getRole() {
            return role;
        }
    }
}
