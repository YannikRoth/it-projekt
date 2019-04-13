package server.controller;

import java.util.Optional;
import java.util.logging.Logger;

import globals.Globals;
import globals.Translator;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputDialog;
import server.ServiceLocator;
import server.model.ServerModel;
import server.model.gameplay.ServerAction;
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
	}

	/**
	 * Add listeners to the buttons in view
	 * @author david
	 * 
	 */
	private void AddViewButtonListeners() {
		view.getButtonChangePort().setOnAction((event) -> {
			//TODO: Notify Clients and Change ServerSocket Port (I think it's a model task...)
			
			view.startNewPortDialog().ifPresent(name -> {
				int port;
				try {
					port = Integer.parseInt(name);
					if(Globals.getPortNr() != port)
						Globals.setPortNr(port);
					//TODO: Change server port
				} catch (NumberFormatException e){
					logger.info("Error cast port to \"" + name + "\", use default: 8080");
					Globals.setPortNr(8080);
				}
			});
			
			view.serverActionData.add(new ServerAction("localhost", "Server", "Port changed to: " + Globals.getPortNr()));
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
				
				MenuItem i = (MenuItem) event.getSource();
				String newLanguage = i.getText().toLowerCase().substring(0, 2);
				
				if(!Translator.getTranslator().getLocale().getLanguage().equalsIgnoreCase(newLanguage)) {
					Translator.getTranslator().setLanguage(newLanguage);
					view.setTexts();
				}
		}));
	}
}
