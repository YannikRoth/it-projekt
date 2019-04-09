package client.controller;

import client.ClientMVC;
import client.model.LobbyModel;
import client.view.LobbyView;
import globals.Translator;
import javafx.application.Platform;
import javafx.scene.control.MenuItem;

/**
 * 
 * @author philipp
 *
 */

public class LobbyController {

	private LobbyModel model;
	private LobbyView view;
	
	public LobbyController(LobbyModel model, LobbyView view) {
		this.model = model;
		this.view = view;
		
		processNewGameButton();
		processRulesButton();
		processQuitButton();
		processGermanMenuItem();
		processEnglishItem();
		
	}
	
	
	private void processEnglishItem() {
		view.getEnglishItem2().setOnAction((e) -> {
			MenuItem i = (MenuItem) e.getSource();
			String newLanguage = i.getText().toLowerCase().substring(0, 2);
			if(!Translator.getTranslator().getLocale().getLanguage().equalsIgnoreCase(newLanguage)) {
				Translator.getTranslator().setLanguage(newLanguage);
				view.setTexts();
			}
		});
	}
	private void processGermanMenuItem() {
		view.getGermanItem2().setOnAction((e) -> {
			MenuItem i = (MenuItem) e.getSource();
			String newLanguage = i.getText().toLowerCase().substring(0, 2);
			if(!Translator.getTranslator().getLocale().getLanguage().equalsIgnoreCase(newLanguage)) {
				Translator.getTranslator().setLanguage(newLanguage);
				view.setTexts();
			}
		});
	}


	private void processNewGameButton() {
		view.getNewGameButton().setOnAction((e) -> {
			ClientMVC newGame = new ClientMVC();
//			this.view.stop();
		});
	}
	
	private void processRulesButton() {
		view.getRulesButton().setOnAction((e) -> {
			System.out.println("TEST RULES");
		});
	}

	private void processQuitButton() {
		view.getQuitButton().setOnAction((e) -> {
			Platform.exit();
			System.exit(0);
		});
	}

}
