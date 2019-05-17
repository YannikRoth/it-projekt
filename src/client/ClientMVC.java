package client;

import java.util.logging.Logger;

import client.controller.ClientController;
import client.controller.LobbyController;
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
	
	public ClientMVC() {
		
	}

	
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
//		if(this.callingLobbyController != null)
//			this.model = new ClientModel(this.callingLobbyController.getView().getPlayerName().getText());
//		else
//			this.model = new ClientModel();
//		ServicelocatorClient.setClientModel(model);
		this.model = ServicelocatorClient.getClientModel();
		this.view = new ClientView(primaryStage, model);
		view.start();
		ServicelocatorClient.setClientView(view);
		this.control = new ClientController(model, view);
		ServicelocatorClient.setClientController(control);
		
		
		logger.info("Client MVC started");
	}

}
