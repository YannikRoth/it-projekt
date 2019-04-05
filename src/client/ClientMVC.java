package client;

import client.control.ClientControl;
import client.model.ClientModel;
import client.view.ClientView;
import javafx.application.Application;
import javafx.stage.Stage;

public class ClientMVC extends Application{

	private ClientModel model;
	private ClientView view;
	private ClientControl control;
	
	public static void main(String[] args) {
		System.out.println("Hello World");

	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		this.model = new ClientModel();
		this.view = new ClientView(model);
		this.control = new ClientControl(model, view);
		
	}

}
