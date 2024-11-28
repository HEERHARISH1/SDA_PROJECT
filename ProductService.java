import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductService {

    // Fetch all available (active) products from the database
    public List<Product> getAllActiveProducts() {
        String query = "SELECT p.product_id, p.name, p.description, p.price, p.category, p.owner_id, u.username AS owner_name " +
                       "FROM products p " +
                       "JOIN users u ON p.owner_id = u.id " +
                       "WHERE p.availibility = 1"; // Only fetch products with availability = 1
        List<Product> products = new ArrayList<>();
    
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
    
            while (resultSet.next()) {
                int id = resultSet.getInt("product_id");
                String name = resultSet.getString("name");
                String ownerName = resultSet.getString("owner_name");
                String description = resultSet.getString("description");
                double price = resultSet.getDouble("price");
                String imageUrl = "file:product_images/" + id + ".jpg"; // Example image URL
    
                products.add(new Product(id, name, ownerName, imageUrl, description, price, true));
            }
    
        } catch (SQLException e) {
            e.printStackTrace();
        }
    
        return products;
    }
    
    public void markProductsAsUnavailable(List<Product> cart) {
        String query = "UPDATE products SET availibility = 0 WHERE product_id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            for (Product product : cart) {
                statement.setInt(1, product.getId());
                statement.addBatch();  // Add batch update for each product
            }
            statement.executeBatch(); // Execute all updates at once
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    // Fetch product details by ID
    public Product getProductById(int productId) {
        // Update query to use 'availibility' instead of 'status_active'
        String query = "SELECT p.product_id, p.name, p.description, p.price, p.category, p.owner_id, u.username AS owner_name " +
                       "FROM products p " +
                       "JOIN users u ON p.owner_id = u.id " +
                       "WHERE p.product_id = ?";  // Query to fetch product by ID and owner name
                       
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, productId);  // Set the product ID in the query
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                // Retrieve product data from the result set
                int id = resultSet.getInt("product_id");
                String name = resultSet.getString("name");
                String ownerName = resultSet.getString("owner_name");  // Get owner name from the joined users table
                String imageUrl = "no_image.jpg";  // Placeholder value for image

                String description = resultSet.getString("description");
                double price = resultSet.getDouble("price");
                int availibilityInt = resultSet.getInt("availibility");
                boolean availibility = (availibilityInt == 1);  // Convert 1 to true, 0 to false

                // Return the product with detailed information
                return new Product(id, name, ownerName, imageUrl, description, price, availibility);
            }

        } catch (SQLException e) {
            e.printStackTrace();  // Handle any SQL errors
        }

        return null;  // Return null if no product is found
    }
    
   public boolean deleteProduct(int productId) {
    String query = "DELETE FROM products WHERE product_id = ?";
    try (Connection connection = DatabaseConnection.getConnection();
         PreparedStatement statement = connection.prepareStatement(query)) {

        statement.setInt(1, productId);
        int rowsAffected = statement.executeUpdate();
        return rowsAffected > 0; // Return true if a row was deleted
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return false;
}
public boolean markProductUnavailable(int productId) {
    String query = "UPDATE products SET availibility = 0 WHERE product_id = ?";
    try (Connection connection = DatabaseConnection.getConnection();
         PreparedStatement statement = connection.prepareStatement(query)) {

        statement.setInt(1, productId);
        int rowsAffected = statement.executeUpdate();

        return rowsAffected > 0; // Return true if the update was successful
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return false;
}
public boolean markProductAvailable(int productId) {
    String query = "UPDATE products SET availibility = 1 WHERE product_id = ?";
    try (Connection connection = DatabaseConnection.getConnection();
         PreparedStatement statement = connection.prepareStatement(query)) {

        statement.setInt(1, productId);
        int rowsAffected = statement.executeUpdate();

        return rowsAffected > 0;
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return false;
}

    // Mark a product as unavailable (used when confirming rental)
   // Mark a product as rented in the database
   public boolean markProductAsRented(int productId) {
    String updateQuery = "UPDATE products SET availability = false WHERE id = ?";
    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(updateQuery)) {

        stmt.setInt(1, productId);
        int rowsUpdated = stmt.executeUpdate();
        return rowsUpdated > 0;
    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
}

// Add the rental to the rentals table
public boolean addRental(int productId, int userId, String startDate, String endDate) {
    String insertQuery = "INSERT INTO rentals (product_id, user_id, start_date, end_date, status) VALUES (?, ?, ?, ?, 'Pending')";
    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(insertQuery)) {

        stmt.setInt(1, productId);
        stmt.setInt(2, userId);
        stmt.setString(3, startDate);
        stmt.setString(4, endDate);

        int rowsInserted = stmt.executeUpdate();
        return rowsInserted > 0;
    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
}
    
    
}
