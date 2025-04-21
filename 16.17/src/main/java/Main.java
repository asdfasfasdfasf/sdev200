import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Main extends Application {
    
    private Text text;
    private Slider redSlider, greenSlider, blueSlider, opacitySlider;
    private Label redValue, greenValue, blueValue, opacityValue;
    
    @Override
    public void start(Stage primaryStage) {
        text = new Text("Hello World");
        text.setFont(Font.font(30));
        
        HBox textContainer = new HBox(text);
        textContainer.setAlignment(Pos.CENTER);
        textContainer.setPadding(new Insets(30));
        
        redSlider = createSlider(0);
        greenSlider = createSlider(0);
        blueSlider = createSlider(0);
        opacitySlider = createSlider(100);
        
        Label redLabel = new Label("Red:");
        Label greenLabel = new Label("Green:");
        Label blueLabel = new Label("Blue:");
        Label opacityLabel = new Label("Opacity:");
        
        redValue = new Label("0");
        greenValue = new Label("0");
        blueValue = new Label("0");
        opacityValue = new Label("100");
        
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(15));
        gridPane.setHgap(15);
        gridPane.setVgap(15);
        
        addToGrid(gridPane, redLabel, redSlider, redValue, 0);
        addToGrid(gridPane, greenLabel, greenSlider, greenValue, 1);
        addToGrid(gridPane, blueLabel, blueSlider, blueValue, 2);
        addToGrid(gridPane, opacityLabel, opacitySlider, opacityValue, 3);
        
        setupSliderListeners();
        
        updateTextColor();
        
        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(textContainer);
        borderPane.setBottom(gridPane);
        
        Scene scene = new Scene(borderPane, 500, 300);
        primaryStage.setTitle("Color Selector");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    private void addToGrid(GridPane grid, Label label, Slider slider, Label value, int row) {
        grid.add(label, 0, row);
        grid.add(slider, 1, row);
        grid.add(value, 2, row);
    }
    
    private Slider createSlider(double initialValue) {
        Slider slider = new Slider(0, 100, initialValue);
        slider.setShowTickLabels(true);
        slider.setShowTickMarks(true);
        slider.setMajorTickUnit(20);
        slider.setMinorTickCount(4);
        slider.setBlockIncrement(5);
        slider.setPrefWidth(250);
        return slider;
    }
    
    private void setupSliderListeners() {
        redSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            redValue.setText(String.format("%.0f", newValue));
            updateTextColor();
        });
        
        greenSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            greenValue.setText(String.format("%.0f", newValue));
            updateTextColor();
        });
        
        blueSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            blueValue.setText(String.format("%.0f", newValue));
            updateTextColor();
        });
        
        opacitySlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            opacityValue.setText(String.format("%.0f", newValue));
            updateTextColor();
        });
    }
    
    private void updateTextColor() {
        Color color = Color.rgb(
            (int)(redSlider.getValue() / 100 * 255),
            (int)(greenSlider.getValue() / 100 * 255),
            (int)(blueSlider.getValue() / 100 * 255),
            opacitySlider.getValue() / 100
        );
        text.setFill(color);
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}
