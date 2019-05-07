package client.controller;

import client.model.CardOptionModel;
import client.model.ClientModel;
import client.view.CardOptionView;

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
		
	}

}
