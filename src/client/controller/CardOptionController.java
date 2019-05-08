package client.controller;

import client.model.CardOptionModel;
import client.model.ClientModel;
import client.view.CardOptionView;
import javafx.application.Platform;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

/**
 * 
 * @author Roman Leuenberger
 *
 */

public class CardOptionController {
	
	private ClientModel model;
	private CardOptionView view;
	
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
			System.out.println("PlayCard");
			view.stopView();
		});
	}
	
	private void processBuildWorldWonderButton() {
		view.getBuildWorldWonderButton().setOnAction(e -> {
			System.out.println("BuildWorldWonder");
			view.stopView();
		});
	}
	
	private void processDiscardCardButton() {
		view.getDiscardCardButton().setOnAction(e -> {
			System.out.println("DiscardCard");
			view.stopView();
		});
	}
	
	private void processClickOnImage() {
		for (int i = 0; i<view.getShownCards().length;i++) {
			ImageView tempImage = view.getImageView(i);
			tempImage.setOnMouseClicked(e ->{
				tempImage.setEffect(new DropShadow(5, Color.RED));
				for (int j = 0;j<view.getShownCards().length;j++) {
					if(view.getImageView(j) != tempImage) {
						view.getImageView(j).setEffect(null);
					}
				}
			});
		}
	}

}
