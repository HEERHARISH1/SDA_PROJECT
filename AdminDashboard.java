import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class AdminDashboard extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Create root layout
        AnchorPane root = new AnchorPane();
        root.setPrefSize(800, 600);

        // Set background using UIHelper
        UIHelper.setBackground(root, "file:src/bg.jpg");

        // Title Label
        Label titleLabel = UIHelper.createStyledLabel("Admin Dashboard");
        titleLabel.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: white;");
        titleLabel.setLayoutX(300); // Center horizontally
        titleLabel.setLayoutY(50); // Top margin

        // Buttons
        Button manageUsersButton = UIHelper.createStyledButton("Manage Users");
        manageUsersButton.setLayoutX(300);
        manageUsersButton.setLayoutY(150);

        Button manageProductsButton = UIHelper.createStyledButton("Manage Products");
        manageProductsButton.setLayoutX(300);
        manageProductsButton.setLayoutY(210);

        Button viewReportsButton = UIHelper.createStyledButton("View Reports");
        viewReportsButton.setLayoutX(300);
        viewReportsButton.setLayoutY(270);

        Button logoutButton = UIHelper.createStyledButton("Logout");
        logoutButton.setStyle("-fx-background-color: #d9534f; -fx-text-fill: white;");
        logoutButton.setLayoutX(300);
        logoutButton.setLayoutY(330);

        // Button Actions
        manageUsersButton.setOnAction(e -> {
            ManageUsersPage manageUsersPage = new ManageUsersPage();
            manageUsersPage.start(primaryStage);
        });

        manageProductsButton.setOnAction(e -> {
            ManageProductsPage manageProductsPage = new ManageProductsPage();
            manageProductsPage.start(primaryStage);
        });

        viewReportsButton.setOnAction(e -> {
            ViewReportsPage viewReportsPage = new ViewReportsPage();
            viewReportsPage.start(primaryStage);
        });

        logoutButton.setOnAction(e -> {
            LoginPage loginPage = new LoginPage();
            loginPage.start(primaryStage);
        });

        // Add elements to root
        root.getChildren().addAll(titleLabel, manageUsersButton, manageProductsButton, viewReportsButton, logoutButton);

        // Set up the scene
        Scene scene = new Scene(root);
        primaryStage.setTitle("Admin Dashboard");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
