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
import server.model.gameplay.Card;
import server.model.gameplay.Player;
import test.testclassserializable;

/**
 * 
 * @author philipp
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
		connect();
		refreshOtherPlayer(new Player("David"));
		
	}
	
	
	public void connect() {
		// TODO Kommunikation mit dem Server hier abhandeln
	
		try (Socket socket = new Socket(Globals.getDefaultIPAddr(), Globals.getPortNr())) {
			this.objOutputStream = new ObjectOutputStream(socket.getOutputStream());
			this.objInputStream = new ObjectInputStream(socket.getInputStream());
			logger.info(socket.toString());
			this.start();
			logger.info("Connection to Server opened");
		}catch(Exception e) {
			logger.info("An error occured while connecting to the server" + e.getStackTrace());
		}
	}
	
	public void playCard(Card playedcard) {
		try {
			objOutputStream.writeObject(playedcard);
			objOutputStream.flush();
			logger.info("Card Sent to Server");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.info("Error occured while sending card");
		}
	}
	
	@Override
	public void run() {
		try {
				//TODO nachrichten vom server empfangen
				while (true) {
					testclassserializable test = new testclassserializable();
					objOutputStream.writeObject(test);
					objOutputStream.flush();
				//			test = (testclassserializable) (objInputStream.readObject());
					
			//		playCard( player.getPlayableCards().get(0));
					logger.info("Player Objects received from Server" + test);
				}
			
			} catch (IOException e) {
				logger.info("Error occured while receiving Player objects from Server 1");
			}/*catch (ClassNotFoundException e) {
				logger.info("Error occured while receiving Player objects from Server 2");
		}*/
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
