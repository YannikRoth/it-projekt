package client.model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import client.ServicelocatorClient;
import globals.ClientAction;
import globals.Globals;
import globals.ServerAction;
import globals.message.ClientPlayerName;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import server.ServiceLocator;
import server.model.gameplay.Card;
import server.model.gameplay.Player;

/**
 * 
 * @author martin
 *
 */

public class ClientModel extends Thread {

	private Player player;
	public ObservableList<Card> Cards = FXCollections.observableArrayList();
	private ObservableList<Player> otherPlayers = FXCollections.observableArrayList();
	private int numberofPlayers;
	private ObjectInputStream objInputStream;
	private ObjectOutputStream objOutputStream;
	private String inputPlayerName = "";

	private static final Logger logger = ServiceLocator.getLogger();

	public ClientModel() {
		super();
		this.setInputPlayerName(System.getProperty("user.name"));
	}

	public ClientModel(String name) {
		super();
		this.setInputPlayerName(name);
	}

	/**
	 * Sends the played card to the server. It DOES NOT effectively play the card on
	 * the player obj. To do so, use <code>getMyPlayer().playCard(Card c)</code>
	 * 
	 * @param playedcard
	 * @param action
	 */
	public void sendPlayedCard(Card playedcard, ClientAction action) {
		//sends the desired card and action to the server
		try {

			objOutputStream.writeObject(action);
			objOutputStream.writeObject(playedcard);
			objOutputStream.flush();
			logger.info("Card " + playedcard.getCardName() + " sent to Server - with following Action: " + action);
		} catch (IOException e) {
			logger.info("Error occured while sending card: " + e.getMessage());
		}
	}

