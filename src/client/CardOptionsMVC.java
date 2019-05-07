package client;

import java.util.logging.Logger;

import client.controller.CardOptionController;
import client.controller.LobbyController;
import client.model.CardOptionModel;
import client.model.LobbyModel;
import client.view.CardOptionView;
import client.view.LobbyView;
import javafx.application.Application;
import javafx.stage.Stage;
import server.ServiceLocator;

/**
 * 
 * @author Roman Leuenberger
 *
 */

public class CardOptionsMVC extends Application{
	
	private CardOptionModel model;
	private CardOptionView view;
	private CardOptionController control;
	private Logger logger = ServiceLocator.getLogger();	
	
	public static void main (String[] args) {
		launch();
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		this.model = new CardOptionModel();
		this.view = new CardOptionView(primaryStage, model);
		this.control = new CardOptionController(model, view);
		view.start();
		
		logger.info("Lobby MVC started");
	}

}
