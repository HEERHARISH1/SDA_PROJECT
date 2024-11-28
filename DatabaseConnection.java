import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    // Correct variable names
    private static final String URL = "jdbc:mysql://localhost:3306/sdaproject";
    private static final String USER = "root";
    private static final String PASSWORD = "12345678";

    public static Connection getConnection() throws SQLException {
        // Use the correct variable names
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
