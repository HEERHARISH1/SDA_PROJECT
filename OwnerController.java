import javafx.scene.control.Alert;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class OwnerController {

    // Method to add a product
   public static boolean addNewProduct(String name, String description, double price, String category, int ownerId) {
    String query = "INSERT INTO products (name, description, price, category, owner_id, availibility) VALUES (?, ?, ?, ?, ?, ?)";

    try (Connection connection = DatabaseConnection.getConnection();
         PreparedStatement statement = connection.prepareStatement(query)) {

        statement.setString(1, name);
        statement.setString(2, description);
        statement.setDouble(3, price);
        statement.setString(4, category);
        statement.setInt(5, ownerId); // Ensure ownerId matches a valid id in the users table
        statement.setBoolean(6, true); // Set availibility to true

        int rowsInserted = statement.executeUpdate();
        return rowsInserted > 0;
    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
}


    // Method to view active rentals
    public static void viewActiveRentals() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT * FROM Rentals WHERE status = 'Pending'";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            StringBuilder rentals = new StringBuilder("Active Rentals:\n");
            while (rs.next()) {
                rentals.append("Rental ID: ").append(rs.getInt("rental_id"))
                        .append(", Product ID: ").append(rs.getInt("product_id"))
                        .append(", Start Date: ").append(rs.getDate("start_date"))
                        .append(", End Date: ").append(rs.getDate("end_date"))
                        .append("\n");
            }

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Active Rentals");
            alert.setHeaderText(null);
            alert.setContentText(rentals.toString());
            alert.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method to view rental history
    public static void viewRentalHistory() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT * FROM Rentals WHERE status IN ('Approved', 'Returned')";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            StringBuilder history = new StringBuilder("Rental History:\n");
            while (rs.next()) {
                history.append("Rental ID: ").append(rs.getInt("rental_id"))
                        .append(", Product ID: ").append(rs.getInt("product_id"))
                        .append(", Renter ID: ").append(rs.getInt("renter_id"))
                        .append(", Status: ").append(rs.getString("status"))
                        .append("\n");
            }

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Rental History");
            alert.setHeaderText(null);
            alert.setContentText(history.toString());
            alert.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method to view feedback
    public static void viewFeedback() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT * FROM Feedback";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            StringBuilder feedback = new StringBuilder("Renter Feedback:\n");
            while (rs.next()) {
                feedback.append("Product ID: ").append(rs.getInt("product_id"))
                        .append(", Rating: ").append(rs.getInt("rating"))
                        .append(", Comment: ").append(rs.getString("comment"))
                        .append("\n");
            }

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Feedback");
            alert.setHeaderText(null);
            alert.setContentText(feedback.toString());
            alert.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
