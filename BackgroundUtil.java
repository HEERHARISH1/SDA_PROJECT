import javafx.scene.layout.*;

public class BackgroundUtil {
    public static void applyBackground(Pane pane) {
        BackgroundImage backgroundImage = new BackgroundImage(
                new javafx.scene.image.Image("file:src/bg.jpg", 600, 400, false, true),
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                BackgroundSize.DEFAULT
        );
        pane.setBackground(new Background(backgroundImage));
    }
}
