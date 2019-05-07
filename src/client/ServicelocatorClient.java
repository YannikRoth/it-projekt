package client;

import client.model.ClientModel;
import server.ServiceLocator;
import server.model.ServerModel;

public class ServicelocatorClient {

	private static ClientModel ClientModelInstance = null;
	
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
	
}
