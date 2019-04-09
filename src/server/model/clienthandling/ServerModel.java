package server.model.clienthandling;

import java.util.ArrayList;
import java.util.logging.Logger;

import server.ServiceLocator;

public class ServerModel {

	private final Logger logger = ServiceLocator.getLogger();
	private ArrayList<ServerClientThread> Clients = new ArrayList<ServerClientThread>();
	
	public ServerModel() {

	}
	
	public void addCardToSet() {
		
	}
	
	public void addClient(ServerClientThread Client) {
		// TODO Bedingungen wenn neuer Client erlaubt ist und wann nicht
		if(true) {
			Clients.add(Client);
			Client.start();
			logger.info("Client erfolgreich hinzugefügt");
		}else {
			logger.info("Client konnte nicht hinzugefügt werden");
		}
	}
}
