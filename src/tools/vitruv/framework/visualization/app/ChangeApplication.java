package tools.vitruv.framework.visualization.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ChangeApplication extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Visualization");
		Parent root = FXMLLoader.load(getClass().getResource("/view.fxml"));
		
        Scene scene = new Scene(root, 1800, 750);
        primaryStage.setScene(scene);
        primaryStage.setResizable(true);
        primaryStage.show();
	}
}
