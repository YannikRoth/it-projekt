package client;

import java.util.Optional;

import javafx.application.Application;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;

public class InputIpExample extends Application{

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		TextInputDialog dialog = new TextInputDialog("192.168.1.");
		dialog.setTitle("Spielpartner wählen");
		dialog.setHeaderText("Wählen Sie Ihren Spielepartner:");
		dialog.setContentText("Adresse (IP):");
		
		Optional<String> result = dialog.showAndWait();
		result.ifPresent(name -> {
			//Store in variable and use IP as Connection
			System.out.println(name);
		});
	}
}
