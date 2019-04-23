package client.model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Collections;
import java.util.Comparator;
import java.util.logging.Logger;

import globals.Globals;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import server.ServiceLocator;
import server.model.gameplay.Player;
/**
 * 
 * @author Phillip
 *
 */

public class ClientModel extends Thread {

	private Player player;
	private ObservableList<Player> otherPlayers = FXCollections.observableArrayList();
	
	private ObjectInputStream objInputStream;
	private ObjectOutputStream objOutputStream;
	
	private static final Logger logger = ServiceLocator.getLogger();
	
	public ClientModel() {
		//TODO: for example, to be remove
		refreshOtherPlayer(new Player("David"));
		
	}
	
	
	public void connect(String ipadress, String name) {
		// TODO Kommunikation mit dem Server hier abhandeln
	
		try (Socket socket = new Socket(ipadress, Globals.getPortNr())) {
			this.objOutputStream = new ObjectOutputStream(socket.getOutputStream());
			this.objOutputStream.flush();
			this.objInputStream = new ObjectInputStream(socket.getInputStream());
			this.start();
			//TODO nachrichten zum server senden
			while (true) {
				objOutputStream.writeObject(player.getPlayableCards().get(0));
				objOutputStream.flush();
			}
		}catch(Exception e) {
			logger.info("An error occured while connecting to the server" + e.getStackTrace());
		}
	}
	
	@Override
	public void run() {
		try {
				//TODO nachrichten vom server empfangen
				while (true) {
					player = (Player) objInputStream.readObject();
					
				}
			
			} catch (IOException | ClassNotFoundException e) {
		}
	}
	
	public ObservableList<Player> getOtherPlayers() {
		return otherPlayers;
	}

	/**
	 * add or refreshs the param player in otherplayer list
	 * @param p
	 * @author david
	 */
	public void refreshOtherPlayer(Player p) {
		if(!getOtherPlayers().contains(p))
			getOtherPlayers().add(p);
		else {
			Player o = getOtherPlayers().get(getOtherPlayers().indexOf(p));
			o = p;
			
			Collections.sort(getOtherPlayers(), new Comparator<Player>() {
				@Override
				public int compare(Player o1, Player o2) {
					return o1.toString().compareTo(o2.toString());
				}
			});
		}
	}
}
