package client;

import client.controller.LobbyController;
import client.model.LobbyModel;
import client.view.LobbyView;
import javafx.application.Application;
import javafx.stage.Stage;

public class LobbyMVC extends Application{

	private LobbyModel model;
	private LobbyView view;
	private LobbyController control;
	
	public static void main(String[] args) {
		System.out.println("Hello World");

	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		this.model = new LobbyModel();
		this.view = new LobbyView(primaryStage, model);
		this.control = new LobbyController(model, view);
		
	}

}
