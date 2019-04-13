package server;

import javafx.application.Application;
import javafx.stage.Stage;
import server.controller.ServerController;
import server.model.ServerModel;
import server.model.clienthandling.ServerRequestHandler;
import server.model.init.CardLoader;
import server.view.ServerView;
import java.util.logging.Logger;

/**
 * 
 * @author david
 *
 */
public class ServerMVC extends Application {
	private ServerModel model;
	private ServerView view;
	private ServerController control;
	private ServerRequestHandler requesthandler;
	private Logger logger = ServiceLocator.getLogger();

	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) {
		this.model = new ServerModel();
		
		this.requesthandler = new ServerRequestHandler(model);
		this.view = new ServerView(primaryStage, model);
		this.control= new ServerController(model, view);
		
		requesthandler.start();
		view.start();
		
		logger.info("Server MVC started");
	}
}
