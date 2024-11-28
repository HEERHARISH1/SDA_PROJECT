import javafx.application.Application;


public class Main {
    public static void main(String[] args) {
        System.out.println("Starting Borrow Box Application...");

        try {
            if (DatabaseConnection.getConnection() != null) {
                System.out.println("Database connected successfully!");
            }
        } catch (Exception e) {
            System.out.println("Database connection failed!");
            e.printStackTrace();
        }
        Application.launch(LoginPage.class, args);
    }
}
