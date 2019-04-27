package server.model.clienthandling;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Logger;

import server.ServiceLocator;
import server.model.gameplay.Card;
import server.model.gameplay.Player;
/**
 * 
 * @author martin
 *
 */
public class ServerClientThread extends Thread{
	private Socket socket;
	private boolean stop;
	private Player player;
	private final Logger logger = ServiceLocator.getLogger();
	private ObjectInputStream objInputStream;
	private ObjectOutputStream objOutputStream;
	
	//TODO ObjectInput/Output reader instead of inputstreamreader
	
	public ServerClientThread(Socket socket) {
		//TODO: Add player name in constructor
		player = new Player("");
		this.socket = socket;
		try {
			this.objOutputStream = new ObjectOutputStream(this.socket.getOutputStream());
			this.objOutputStream.flush();
			this.objInputStream = new ObjectInputStream(this.socket.getInputStream()); 
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Override
	public void run() {
		Card cardplayed;
		// TODO Kommunikation zwischen Clientgerät, Clientthread und Gamehandling
		stop = false;
		while(!stop) {
			try {
				objOutputStream.writeObject(player);
				objOutputStream.flush();
				logger.info("Players sent to Client");
				cardplayed = (Card) objInputStream.readObject();
				logger.info("Cards received from Client");
			} catch (IOException | ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				logger.info("Error occured durring communication with client");
				stop = true;
			}

		}
	}
	
	public Player getPlayer() {
		return player;
	}
	
}
