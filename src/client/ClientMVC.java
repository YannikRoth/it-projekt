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
	private LobbyController callingLobbyController;
	
	public ClientMVC() {
		this.callingLobbyController = null;
	}
	
	public ClientMVC(LobbyController callingLobbyController) {
		this.callingLobbyController = callingLobbyController;
	}
	
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		if(this.callingLobbyController != null)
			this.model = new ClientModel(this.callingLobbyController.getView().getPlayerName().getText());
		else
			this.model = new ClientModel();
		ServicelocatorClient.setClientModel(model);
		this.view = new ClientView(primaryStage, model);
		ServicelocatorClient.setClientView(view);
		this.control = new ClientController(model, view, this.callingLobbyController);
		ServicelocatorClient.setClientController(control);
		view.start();
		
		logger.info("Client MVC started");
	}

}
