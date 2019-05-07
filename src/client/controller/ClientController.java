package client.controller;


import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

import client.model.ClientModel;
import client.view.CardOptionView;
import client.view.ClientView;
import globals.ClientAction;
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
		view.getAboutItem().setOnAction(e ->{
			if (Desktop.isDesktopSupported()) {
			    try {
			        File myFile = new File("./resource/images/blackhat-logo.jpg");
			        Desktop.getDesktop().open(myFile);
			    } catch (IOException ex) {
			        // no application registered for PDFs
			    }
			}
		});
	}

	private void processHintItem() {
		// TODO Auto-generated method stub
		
	}

	private void processRulesItem() {
		// TODO Auto-generated method stub
		view.getRulesItem().setOnAction((e) -> {
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

}
