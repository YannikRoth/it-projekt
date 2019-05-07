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
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import globals.ClientAction;
import globals.Globals;
import globals.ResourceMapType;
import globals.ResourceType;
import globals.ServerAction;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import server.ServiceLocator;
import server.model.ServerModel;
import server.model.gameplay.Card;
import server.model.gameplay.Player;
import server.model.gameplay.ResourceMap;
import server.model.init.CardLoader;
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
	//private ServerModel model;

	private static final Logger logger = ServiceLocator.getLogger();

	public ClientModel() {
		refreshOtherPlayer(new Player("David"));
		player = new Player("Mein Spieler");
//		player.getResources().put(ResourceType.BRICK, 20);
//		player.getResources().put(ResourceType.FABRIC, 25);
//		player.getResources().put(ResourceType.ORE, 230);
//		player.getResources().put(ResourceType.GLAS, 230);
//		player.getResources().put(ResourceType.PAPYRUS, 230);
//		player.getResources().put(ResourceType.STONE, 230);
//		player.getResources().put(ResourceType.WOOD, 230);

//		this.playCard(this.getMyPlayer().getPlayableCards().get(0), ClientAction.PLAYCARD);
//		Card c1 = model.getCard(1); //brick OR stone OR ore OR wood
//		player.playCard(c1);
//		System.out.println(model.getCard(1));
		
		//ServerModel model = new ServerModel();
		Map<Integer, Card> cards = CardLoader.importCards();
		this.getMyPlayer().playCard(cards.get(19));
		this.getMyPlayer().playCard(cards.get(18));
		this.getMyPlayer().playCard(cards.get(1));
		Boolean f  = this.getMyPlayer().playCard(cards.get(7));
		System.out.println(f);
		//this.sendPlayedCard(this.getMyPlayer().getPlayedCards().get(0), ClientAction.PLAYCARD);
	}
	
	
	/**
	 * Sends the played card to the server. It DOES NOT effectively plax the card on the player obj. To do so, use <code>getMyPlayer().playCard(Card c)</code>
	 * @param playedcard
	 * @param action
	 */
	public void sendPlayedCard(Card playedcard, ClientAction action) {
		try {
			
			objOutputStream.writeObject(action);
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
		ServerAction action;

		try (Socket socket = new Socket(Globals.getDefaultIPAddr(), Globals.getPortNr())) {
			this.objOutputStream = new ObjectOutputStream(socket.getOutputStream());
			this.objInputStream = new ObjectInputStream(socket.getInputStream());
			
			while (((action = (ServerAction) objInputStream.readObject()) != null)) {
				switch (action) {
				case ESTABLISED:
					numberofPlayers = (Integer) objInputStream.readObject();
					synchronized(objInputStream) {
						player = (Player) objInputStream.readObject();
					}
					logger.info("Connection and Game opened with "+numberofPlayers+" Players");
					break;
					
				case UPDATEVIEW:
					Player tempplayer = null;
					synchronized(objInputStream) {
						player = (Player) objInputStream.readObject();
					}
					logger.info("Own Player Object "+player.getPlayerName()+" received from Server");
					otherPlayers.clear();
					synchronized(otherPlayers) {
						for (int i = 0; i < numberofPlayers - 1; i++) {
							tempplayer = (Player) objInputStream.readObject();
							otherPlayers.add(tempplayer);
							logger.info("Opponent player "+tempplayer.getPlayerName()+" received from Server");						
						}
					}
					setneigbours();
					//refreshOtherPlayer();
					break;

				case INFORMATION:				
					break;
				case ENDGAME:				
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

	private void setneigbours() {
		//iterrate through all players
			player.setRightPlayer(otherPlayers.get(0));
			player.setLeftPlayer(otherPlayers.get(otherPlayers.size()-1));
			
			for (int p = 0; p < otherPlayers.size(); p++) {
				if(p==0) {
					otherPlayers.get(0).setLeftPlayer(player);
					otherPlayers.get(p).setRightPlayer(otherPlayers.get(p+1));	
				}else if (p==otherPlayers.size()-1) {
					otherPlayers.get(otherPlayers.size()-1).setRightPlayer(player);	
					otherPlayers.get(p).setLeftPlayer(otherPlayers.get(p-1));
				}else {
					otherPlayers.get(p).setLeftPlayer(otherPlayers.get(p-1));
					otherPlayers.get(p).setRightPlayer(otherPlayers.get(p+1));	
				}
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
	/**
	 * @author Roman Leuenberger
	 * @return
	 * This method returns a Map with all playable cards and possible play option for each card
	 */
	public Map<Card, Map<String, Boolean>> getPlayOptionsOfCards() {
		Map<Card, Map<String,Boolean>> cardsWithOptions = new HashMap<Card, Map<String, Boolean>>();
		for (Card c : player.getPlayableCards()) {
			Map<String, Boolean> optionValues = new HashMap<String, Boolean>();
			optionValues.put("canBuildCard", false);
			optionValues.put("canBuildWorldWonder", false);
			optionValues.put("canLayDownCard", true);
			if (player.isAbleToAffordCard(c)) {
				
			}
			cardsWithOptions.put(c, optionValues);
			//TODO evaluate possible options and change booleans
		}
		return cardsWithOptions;
	}

	public Player getMyPlayer() {
		return player;
	}

	public void setMyPlayer(Player player) {
		this.player = player;
	}
}
