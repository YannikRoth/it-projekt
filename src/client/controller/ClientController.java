package client.controller;


import client.model.ClientModel;
import client.view.ClientView;
import globals.Translator;
import javafx.application.Platform;
import javafx.scene.control.MenuItem;

/**
 * 
 * @author philipp
 *
 */

public class ClientController {

	private ClientModel model;
	private ClientView view;
	
	public ClientController(ClientModel model, ClientView view) {
		this.model = model;
		this.view = view;
		
		processRulesItem();
		processHintItem();
		processAboutItem();
		processQuitItem();
		processGermanItem();
		processEnglishItem();
		
	}
	
	private void processEnglishItem() {
		view.getEnglishItem().setOnAction((e) -> {
			MenuItem i = (MenuItem) e.getSource();
			String newLanguage = i.getText().toLowerCase().substring(0, 2);
			if(!Translator.getTranslator().getLocale().getLanguage().equalsIgnoreCase(newLanguage)) {
				Translator.getTranslator().setLanguage(newLanguage);
				view.setTexts();
			}
		});
	}

	private void processGermanItem() {
		view.getGermanItem().setOnAction((e) -> {
			MenuItem i = (MenuItem) e.getSource();
			String newLanguage = i.getText().toLowerCase().substring(0, 2);
			if(!Translator.getTranslator().getLocale().getLanguage().equalsIgnoreCase(newLanguage)) {
				Translator.getTranslator().setLanguage(newLanguage);
				view.setTexts();
			}
		});
	}

	private void processQuitItem() {
		view.getQuitItem().setOnAction((e) -> {
			Platform.exit();
			System.exit(0);
		});
	}

	private void processAboutItem() {
		// TODO Auto-generated method stub
		
	}

	private void processHintItem() {
		// TODO Auto-generated method stub
		
	}

	private void processRulesItem() {
		// TODO Auto-generated method stub
		
	}

}
