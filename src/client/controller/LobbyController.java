package client.controller;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

import client.ClientMVC;
import client.model.ClientModel;
import client.model.LobbyModel;
import client.view.ClientView;
import client.view.LobbyView;
import globals.Globals;
import globals.Translator;
import javafx.application.Platform;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;


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
			Stage secondStage = new Stage();
			ClientMVC clientMVC = new ClientMVC();
			try {
				clientMVC.start(secondStage);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
//			view.getStage().hide();
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

	/**
	 * Handle IP Input and write globals
	 * Format: IP:Port -> 127.0.0.1:8080
	 * @author david
	 */
	public static void handleIpInput(String ipInput) {
		String ip = ipInput.substring(0, ipInput.indexOf(":"));
		String port = ipInput.substring(ipInput.indexOf(":") + 1);
		Globals.setDefaultIPAddr(ip);
		Globals.setPortNr(Integer.parseInt(port));;
	}
}
