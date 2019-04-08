package server.model.clienthandling;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Logger;


/**
 * 
 * @author martin
 *
 */
public class ServerRequestHandler extends Thread {
	private ServerModel servermodel;
	private ServerSocket listener;
	private volatile boolean stop = false;
	private final Logger logger = Logger.getLogger("");
	
	public ServerRequestHandler(ServerModel model) {
		servermodel = model;
	}

	@Override
	public void run() {
		logger.info("Start server");
		try {
			listener = new ServerSocket(8080, 10, null);
			ServerClientThread client;
			while(!stop) {
				try {
					Socket socket = listener.accept();
					client = new ServerClientThread(socket);
					logger.info("Neuer Client hat den Server angefragt");
					synchronized(servermodel){
						servermodel.addClient(client);
					}
				} catch (Exception e) {
					
				}
	
			}
		} catch (IOException e) {
			logger.info(e.toString());
		}
	}
}
