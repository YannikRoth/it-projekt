package server.controller;

import java.util.Optional;

import globals.Translator;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputDialog;
import javafx.stage.WindowEvent;
import server.model.ServerModel;
import server.model.gameplay.ServerAction;
import server.view.ServerView;

public class ServerController{
	private ServerModel model;
	private ServerView view;

	public ServerController(ServerModel model, ServerView view) {
		this.model = model;
		this.view = view;
		
		AddViewButtonListeners();
		AddMenuItemListeners();
	}

	/**
	 * Add listeners to the buttons in view
	 * @author david
	 * 
	 */
	private void AddViewButtonListeners() {
		view.getButtonChangePort().setOnAction((event) -> {
			TextInputDialog dialog = new TextInputDialog();
			dialog.setTitle("Change Port");
			dialog.setHeaderText("Choose new server port:");
			
			Optional<String> result = dialog.showAndWait();
			result.ifPresent(name -> {
				//TODO: Notify Clients and Change ServerSocket Port (I think it's a model task...)
				view.serverActionData.add(new ServerAction("localhost", "Server", "Port changed to: " + name));
			});
		});
		
		view.getButtonRestartServer().setOnAction((event) -> {
			//TODO: Restart ServerSocket
			view.serverActionData.add(new ServerAction("localhost", "Server", "Server restarted"));
		});
	}
	
	/**
	 * Add listeners to menu and menuitems 
	 * @author david
	 */
	private void AddMenuItemListeners() {
		view.getMenuLanguage().getItems().stream()
			.forEach(t->t.setOnAction((event) -> {
				String newLanguage = ((MenuItem) event.getSource())
											.getText().toLowerCase().substring(0, 2);
				Translator.getTranslator().setLanguage(newLanguage);
				view.setTexts();
		}));
	}
	
	private void changeLanguage(Event e) {
	}
}
