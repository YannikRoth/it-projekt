package server.model.clienthandling;

import java.io.IOException;
import java.io.Serializable;
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
public class ServerRequestHandler extends Thread implements Serializable{
	private ServerModel servermodel;
	private ServerSocket listener;
	private volatile boolean stop = false;
	private final Logger logger = ServiceLocator.getLogger();
	private Socket socket;
	private ServerClientThread client;
	
	public ServerRequestHandler(ServerModel model) {
		servermodel = model;
	}

	@Override
	public void run() {
		logger.info("Start server");
			try {
				listener = new ServerSocket(Globals.getPortNr());
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			while(!stop) {
				try {
					socket = listener.accept();
					client = new ServerClientThread(socket, servermodel);
					logger.info("New client request received");
					if (socket.isConnected()) {
						synchronized(servermodel){
							servermodel.addClient(client);
							logger.info("Added new client to server");
						}
					}
				} catch (Exception e) {
					logger.info("Error occured during Request processing" + e.getLocalizedMessage());
				}
	
			}

	}
}
