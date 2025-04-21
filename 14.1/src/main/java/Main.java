import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class Main extends Application {
    private static final double IMAGE_HEIGHT = 100;
    private static final double IMAGE_WIDTH = 150;
    private static final double GRID_GAP = 5;
    
    @Override
    public void start(Stage primaryStage) {
        GridPane pane = new GridPane();
        pane.setHgap(GRID_GAP);
        pane.setVgap(GRID_GAP);
        
        ImageView flag1 = createImageView("/flag1.gif");
        ImageView flag2 = createImageView("/flag2.gif");
        ImageView flag6 = createImageView("/flag6.gif");
        ImageView flag7 = createImageView("/flag7.gif");
        
        pane.add(flag1, 0, 0);
        pane.add(flag2, 1, 0);
        pane.add(flag6, 0, 1);
        pane.add(flag7, 1, 1);
        
        Scene scene = new Scene(pane);
        primaryStage.setTitle("Flag Display");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private ImageView createImageView(String filename) {
        Image image = new Image(getClass().getResourceAsStream(filename));
        ImageView imageView = new ImageView(image);

        imageView.setFitHeight(IMAGE_HEIGHT);
        imageView.setFitWidth(IMAGE_WIDTH);
        imageView.setPreserveRatio(true);
        
        return imageView;
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}
