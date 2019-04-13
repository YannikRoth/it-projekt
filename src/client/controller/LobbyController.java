package client.controller;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

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
			if (Desktop.isDesktopSupported()) {
			    try {
			    	String lang = Translator.getTranslator().getLocale().getLanguage(); 
			        File myFile = new File("./resource/rules/"+lang+"_7WONDERS_RULES.pdf");
			        Desktop.getDesktop().open(myFile);
			    } catch (IOException ex) {
			        // no application registered for PDFs
			    }
			}
		});
	}

	private void processQuitButton() {
		view.getQuitButton().setOnAction((e) -> {
			Platform.exit();
			System.exit(0);
		});
	}

}
