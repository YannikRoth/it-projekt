package server.model;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Logger;


/**
 * 
 * @author martin
 *
 */
public class ServerRequestHandler implements Runnable {
	private ServerModel servermodel;
	private ServerSocket listener;
	private volatile boolean stop = false;
	
	public ServerRequestHandler(ServerModel model) {
		servermodel = model;
	}

	@Override
	public void run() {
		ServerClientThread client;
		while(!stop) {
			try {
				Socket socket = listener.accept();
				client = new ServerClientThread(socket);
				synchronized(servermodel){
					servermodel.addClient(client);
				}
			} catch (Exception e) {
			}

		}
	}
}
