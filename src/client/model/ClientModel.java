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
 * @author martin
 *
 */

public class ClientModel extends Thread {

	private Player player;
	private ObservableList<Player> otherPlayers = FXCollections.observableArrayList();
	private int numberofPlayers;
	private ObjectInputStream objInputStream;
	private ObjectOutputStream objOutputStream;
	private BufferedReader inputmessage;
	private PrintWriter outputmessage;

	private static final Logger logger = ServiceLocator.getLogger();

	public ClientModel() {
		// TODO: for example, to be remove
		refreshOtherPlayer(new Player("David"));
		player = new Player("Mein Spieler");
		player.getResources().put(ResourceType.BRICK, 20);
	}
	
	
	public void playCard(Card playedcard, String action) {
		try {
			
			outputmessage.println(action);
			outputmessage.flush();
			objOutputStream.writeObject(playedcard);
			objOutputStream.flush();
			logger.info("Card "+playedcard.getCardName()+" sent to Server - with following Action: " + action);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.info("Error occured while sending card");
		}
	}


	@Override
	public void run() {
		//Join Game
		String message;

		try (Socket socket = new Socket(Globals.getDefaultIPAddr(), Globals.getPortNr())) {
			this.objOutputStream = new ObjectOutputStream(socket.getOutputStream());
			this.objInputStream = new ObjectInputStream(socket.getInputStream());
			//used to know what the server wants
			this.inputmessage = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			//used to inform the server what we wanna do
			this.outputmessage = new PrintWriter(socket.getOutputStream()); 
			
			while ((message = inputmessage.readLine()) != null) {
				switch (message) {
				case "connectionestablished":
					numberofPlayers = Integer.parseInt(inputmessage.readLine());
					logger.info("Connection and Game opened with "+numberofPlayers+" Players");
					break;
					
				case "updateview":
					Player tempplayer;
					player = (Player) objInputStream.readObject();
					logger.info("Own Player Object "+player.getPlayerName()+" received from Server");
					otherPlayers.clear();
					for (int i = 1; i < numberofPlayers; i++) {
						tempplayer = (Player) objInputStream.readObject();
						otherPlayers.add(tempplayer);
						logger.info("Opponent player "+tempplayer.getPlayerName()+" received from Server");						
						//refreshOtherPlayer(tempplayer);
					}
					playCard(player.getPlayableCards().get(0), "playcard");
					break;
					
				case "gameend":				
					break;

				default:
					logger.info("An error Occured during the Communication - invalid input from the Server");
					break;
				}
			}

		} catch (IOException e) {
			logger.info(e.getLocalizedMessage());
		} catch (ClassNotFoundException e) {
			logger.info(e.getLocalizedMessage());
		}
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
