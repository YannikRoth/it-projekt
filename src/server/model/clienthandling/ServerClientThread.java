package server.model.clienthandling;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Logger;

import globals.*;
import server.ServiceLocator;
import server.model.ServerModel;
import server.model.gameplay.Card;
import server.model.gameplay.Player;
import test.testclassserializable;

/**
 * 
 * @author martin
 *
 */
public class ServerClientThread extends Thread implements Serializable {
	private Socket socket;
	private ServerModel servermodel;
	private boolean stop;
	private Player player;
	private final Logger logger = ServiceLocator.getLogger();
	private ObjectInputStream objInputStream;
	private ObjectOutputStream objOutputStream;
	private boolean start;

	// TODO ObjectInput/Output reader instead of inputstreamreader

	public ServerClientThread(Socket socket, ServerModel model) {
		// TODO: Add player name in constructor
		player = new Player("Player " + ServiceLocator.getmanualCardId());
		servermodel = model;
		start = false;
		player.setBoard(servermodel.getBoard(7));
		this.socket = socket;
		
		//When the game starts the player object and numbers of players will be sent to the client
		try {
			objOutputStream = new ObjectOutputStream(this.socket.getOutputStream());
			objInputStream = new ObjectInputStream(this.socket.getInputStream());	
		
			ServiceLocator.getServerModel().getServerActionData().add(
					new server.model.gameplay.ServerAction(socket.getInetAddress().toString(), player.getPlayerName(), "Connected"));
		} catch (IOException e) {
			logger.info("Error occured durring communication with client");
		}
	}

	@Override
	public void run() {
		ClientAction action;
		
		
		//this sends the own player obj to the server
		try {
			synchronized (objInputStream) {
				objOutputStream.writeObject(ServerAction.ESTABLISHED);
				objOutputStream.writeObject(new Integer(servermodel.getNumberOfPlayers()));
				objOutputStream.writeObject(this.player);
				objOutputStream.flush();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		boolean legalaction = true;
		Card cardplayed = null;
		stop = false;
		while (!stop) {
			try {
				
				while(servermodel.counter != 0) {
					//wait
					logger.info("waiting...");
					try {
						this.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				//notify the client that updated player objects will be sent
				synchronized(objOutputStream) {
					objOutputStream.writeObject(ServerAction.UPDATEVIEW);
					objOutputStream.flush();
				}
				//send all playerobjects to client
				OutputAllplayers(this.player);
				
				
				do {
					//get the action the client wishes to do with the incoming card
					action = (ClientAction) objInputStream.readObject();
					switch (action) {
					case PLAYCARD:
						synchronized(objInputStream) {
							cardplayed = (Card) objInputStream.readObject();
							this.player.playCard(cardplayed);
						}
						logger.info(cardplayed.getCardName() + " Cards received from "
									+ player.getPlayerName() + " with following Action: " + action);
						break;
	
					case DISCARD:
						synchronized(objInputStream) {
							cardplayed = (Card) objInputStream.readObject();
							this.player.discardCard(cardplayed);
						}
						logger.info(cardplayed.getCardName() + "Cards received from "
									+ player.getPlayerName() + " with following Action: " + action);
						break;
					case BUILDWONDER:
						synchronized(objInputStream) {
							cardplayed = (Card) objInputStream.readObject();
							this.player.playWorldWonder(this.player.getBoard().getNextWorldWonderStage());
							this.player.removeCardFromCurrentPlayabled(cardplayed);
						}
						logger.info(cardplayed.getCardName() + "Cards received from "
									+ player.getPlayerName() + " with following Action: " + action);
						//TODO function in Servermodel
						break;				
					default:
						logger.info("An error occured during the Communication - invalid input from " + player.getPlayerName()+ " ----retry");
						legalaction = false;
						break;
					}
				}while(!legalaction);
				
				//this method is used to pass the card to the next player
				servermodel.updateGameStatus();
				
				//TODO wait for Servermodel to notify the updated cardset and other players
			} catch (IOException e) {
				logger.info(e.getLocalizedMessage());
			} catch (ClassNotFoundException e) {
				logger.info(e.getLocalizedMessage());
			}
		}

	}
	
	public void OutputAllplayers(Player curplayer) throws IOException {
		System.out.println("Sending players " + curplayer.getPlayerName() + " " + curplayer.getPlayableCards());
		synchronized(objOutputStream) {
			objOutputStream.reset();
			objOutputStream.writeObject(curplayer);
			objOutputStream.flush();
		}
		logger.info("Player "+curplayer.getPlayerName()+" sent to "+player.getPlayerName());	
		if (curplayer.getRightPlayer() != player) {
			OutputAllplayers(curplayer.getRightPlayer());
		}
	}

	public Player getPlayer() {
		return player;
	}


}
