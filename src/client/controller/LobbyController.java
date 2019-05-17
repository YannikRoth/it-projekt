package client.controller;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import client.ClientMVC;
import client.ServicelocatorClient;
import client.model.LobbyModel;
import client.view.LobbyView;
import globals.Globals;
import globals.Translator;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import server.ServiceLocator;


/**
 * 
 * @author philipp
 *
 */

public class LobbyController {

	private LobbyModel model;
	private LobbyView view;
	


	public LobbyController(LobbyModel model, LobbyView view) {
		this.setModel(model);
		this.view = view;
		
		processNewGameButton();
		processRulesButton();
		processQuitButton();
		processLeaderboardButton();
		processGermanMenuItem();
		processEnglishItem();
		processFrenchItem();
		handleCloseRequest();
	}
	
	
	private void processFrenchItem() {
		view.getFrenchItem2().setOnAction((e) -> {
			MenuItem i = (MenuItem) e.getSource();
			String newLanguage = i.getText().toLowerCase().substring(0, 2);
			if(!Translator.getTranslator().getLocale().getLanguage().equalsIgnoreCase(newLanguage)) {
				Translator.getTranslator().setLanguage(newLanguage);
				view.setTexts();
			}
		});
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
	public void processNewGame() {
		view.disableDialogElements();
		view.getButtonLeaderboard().setDisable(false);
		handleInetAdress(view.getIpAdress(), view.getPort());
		Stage secondStage = new Stage();
		ClientMVC clientMVC = new ClientMVC();
		try {
			view.stop();
			clientMVC.start(secondStage);
		} catch (Exception e1) {
			ServiceLocator.getLogger().warning(e1.getMessage());
		}
	}

	private void processNewGameButton() {
		view.getNewGameButton().setOnAction((e) -> {
			if(!ServicelocatorClient.getClientModel().isAlive()) {
				ServicelocatorClient.getClientModel().setInputPlayerName(view.getPlayerName().getText());
				ServicelocatorClient.getClientModel().start();
				view.disableDialogElements();
				view.getButtonLeaderboard().setDisable(false);
			}
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
	 * shows leaderboard on button click
	 * @author david
	 */
	private void processLeaderboardButton() {
		view.getButtonLeaderboard().setOnAction((e) -> {
			Alert dialog = new Alert(AlertType.INFORMATION);
			dialog.setTitle(Translator.getTranslator().getString("button.leaderboard"));
			dialog.setHeaderText(Translator.getTranslator().getString("message.leaderboard"));
			dialog.setContentText("1...\r\n2...\r\n3...");
			dialog.showAndWait();
		});
	}

	/**
	 * Handle IP and port input and write globals
	 * @author david
	 */
	public static void handleInetAdress(String ip, int port) {
		Globals.setDefaultIPAddr(ip);
		Globals.setPortNr(port);
	}
	
	/**
	 * shows lobby an enables elements
	 * can be called from ClientController after close request
	 * @author david
	 */
	public void showAndEnableView() {
		view.enableDialogElements();
		view.getButtonLeaderboard().setDisable(true);
		view.start();
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


	public LobbyModel getModel() {
		return model;
	}
	public void setModel(LobbyModel model) {
		this.model = model;
	}
	public LobbyView getView() {
		return view;
	}
	public void setView(LobbyView view) {
		this.view = view;
	}


	public void updateWaitingPlayers(ArrayList<String> listOfWaitingPlayers) {
		
		
	}
}
