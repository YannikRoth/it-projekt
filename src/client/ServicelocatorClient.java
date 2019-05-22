package client;

import client.controller.ClientController;
import client.controller.LobbyController;
import client.model.ClientModel;
import client.model.LobbyModel;
import client.view.ClientView;

public class ServicelocatorClient {

	private static ClientModel ClientModelInstance = null;
	private static LobbyModel LobbyModelInstance = null;
	private static ClientController controllerInstance = null;
	private static LobbyController lobbycontrollerinstance = null;
	private static ClientView viewInstance = null;
	
	public static void setClientModel(ClientModel m) {
		if(ClientModelInstance == null) {
			ClientModelInstance = m;
		}else {
			//do not override model!
		}
	}
	

	public static ClientModel getClientModel() {
		return ServicelocatorClient.ClientModelInstance; 
	}

	public static void setLobbyModel(LobbyModel m) {
		if(LobbyModelInstance == null) {
			LobbyModelInstance = m;
		}else {
			//do not override model!
		}
	}
	public static LobbyModel getLobbyModel() {
		return ServicelocatorClient.LobbyModelInstance; 
	}
	
	
	
	public static void setClientController(ClientController m) {
		if(controllerInstance == null) {
			controllerInstance = m;
		}else {
			//do not override model!
		}
	}
	

	public static ClientController getClientController() {
		return ServicelocatorClient.controllerInstance; 
	}
	
	
	public static void setLobbyController(LobbyController m) {
		if(lobbycontrollerinstance == null) {
			lobbycontrollerinstance = m;
		}else {
			//do not override model!
		}
	}
	

	public static LobbyController getLobbyController() {
		return ServicelocatorClient.lobbycontrollerinstance; 
	}	
	
	public static void setClientView(ClientView m) {
		if(viewInstance == null) {
			viewInstance = m;
		}else {
			//do not override model!
		}
	}
	

	public static ClientView getClientView() {
		return ServicelocatorClient.viewInstance; 
	}
	
	
}
