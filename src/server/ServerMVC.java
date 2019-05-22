package server;

import java.util.logging.Logger;

import javafx.application.Application;
import javafx.stage.Stage;
import server.controller.ServerController;
import server.model.ServerModel;
import server.view.ServerView;

/**
 * 
 * @author david
 *
 */
public class ServerMVC extends Application {
	private ServerModel model;
	private ServerView view;
	private ServerController control;
	private Logger logger = ServiceLocator.getLogger();

	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) {
		this.model = new ServerModel();
		
		//save model to service locator for other classes
		ServiceLocator.setModel(this.model);
		
		this.view = new ServerView(primaryStage, model);
		this.control= new ServerController(model, view);
		view.start();
		
		logger.info("Server MVC started");
		
	}
}
