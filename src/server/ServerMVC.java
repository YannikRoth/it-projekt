package server;

import java.util.logging.Logger;

import javafx.application.Application;
import javafx.stage.Stage;
import server.control.ServerControl;
import server.model.ServerModel;
import server.view.ServerView;

public class ServerMVC extends Application{

	private ServerModel model;
	private ServerView view;
	private ServerControl control;
	private Logger logger = ServiceLocator.getLogger();
	
	public static void main(String[] args) {
		launch(args);

	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		this.model = new ServerModel();
		this.view = new ServerView(model);
		this.control = new ServerControl(model, view);
		
		logger.info("MVC started");
		
	}

}
