package client.controller;


import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.Map.Entry;
import java.util.logging.Logger;

import client.model.ClientModel;
import client.view.CardOptionView;
import client.view.ClientView;
import globals.ClientAction;
import globals.Translator;
import javafx.application.Platform;
import javafx.scene.control.MenuItem;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
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
	
	public ClientController(ClientModel model, ClientView view) {
		this.model = model;
		this.view = view;
		processRulesItem();
		processHintItem();
		processAboutItem();
		processQuitItem();
		processGermanItem();
		processEnglishItem();
		processPlayCardButton();
		processBuildWorldWonderButton();
		processDiscardCardButton();
		processClickOnImage();
		
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

	private void processPlayCardButton() {
		view.getPlayCardButton().setOnAction(e -> {
			//model.getMyPlayer().playCard(this.selectedCard);
			model.sendPlayedCard(this.selectedCard, ClientAction.PLAYCARD);
			logger.info("Played Card");
			view.disableCards();
//			view.updatePlayedCardView();

			ImageView correct = null;
			
			for(Entry<ImageView, Card> temp : view.getCardsWithImages().entrySet()){
				ImageView v = temp.getKey();
				Card c = temp.getValue();
				
				if(c.equals(this.selectedCard)) {
					correct = v;
					view.addImageView(correct);
				}
			}
			
		});
	}
	
	private void processBuildWorldWonderButton() {
		view.getBuildWorldWonderButton().setOnAction(e -> {
			//model.getMyPlayer().playCard(this.selectedCard);
			model.sendPlayedCard(this.selectedCard, ClientAction.BUILDWONDER);
			logger.info("Build WorldWonder");
			view.disableCards();
			//view.updatePlayedCardView();
		});
	}
	
	private void processDiscardCardButton() {
		view.getDiscardCardButton().setOnAction(e -> {
			//TODO Method for discard card
			model.sendPlayedCard(this.selectedCard, ClientAction.DISCARD);
			logger.info("DiscardCard");
			view.disableCards();
//			ImageView v = (ImageView) e.getSource();
			//view.updatePlayedCardView();
		});
	}
	
	public void processClickOnImage() {
		for (int i = 0; i<view.getShownCards().length;i++) {
			this.selectedCard = view.getCardsWithImages().get(view.getImageView(i));
			ImageView tempImage = view.getImageView(i);
			tempImage.setOnMouseClicked(e ->{
				tempImage.setEffect(new DropShadow(5, Color.RED));
				for (int j = 0;j<view.getShownCards().length;j++) {
					if(view.getImageView(j) != tempImage) {
						view.getImageView(j).setEffect(null);
					}else {
						this.selectedCard = view.getCardsWithImages().get(view.getImageView(j));
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
	

}

