package server.model;

import java.util.ArrayList;

public class ServerModel {
	
	private ArrayList<ServerClientThread> Clients = new ArrayList<ServerClientThread>();
	
	public ServerModel() {

	}
	
	public void addCardToSet() {
		
	}
	
	public void addClient(ServerClientThread Client) {
		//TODO Bedingungen wenn neuer Client erlaubt ist und wann nicht
		if(true) {
			Clients.add(Client);
		}
	}
}
