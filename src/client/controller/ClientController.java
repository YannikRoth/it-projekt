package client.controller;


import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.Map.Entry;
import java.util.logging.Logger;

import com.sun.glass.ui.View;

import client.ServicelocatorClient;
import client.model.ClientModel;
import client.view.ClientView;
import globals.ClientAction;
import globals.Translator;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.control.MenuItem;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.WindowEvent;
import server.ServiceLocator;
import server.model.gameplay.Card;

/**
 * 
 * @author philipp
 *
 */

public class ClientController {

	private ClientModel model;
	private ClientView view;
	private Card selectedCard;
	private Logger logger = ServiceLocator.getLogger();
	private LobbyController callingLobbyController = ServicelocatorClient.getLobbyController();
	
	public ClientController(ClientModel model, ClientView view) {
		this.model = model;
		this.view = view;
		processRulesItem();
		processHintItem();
		processAboutItem();
		processQuitItem();
		processGermanItem();
		processEnglishItem();
		processFrenchItem();
		processPlayCardButton();
		processBuildWorldWonderButton();
		processDiscardCardButton();
		processClickOnImage();
		handleCloseRequest();
		
		if(model.getMyPlayer() != null &&
				model.getMyPlayer().getResources() != null &&
				model.getMyPlayer().getResources().getResourcesListObservable() != null)
			view.getTablePoints().setItems(model.getMyPlayer().getResources().getResourcesListObservable());
	}
	
	private void processFrenchItem() {
		view.getFrenchItem().setOnAction((e) -> {
			MenuItem i = (MenuItem) e.getSource();
			String newLanguage = i.getText().toLowerCase().substring(0, 2);
			if(!Translator.getTranslator().getLocale().getLanguage().equalsIgnoreCase(newLanguage)) {
				Translator.getTranslator().setLanguage(newLanguage);
				view.setTexts();
			}
		});
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
			if(callingLobbyController != null) {
				callingLobbyController.showAndEnableView();
				view.getStage().close();
			} else {
				Platform.exit();
				System.exit(0);
			}
		});
	}

	private void processAboutItem() {
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
	}

	private void processRulesItem() {
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
	
	/**
	 * Method handels PlayCard-Button
	 * @author Roman Leuenberger
	 */

	private void processPlayCardButton() {
		view.getPlayCardButton().setOnAction(e -> {
			model.sendPlayedCard(this.selectedCard, ClientAction.PLAYCARD);
			logger.info("Played Card");
			view.disableCards();
			view.disableButtons();

			ImageView correct = null;
			
			for(Entry<ImageView, Card> temp : view.getCardsWithImages().entrySet()){
				ImageView v = temp.getKey();
				Card c = temp.getValue();
				
				if(c.equals(this.selectedCard)) {
					correct = v;
					view.addImageView(correct);
					correct.setDisable(true);
					return;
				}
			}
			
		});
	}
	
	/**
	 * Method handels BuildWorldWonder-Button
	 * @author Roman Leuenberger
	 */
	
	private void processBuildWorldWonderButton() {
		view.getBuildWorldWonderButton().setOnAction(e -> {
			model.sendPlayedCard(this.selectedCard, ClientAction.BUILDWONDER);
			logger.info("Build WorldWonder");
			view.disableCards();
			view.disableButtons();
			if(this.selectedCard.getCardAgeValue() == 1) {
				view.addImageViewWorldWonderOne();
			} else {
				view.addImageViewWorldWonderTwo();
			}
		});
	}
	
	/**
	 * Method handels DiscardCard-Button
	 * @author Roman Leuenberger
	 */
	
	private void processDiscardCardButton() {
		view.getDiscardCardButton().setOnAction(e -> {
			model.sendPlayedCard(this.selectedCard, ClientAction.DISCARD);
			logger.info("DiscardCard");
			view.disableCards();
			view.disableButtons();
		});
	}
	
	/**
	 * Method handels Layout of cards when a card gets clicked
	 * Furthermore it handels if a Button is enabled or not
	 * @author Roman Leuenberger
	 */
	
	public void processClickOnImage() {
		for (ImageView imageViewOne : view.getCardsWithImages().keySet()) {
			this.selectedCard = view.getCardsWithImages().get(imageViewOne);
			ImageView tempImage = imageViewOne;
			tempImage.setOnMouseClicked(e ->{
				tempImage.setEffect(new DropShadow(40, Color.GOLD));
				for (ImageView imageViewTwo : view.getCardsWithImages().keySet()) {
					if(imageViewTwo != tempImage) {
						imageViewTwo.setEffect(null);
					}else {
						this.selectedCard = view.getCardsWithImages().get(imageViewTwo);
						if(this.selectedCard == null) {
							System.out.println("The selected card value is null!!");
						}
					}
				}
			view.getPlayCardButton().setDisable(!model.getPlayOptionsOfCards().get(this.selectedCard).get(ClientAction.PLAYCARD));
			view.getBuildWorldWonderButton().setDisable(!model.getPlayOptionsOfCards().get(this.selectedCard).get(ClientAction.BUILDWONDER));
			view.getDiscardCardButton().setDisable(!model.getPlayOptionsOfCards().get(this.selectedCard).get(ClientAction.DISCARD));
			});
		}
		
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
				if(callingLobbyController != null)
					callingLobbyController.showAndEnableView();
				else {
					Platform.exit();
					System.exit(0);
				}
			}
		});
	}

	/**
	 * Funtionality for winnerlist quit-Button
	 * @author david
	 */
	public void updateClientViewEndGame() {
		view.getOkButton().setOnAction(e -> {
			callingLobbyController.showAndEnableView();
			this.view.getStage().close();
		});
	}
}

