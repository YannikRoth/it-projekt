package server.model.clienthandling;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Logger;

import globals.Globals;
import server.ServiceLocator;
import server.model.ServerModel;


/**
 * 
 * @author martin
 *
 */
public class ServerRequestHandler extends Thread {
	private ServerModel servermodel;
	private ServerSocket listener;
	private volatile boolean stop = false;
	private final Logger logger = ServiceLocator.getLogger();
	
	public ServerRequestHandler(ServerModel model) {
		servermodel = model;
	}

	@Override
	public void run() {
		logger.info("Start server");
		try {
			listener = new ServerSocket(Globals.getPortNr(), 10, null);
			ServerClientThread client;
			while(!stop) {
				try {
					Socket socket = listener.accept();
					client = new ServerClientThread(socket);
					logger.info("New client request received");
					synchronized(servermodel){
						servermodel.addClient(client);
						logger.info("Added new client to server");
					}
				} catch (Exception e) {
					
				}
	
			}
		} catch (IOException e) {
			logger.info(e.toString());
		}
	}
}
