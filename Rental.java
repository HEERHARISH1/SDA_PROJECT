import javafx.beans.property.*;

public class Rental {
    private final IntegerProperty rentalId;
    private final IntegerProperty productId;
    private final StringProperty startDate;
    private final StringProperty endDate;
    private final StringProperty status;

    public Rental(int rentalId, int productId, String startDate, String endDate, String status) {
        this.rentalId = new SimpleIntegerProperty(rentalId);
        this.productId = new SimpleIntegerProperty(productId);
        this.startDate = new SimpleStringProperty(startDate);
        this.endDate = new SimpleStringProperty(endDate);
        this.status = new SimpleStringProperty(status);
    }

    // Getters and setters...

    @Override
    public String toString() {
        return "Rental{" +
                "rentalId=" + rentalId.get() +
                ", productId=" + productId.get() +
                ", startDate='" + startDate.get() + '\'' +
                ", endDate='" + endDate.get() + '\'' +
                ", status='" + status.get() + '\'' +
                '}';
    }


    // Rental ID
    public int getRentalId() {
        return rentalId.get();
    }

    public void setRentalId(int rentalId) {
        this.rentalId.set(rentalId);
    }

    public IntegerProperty rentalIdProperty() {
        return rentalId;
    }

    // Product ID
    public int getProductId() {
        return productId.get();
    }

    public void setProductId(int productId) {
        this.productId.set(productId);
    }

    public IntegerProperty productIdProperty() {
        return productId;
    }

    // Start Date
    public String getStartDate() {
        return startDate.get();
    }

    public void setStartDate(String startDate) {
        this.startDate.set(startDate);
    }

    public StringProperty startDateProperty() {
        return startDate;
    }

    // End Date
    public String getEndDate() {
        return endDate.get();
    }

    public void setEndDate(String endDate) {
        this.endDate.set(endDate);
    }

    public StringProperty endDateProperty() {
        return endDate;
    }

    // Status
    public String getStatus() {
        return status.get();
    }

    public void setStatus(String status) {
        this.status.set(status);
    }

    public StringProperty statusProperty() {
        return status;
    }
}
