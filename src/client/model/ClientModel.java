package client.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Collections;
import java.util.Comparator;
import java.util.logging.Logger;

import globals.Globals;
import globals.ResourceType;
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
		// TODO: for example, to be remove
		connect();
		refreshOtherPlayer(new Player("David"));
		player = new Player("Mein Spieler");
		player.getResources().put(ResourceType.BRICK, 20);
	}

	public void connect() {
		// TODO Kommunikation mit dem Server hier abhandeln
		this.start();
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

	/**
	 * 
	 * @author martin
	 *
	 */
	@Override
	public void run() {
		try (Socket socket = new Socket(Globals.getDefaultIPAddr(), Globals.getPortNr())) {
			this.objOutputStream = new ObjectOutputStream(socket.getOutputStream());
			this.objInputStream = new ObjectInputStream(socket.getInputStream());
			// TODO nachrichten vom server empfangen
			while (true) {
				Player test;
				test = (Player) objInputStream.readObject();

				playCard(test.getPlayableCards().get(0));
				logger.info("Player Objects received from Server" + test);
			}

		} catch (IOException e) {
			logger.info(e.getLocalizedMessage());
		} catch (ClassNotFoundException e) {
			logger.info(e.getLocalizedMessage());
		}

		/*
		 * while(true) { testclassserializable test = new testclassserializable();
		 * test.setTest(100); objOutputStream.writeObject(test);
		 * objOutputStream.flush();
		 * 
		 * test = (testclassserializable) (objInputStream.readObject());
		 * logger.info(test.getTest() + "nummer");
		 * 
		 * // playCard( player.getPlayableCards().get(0)); // this.start();
		 * logger.info("Connection to Server opened"); } } catch (IOException e) {
		 * logger.info("Error occured while receiving Player objects from Server 1");
		 * }catch (ClassNotFoundException e) {
		 * logger.info("Error occured while receiving Player objects from Server 2"); }
		 */
	}

	public ObservableList<Player> getOtherPlayers() {
		return otherPlayers;
	}

	/**
	 * add or refreshs the param player in otherplayer list
	 * 
	 * @param p
	 * @author david
	 */
	public void refreshOtherPlayer(Player p) {
		if (!getOtherPlayers().contains(p))
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

	public Player getMyPlayer() {
		return player;
	}

	public void setMyPlayer(Player player) {
		this.player = player;
	}
}
