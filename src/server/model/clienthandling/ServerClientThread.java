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
			this.objOutputStream = new ObjectOutputStream(socket.getOutputStream());
			this.objOutputStream.flush();
			this.objInputStream = new ObjectInputStream(socket.getInputStream()); 
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Override
	public void run() {
		Card cardplayed;
		// TODO Kommunikation zwischen Clientgerät, Clientthread und Gamehandling
		while(true) {
			try {
				objOutputStream.writeObject(player);
				objOutputStream.flush();
				cardplayed = (Card) objInputStream.readObject();
			} catch (IOException | ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}
	
	public Player getPlayer() {
		return player;
	}
	
}
