package client.controller;

import client.model.LobbyModel;
import client.view.LobbyView;

public class LobbyController {

	private LobbyModel model;
	private LobbyView view;
	
	public LobbyController(LobbyModel model, LobbyView view) {
		this.model = model;
		this.view = view;
	}

}
