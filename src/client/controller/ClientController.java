package client.controller;

import client.model.ClientModel;
import client.view.ClientView;

public class ClientController {

	private ClientModel model;
	private ClientView view;
	
	public ClientController(ClientModel model, ClientView view) {
		this.model = model;
		this.view = view;
	}
	
}
