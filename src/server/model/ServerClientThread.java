package server.model;

import java.net.Socket;

public class ServerClientThread {
	private Socket socket;
	
	public ServerClientThread(Socket socket) {
		this.socket = socket;
	}
}
