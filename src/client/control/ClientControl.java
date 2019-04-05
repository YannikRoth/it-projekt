package client.control;

import client.model.ClientModel;
import client.view.ClientView;

public class ClientControl {

	private ClientModel model;
	private ClientView view;
	
	public ClientControl(ClientModel model, ClientView view) {
		this.model = model;
		this.view = view;
	}
	
}