	@Override
	public void run() {
		//Join Game
		ServerAction action;
		//uses the IP adress and port number from the lobby
		try (Socket socket = new Socket(Globals.getDefaultIPAddr(), Globals.getPortNr())) {
			this.objInputStream = new ObjectInputStream(socket.getInputStream());
			this.objOutputStream = new ObjectOutputStream(socket.getOutputStream());
			//sends the playername to the server
			ClientPlayerName cpn = new ClientPlayerName(inputPlayerName);
			objOutputStream.writeObject(cpn);
			objOutputStream.flush();
			//waiting for Server input
			while (((action = (ServerAction) objInputStream.readObject()) != null)) {
				switch (action) {
				//server notifys client the connection is established and sends its own player object
				case ESTABLISHED:
					numberofPlayers = (Integer) objInputStream.readObject();
					synchronized(objInputStream) {
						setMyPlayer((Player) objInputStream.readObject());
					}
					logger.info("Connection and Game opened with "+numberofPlayers+" Players");
					break;
				//Server wants the client to update his views with the new player objects	
				case UPDATEVIEW:
					Player tempplayer = null;
					//update own player object first
					synchronized(objInputStream) {
						setMyPlayer((Player) objInputStream.readObject());
					}
					logger.info("Own Player Object "+getMyPlayer().getPlayerName()+" received from Server");
					otherPlayers.clear();
					//update all other players
					synchronized(otherPlayers) {
						for (int i = 0; i < numberofPlayers - 1; i++) {
							tempplayer = (Player) objInputStream.readObject();
							otherPlayers.add(tempplayer);
							logger.info("Opponent player "+tempplayer.getPlayerName()+" received from Server");						
						}
					}
					//set neigbours manually on client side as references will be invalid
					setneigbours();
					//update the view
					Cards.setAll(getMyPlayer().getPlayableCards());
					Platform.runLater(new Runnable() {
						@Override
						public void run() {
							System.out.println("View ref: " + ServicelocatorClient.getClientView());
									ServicelocatorClient.getClientView().updatePlayableCardView();
									ServicelocatorClient.getClientView().refreshAgeLabelFromModel();
									ServicelocatorClient.getClientController().processClickOnImage();
						
						}
					});
					
					break;

				case INFORMATION:
					//receive the players already in the game of the server
					Integer numberofPlayersConnected = 0;
					numberofPlayersConnected = (Integer) objInputStream.readObject();
				    ArrayList<Player> ListOfWaitingPlayers = new ArrayList<>();
				    Player tempplayerr= null;
					synchronized(ListOfWaitingPlayers) {
						for (int i = 0; i < numberofPlayersConnected; i++) {
							tempplayerr = (Player) objInputStream.readObject();
							ListOfWaitingPlayers.add(tempplayerr);
							logger.info("Waiting player "+  tempplayerr + " received from Server");						
						}
					}
					ServicelocatorClient.getLobbyModel().setLobbyPlayerData(ListOfWaitingPlayers);
					break;
					
				case STARTGAME:		
					//start the new game view 
					Platform.runLater(new Runnable() {
						@Override
						public void run() {
							ServicelocatorClient.getLobbyController().processNewGame();
						}
					});
					break;					
				case ENDGAME:	
					//update the client with the winnerlist
				    ArrayList<Player> winnerList = new ArrayList<>();
				    Player tempplayer1 = null;
					synchronized(winnerList) {
						for (int i = 0; i < numberofPlayers; i++) {
							tempplayer1 = (Player) objInputStream.readObject();
							winnerList.add(tempplayer1);
							logger.info("Number "+ i + tempplayer1.getPlayerName()+" received from Server");						
						}
					}				    
					Platform.runLater(new Runnable() {
						@Override
						public void run() {
							ServicelocatorClient.getClientView().updateClientViewEndGame(winnerList);
							ServicelocatorClient.getClientController().updateClientViewEndGame();
						}
					});
					
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
		// iterrate through all players
		getMyPlayer().setRightPlayer(otherPlayers.get(0));
		getMyPlayer().setLeftPlayer(otherPlayers.get(otherPlayers.size() - 1));

		for (int p = 0; p < otherPlayers.size(); p++) {
			if (p == 0) {
				otherPlayers.get(0).setLeftPlayer(getMyPlayer());
				otherPlayers.get(p).setRightPlayer(otherPlayers.get(p + 1));
			} else if (p == otherPlayers.size() - 1) {
				otherPlayers.get(otherPlayers.size() - 1).setRightPlayer(getMyPlayer());
				otherPlayers.get(p).setLeftPlayer(otherPlayers.get(p - 1));
			} else {
				otherPlayers.get(p).setLeftPlayer(otherPlayers.get(p - 1));
				otherPlayers.get(p).setRightPlayer(otherPlayers.get(p + 1));
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
	 * @return This method returns a Map with all playable cards and possible play
	 *         option for each card
	 */
	public Map<Card, Map<ClientAction, Boolean>> getPlayOptionsOfCards() {
		Map<Card, Map<ClientAction, Boolean>> cardsWithOptions = new HashMap<Card, Map<ClientAction, Boolean>>();
		for (Card c : getMyPlayer().getPlayableCards()) {
			Map<ClientAction, Boolean> optionValues = new HashMap<ClientAction, Boolean>();
			optionValues.put(ClientAction.PLAYCARD, false);
			optionValues.put(ClientAction.BUILDWONDER, false);
			optionValues.put(ClientAction.DISCARD, true);
			if (getMyPlayer().isAbleToAffordCard(c)) {
				optionValues.replace(ClientAction.PLAYCARD, false, true);
			}
			if (getMyPlayer().getPlayerBoard().getNextWorldWonderStage() != null) {
				if (getMyPlayer().isAbleToAffordCard(
						getMyPlayer().getPlayerBoard().getNextWorldWonderStage().getWorldWonderCard())) {
					optionValues.replace(ClientAction.BUILDWONDER, false, true);
				}
			}
			cardsWithOptions.put(c, optionValues);
		}
		return cardsWithOptions;
	}

	public Player getMyPlayer() {
		return player;
	}

	/**
	 * Set player and refresh observable maps from resources refresh tableview to
	 * show myPlayers resources
	 * 
	 * @param player
	 * @author david
	 */
	public void setMyPlayer(Player player) {
		this.player = player;
		synchronized (this.player) {
			player.getResources().refreshObservableMap();
			if(ServicelocatorClient.getClientView() != null) {
				//Liste l�schen
				ServicelocatorClient.getClientView().getTablePoints().getItems().clear();
				//Listener mit neuer Liste
				ServicelocatorClient.getClientView().getTablePoints().setItems(
						player.getResources().getResourcesListObservable());
			}
		}
	}

	public String getInputPlayerName() {
		return inputPlayerName;
	}

	public void setInputPlayerName(String inputPlayerName) {
		if (inputPlayerName == "")
			this.inputPlayerName = System.getProperty("user.name");
		else
			this.inputPlayerName = inputPlayerName;
	}
	
	public ObservableList<Card> getCards() {
		return this.Cards;
	}
	
}
