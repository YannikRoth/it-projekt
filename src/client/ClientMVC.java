package client;

import client.controller.ClientController;
import client.model.ClientModel;
import client.view.ClientView;
import javafx.application.Application;
import javafx.stage.Stage;

public class ClientMVC extends Application{

	private ClientModel model;
	private ClientView view;
	private ClientController control;
	
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		this.model = new ClientModel();
		this.view = new ClientView(primaryStage, model);
		this.control = new ClientController(model, view);
		view.start(primaryStage);
	}

}
