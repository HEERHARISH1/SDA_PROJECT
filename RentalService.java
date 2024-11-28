import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RentalService {


    public boolean addRental(int productId, int userId, String startDate, String endDate) {
        String query = "INSERT INTO rentals (product_id, renter_id, start_date, end_date, status) VALUES (?, ?, ?, ?, 'Pending')";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, productId);
            statement.setInt(2, userId);
            statement.setString(3, startDate);
            statement.setString(4, endDate);

            int rowsInserted = statement.executeUpdate();
            return rowsInserted > 0; // Return true if the insertion was successful
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Rental> getRentalsByUserEmail(String email) {
        List<Rental> rentals = new ArrayList<>();
        String query = "SELECT r.rental_id, r.product_id, r.start_date, r.end_date, r.status " +
                       "FROM Rentals r " +
                       "JOIN Users u ON r.renter_id = u.id " +
                       "WHERE u.email = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Rental rental = new Rental(
                        rs.getInt("rental_id"),
                        rs.getInt("product_id"),
                        rs.getString("start_date"),
                        rs.getString("end_date"),
                        rs.getString("status")
                );

               // System.out.println(rental);
                System.out.println("\n");
                rentals.add(rental);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rentals;
    }
}