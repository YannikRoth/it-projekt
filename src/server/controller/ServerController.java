package server.controller;

import java.util.Optional;

import globals.Translator;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputDialog;
import server.model.ServerModel;
import server.model.gameplay.ServerAction;
import server.view.ServerView;

public class ServerController{
	private ServerModel model;
	private ServerView view;
	private Translator translator = Translator.getTranslator();

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
			dialog.setGraphic(null);
			dialog.setTitle(		translator.getString("dlg.port.changeport"));
			dialog.setHeaderText(	translator.getString("dlg.port.choosenew"));
			
			((Button)dialog.getDialogPane().lookupButton(ButtonType.OK)).setText(translator.getString("dlg.ok"));
			((Button)dialog.getDialogPane().lookupButton(ButtonType.CANCEL)).setText(translator.getString("dlg.cancel"));
			
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
				
				MenuItem i = (MenuItem) event.getSource();
				String newLanguage = i.getText().toLowerCase().substring(0, 2);
				
				if(!Translator.getTranslator().getLocale().getLanguage().equalsIgnoreCase(newLanguage)) {
					Translator.getTranslator().setLanguage(newLanguage);
					view.setTexts();
				}
		}));
	}
}
