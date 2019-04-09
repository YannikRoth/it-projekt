package client;

import java.util.logging.Logger;

import client.controller.ClientController;
import client.model.ClientModel;
import client.view.ClientView;
import javafx.application.Application;
import javafx.stage.Stage;
import server.ServiceLocator;

/**
 * 
 * @author philipp
 *
 */

public class ClientMVC extends Application{

	private ClientModel model;
	private ClientView view;
	private ClientController control;
	private Logger logger = ServiceLocator.getLogger();
	
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		this.model = new ClientModel();
		this.view = new ClientView(primaryStage, model);
		this.control = new ClientController(model, view);
		view.start(primaryStage);
		
		logger.info("Client MVC started");
	}

}
