package client;

import client.controller.ClientController;
import client.model.ClientModel;
import client.view.ClientView;
import server.ServiceLocator;
import server.model.ServerModel;

public class ServicelocatorClient {

	private static ClientModel ClientModelInstance = null;
	private static ClientController controllerInstance = null;
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
