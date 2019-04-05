package client.view;

import client.model.LobbyModel;
import javafx.stage.Stage;

public class LobbyView {
	LobbyModel model;
	private Stage stage;
	
	public LobbyView(Stage primaryStage, LobbyModel model) {
		this.stage = primaryStage;
		this.model = model;
	}
}
