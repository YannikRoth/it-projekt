package server.model;

import java.net.Socket;

public class ServerClientThread {
	Socket socket;
	public ServerClientThread(Socket socket) {
		this.socket = socket;
	}
}
