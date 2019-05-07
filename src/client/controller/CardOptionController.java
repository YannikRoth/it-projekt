package client.controller;

import client.model.CardOptionModel;
import client.model.ClientModel;
import client.view.CardOptionView;
import javafx.application.Platform;

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

}
