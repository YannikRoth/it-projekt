package client.controller;

import client.model.CardOptionModel;
import client.model.ClientModel;
import client.view.CardOptionView;
import globals.ClientAction;
import javafx.application.Platform;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import server.ServiceLocator;
import server.model.gameplay.Card;
import java.util.logging.Logger;

/**
 * 
 * @author Roman Leuenberger
 *
 */

public class CardOptionController {
	
	private ClientModel model;
	private CardOptionView view;
	private Card selectedCard;
	private Logger logger = ServiceLocator.getLogger();
	
	public CardOptionController (ClientModel model, CardOptionView view) {
		this.model = model;
		this.view = view;
		processPlayCardButton();
		processBuildWorldWonderButton();
		processDiscardCardButton();
		processClickOnImage();
	}
	
	private void processPlayCardButton() {
		view.getPlayCardButton().setOnAction(e -> {
			model.getMyPlayer().playCard(this.selectedCard);
			logger.info("Played Card");
			view.stopView();
		});
	}
	
	private void processBuildWorldWonderButton() {
		view.getBuildWorldWonderButton().setOnAction(e -> {
			model.getMyPlayer().playCard(this.selectedCard);
			logger.info("Build WorldWonder");
			view.stopView();
		});
	}
	
	private void processDiscardCardButton() {
		view.getDiscardCardButton().setOnAction(e -> {
			//TODO Method for discard card
			logger.info("DiscardCard");
			view.stopView();
		});
	}
	
	private void processClickOnImage() {
		for (int i = 0; i<view.getShownCards().length;i++) {
			this.selectedCard = view.getCardsWithImages().get(view.getImageView(i));
			ImageView tempImage = view.getImageView(i);
			tempImage.setOnMouseClicked(e ->{
				tempImage.setEffect(new DropShadow(5, Color.RED));
				for (int j = 0;j<view.getShownCards().length;j++) {
					if(view.getImageView(j) != tempImage) {
						view.getImageView(j).setEffect(null);
					}
				}
			view.getPlayCardButton().setDisable(!model.getPlayOptionsOfCards().get(this.selectedCard).get(ClientAction.PLAYCARD));
			view.getBuildWorldWonderButton().setDisable(!model.getPlayOptionsOfCards().get(this.selectedCard).get(ClientAction.BUILDWONDER));
			view.getDiscardCardButton().setDisable(!model.getPlayOptionsOfCards().get(this.selectedCard).get(ClientAction.DISCARD));
			});
		}
	}

}
