import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.DoubleProperty;

public class Product {
    private int id;
    private String name;
    private String ownerName;
    private String imageUrl;
    private String description;
    private double price;
    private boolean availability;

    // Constructor
    public Product(int id, String name, String ownerName, String imageUrl, String description, double price, boolean availability) {
        this.id = id;
        this.name = name;
        this.ownerName = ownerName;
        this.imageUrl = imageUrl;
        this.description = description;
        this.price = price;
        this.availability = availability;
    }
    public Product(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }
    

    // Getters and setters


  

    public StringProperty getOwnerNameProperty() {
        return new SimpleStringProperty(ownerName);
    }

  
    // Getters and Setters
 // JavaFX properties for table binding
    public StringProperty getNameProperty() {
        return new SimpleStringProperty(name);
    }

    public DoubleProperty getPriceProperty() {
        return new SimpleDoubleProperty(price);
    }

    // Getter methods
   
    public String getOwnerName() { return ownerName; }
   
 
    


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public boolean isAvailability() {
        return availability;
    }

    public void setAvailability(boolean availability) {
        this.availability = availability;
    }
}
