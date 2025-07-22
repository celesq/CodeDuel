import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

public class Main extends Application {
	
	private static final int ROWS = 10;
	private static final int COLUMNS = 10;
	private static final int SIZE = 50;
	
	private Canvas canvas = new Canvas(500, 500);
	private GraphicsContext gc = canvas.getGraphicsContext2D();
	
	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage stage) throws Exception {
		
		stage.setTitle("Hello World");
		Button button = new Button("Click Me");
		Button button2 = new Button("Arrange Table");
		VBox root = new VBox();
		root.getChildren().addAll(button, button2);
		root.setAlignment(Pos.TOP_RIGHT);
		
		StackPane layout = new StackPane();
		
		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < COLUMNS; j++) {
				gc.setStroke(Color.GRAY);
				gc.strokeRect(i * SIZE, j * SIZE, SIZE, SIZE);
				gc.setFill(Color.SADDLEBROWN);
				gc.fillRect(i * SIZE, j * SIZE, SIZE, SIZE);
			}
		}
		
		layout.getChildren().add(canvas);
		layout.getChildren().add(root);
		
		Scene scene = new Scene(layout, 1920, 1080);
		stage.setScene(scene);
		
		stage.show();
	
	}
}