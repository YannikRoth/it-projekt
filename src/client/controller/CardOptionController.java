package client.controller;

import client.model.CardOptionModel;
import client.view.CardOptionView;

/**
 * 
 * @author Roman Leuenberger
 *
 */

public class CardOptionController {
	
	private CardOptionModel model;
	private CardOptionView view;
	
	public CardOptionController (CardOptionModel model, CardOptionView view) {
		this.model = model;
		this.view = view;
		
	}

}
