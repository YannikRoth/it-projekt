package client;

import java.util.logging.Logger;

import client.controller.LobbyController;
import client.model.ClientModel;
import client.model.LobbyModel;
import client.view.LobbyView;
import javafx.application.Application;
import javafx.stage.Stage;
import server.ServiceLocator;

/**
 * 
 * @author philipp
 *
 */

public class LobbyMVC extends Application{

	private LobbyModel model;
	private LobbyView view;
	private LobbyController control;
	private Logger logger = ServiceLocator.getLogger();
	
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {

	    ServicelocatorClient.setClientModel(new ClientModel());
	    
	    
		this.model = new LobbyModel();
		ServicelocatorClient.setLobbyModel(model);
		this.view = new LobbyView(primaryStage, model);
		this.control = new LobbyController(model, view);
		ServicelocatorClient.setLobbyController(control);
		view.start();
		
		logger.info("Lobby MVC started");
	}

}
