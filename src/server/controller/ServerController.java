package server.controller;

import java.util.logging.Logger;

import globals.Translator;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.WindowEvent;
import server.ServiceLocator;
import server.model.ServerModel;
import server.view.ServerView;

public class ServerController{
	private ServerModel model;
	private ServerView view;
	private Translator translator = Translator.getTranslator();
	private Logger logger = ServiceLocator.getLogger(); 

	public ServerController(ServerModel model, ServerView view) {
		this.model = model;
		this.view = view;
		
		AddViewButtonListeners();
		AddMenuItemListeners();
		handleCloseRequest();
	}

	/**
	 * Add listeners to the buttons in view
	 * @author david
	 * 
	 */
	private void AddViewButtonListeners() {
		view.getButtonLeaderboard().setOnAction((e) -> {
			Alert dialog = new Alert(AlertType.INFORMATION);
			dialog.setTitle(Translator.getTranslator().getString("button.leaderboard"));
			dialog.setHeaderText(Translator.getTranslator().getString("message.leaderboard"));
			dialog.setContentText("1...\r\n2...\r\n3...");
			dialog.showAndWait();
		});
	}
	
	/**
	 * Add listeners to menu and menuitems 
	 * @author david
	 */
	private void AddMenuItemListeners() {
		view.getMenuLanguage().getItems().stream()
			.forEach(t->t.setOnAction((event) -> {
				
				MenuItem i = (MenuItem) event.getSource();
				String newLanguage = i.getText().toLowerCase().substring(0, 2);
				
				if(!Translator.getTranslator().getLocale().getLanguage().equalsIgnoreCase(newLanguage)) {
					Translator.getTranslator().setLanguage(newLanguage);
					view.setTexts();
				}
		}));
	}
	
	/**
	 * Disconnect from Server
	 * Kill open threads
	 * @author david
	 */
	public void handleCloseRequest() {
		view.getStage().setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent event) {
				Platform.exit();
				System.exit(0);
			}
		});
	}
}
