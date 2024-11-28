public class Admin {
    private static Admin instance;
    private String username;
    private String password;

    // Private constructor to prevent instantiation
    private Admin(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Public method to provide the single instance
    public static Admin getInstance(String username, String password) {
        if (instance == null) {
            instance = new Admin(username, password);
        }
        return instance;
    }

    // Getters for admin details
    public String getUsername() {
        return username;
    }

    public boolean validatePassword(String inputPassword) {
        return this.password.equals(inputPassword);
    }
}
