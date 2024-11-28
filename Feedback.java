import javafx.beans.property.*;

public class Feedback {
    private final IntegerProperty productId;
    private final IntegerProperty rating;
    private final StringProperty comment;

    public Feedback(int productId, int rating, String comment) {
        this.productId = new SimpleIntegerProperty(productId);
        this.rating = new SimpleIntegerProperty(rating);
        this.comment = new SimpleStringProperty(comment);
    }

    // Getter and Setter for Product ID
    public int getProductId() {
        return productId.get();
    }

    public void setProductId(int productId) {
        this.productId.set(productId);
    }

    public IntegerProperty productIdProperty() {
        return productId;
    }

    // Getter and Setter for Rating
    public int getRating() {
        return rating.get();
    }

    public void setRating(int rating) {
        this.rating.set(rating);
    }

    public IntegerProperty ratingProperty() {
        return rating;
    }

    // Getter and Setter for Comment
    public String getComment() {
        return comment.get();
    }

    public void setComment(String comment) {
        this.comment.set(comment);
    }

    public StringProperty commentProperty() {
        return comment;
    }
}
