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
import globals.message.ClientPlayerName;
import server.ServiceLocator;
import server.model.ServerModel;
import server.model.gameplay.Card;
import server.model.gameplay.Player;

/**
 * This Class Manages the Communication between one Client (Player) and the Gameplayserver.
 * Each Client will have one running ServerClientThread. 
 * @author martin
 *
 */
public class ServerClientThread extends Thread {
	private Socket socket;
	private ServerModel servermodel;
	private boolean stop;
	private Player player;
	private final Logger logger = ServiceLocator.getLogger();
	private ObjectInputStream objInputStream;
	private ObjectOutputStream objOutputStream;
	/**
	 * This method creates a new Thread for the Client and also creates the Playerobject with the incoming Name from the Client
	 * @author martin
	 * 
	 */
	public ServerClientThread(Socket socket, ServerModel model) {
		// When the game starts the player object and numbers of players will be sent to
		// the client
		String name = "";
		try {
			objOutputStream = new ObjectOutputStream(socket.getOutputStream());
			objInputStream = new ObjectInputStream(socket.getInputStream());
			ClientPlayerName cpn = (ClientPlayerName) objInputStream.readObject();
			name = cpn.getName();

			if (name == "")
				name = "Player " + ServiceLocator.getmanualCardId();

			synchronized (model.getConnectedPlayerList()) {
				ArrayList<String> playerNames = new ArrayList<>();
				for (Player p : model.getConnectedPlayerList()) {
					playerNames.add(p.getPlayerName());
				}
				if (playerNames.contains(name)) {
					int i = 2;
					while (playerNames.contains(name + " " + i)) {
						++i;
					}
					name = name + " " + i;
				}

				player = new Player(name);
				model.getConnectedPlayerList().add(player);
				servermodel = model;
				player.setBoard(servermodel.getBoard(7));
				this.setSocket(socket);
			}
		} catch (IOException e) {
			logger.info("Error occured during communication with client");
		} catch (ClassNotFoundException e) {
			logger.warning(e.getLocalizedMessage());
		}
	}
	/**
	 * This method tells the Client that the Connection was successfully established and tells him how many players the game will have.
	 * @author martin
	 * 
	 */
	public synchronized void establishconnection() {

		// this sends the own player obj to the client and tells him the game is
		// established
		// =================================================================
		try {
			objOutputStream.writeObject(ServerAction.ESTABLISHED);
			objOutputStream.writeObject(new Integer(servermodel.getNumberOfPlayers()));
			objOutputStream.writeObject(this.player);
			objOutputStream.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * This method will inform the Client if a new player joined a game so he can add the player in the List of the lobby
	 * @author martin
	 * 
	 */
	public synchronized void sendUpdateOfPlayers() {
		// this updates the client with the list of players currently waiting in lobby
		// =================================================================
		try {
			objOutputStream.writeObject(ServerAction.INFORMATION);
			objOutputStream.reset();
			objOutputStream.writeObject(new Integer(servermodel.getConnectedPlayerList().size()));
			objOutputStream.flush();
			for (Player players : servermodel.getConnectedPlayerList()) {
				objOutputStream.reset();
				objOutputStream.writeObject(players);
				objOutputStream.flush();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * This is the run method of the Communication, this is split into several subprocesses:
	 * STARTGAME will tell the Client to start its Gameview after that the game will enter a loop that will stop when the game ends.
	 * UPDATEVIEW the Server will send all PlayerObjects to the Client so he can update his current GameView with the newest PlayerObjects thus newest Gamestate.
	 * After Sending all Objects the thread will wait until the Client sends a Card with a desired action this will be forwarded to the Servermodel to Execute it.
	 * ENDGAME the thread checks each loop whether the game has ended if this is the Case it will inform the Client and leave the loop.
	 * After the Cycle the Thread will tell the Servermodel to update its gamestatus.
	 * @author martin
	 * 
	 */
	@Override
	public void run() {
		// tells the client to start its gameview
		// =================================================================
		try {
			objOutputStream.reset();
			objOutputStream.writeObject(ServerAction.STARTGAME);
			objOutputStream.flush();
			OutputAllplayers(this.player);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		// this will iterrate through all the client and server actions
		// =================================================================
		ClientAction action;
		boolean legalaction = true;
		Card cardplayed = null;
		stop = false;
		while (!stop) {
			try {

				while (servermodel.counter != 0) {
					// wait for model to allow thread to continue
					logger.info("waiting...");
					try {
						ServerClientThread.sleep(1000);
					} catch (InterruptedException e) {
						logger.warning("waiting loop: " + e.getMessage());
					}
				}

				// if the game ends the client of the thread will be notified with the winners
				// and the thread stops
				// =========================================================
				if (servermodel.gameEnd) {
					// notify the client that updated player objects will be sent
					synchronized (objOutputStream) {
						objOutputStream.writeObject(ServerAction.UPDATEVIEW);
						objOutputStream.flush();
					}
					// send all playerobjects to client
					OutputAllplayers(this.player);
					
					// this will notify the client that the game has ended and will send him the rankings
					// =================================================================
					synchronized (objOutputStream) {
						objOutputStream.reset();
						objOutputStream.writeObject(ServerAction.ENDGAME);
						objOutputStream.flush();
						for (Player currentplayer : servermodel.getWinners()) {
							objOutputStream.reset();
							objOutputStream.writeObject(currentplayer);
							objOutputStream.flush();
							logger.info(currentplayer.getPlayerName() + " sent to " + getPlayer().getPlayerName()
							+ " for winnerevaluation.");
						}
						
					}
					//stopping the game after this method
					stop = true;
				} else {

					// notify the client that updated player objects will be sent
					synchronized (objOutputStream) {
						objOutputStream.writeObject(ServerAction.UPDATEVIEW);
						objOutputStream.flush();
					}
					// send all playerobjects to client
					OutputAllplayers(this.player);

					do {
						// get the action the client wishes to do with the incoming card
						action = (ClientAction) objInputStream.readObject();
						switch (action) {
						case PLAYCARD:
							synchronized (objInputStream) {
								//Client wants to play the incoming card 
								cardplayed = (Card) objInputStream.readObject();
								boolean check = this.player.playCard(cardplayed);
								if(check == false) {
									logger.warning("PLAYCARD: card could not be played, maybe a consistency problem?");
									//legalaction = false;
								}
							}
							logger.info(cardplayed.getCardName() + " Cards received from " + player.getPlayerName()
									+ " with following Action: " + action);
							break;

						case DISCARD:
							synchronized (objInputStream) {
								//Client wants to discard the incoming card 
								cardplayed = (Card) objInputStream.readObject();
								this.player.discardCard(cardplayed);
							}
							logger.info(cardplayed.getCardName() + "Cards received from " + player.getPlayerName()
									+ " with following Action: " + action);
							
							break;
						case BUILDWONDER:
							synchronized (objInputStream) {
								//Client wants to build a wonder with the incoming card 
								cardplayed = (Card) objInputStream.readObject();
//								boolean check = this.player.playWorldWonder(this.player.getBoard().getNextWorldWonderStage());
								boolean check = this.player.playWorldWonder(this.player.getBoard().getNextWorldWonderStage(this.player));
								this.player.removeCardFromCurrentPlayabled(cardplayed);
								if(check == false) {
									//legalaction = false;
									logger.warning("BUILDWONDER: card could not be played, maybe a consistency problem?");
								}
							}
							logger.info(cardplayed.getCardName() + "Cards received from " + player.getPlayerName()
									+ " with following Action: " + action);

							break;
						default:
							logger.info("An error occured during the Communication - invalid input from "
									+ player.getPlayerName() + " ----retry");
							legalaction = false;
							break;
						}
						//This loop will continue as long as the player sends illegal actions or cards (should not be possible)
					} while (!legalaction);

					// this method is used to Update the current game status
					// =========================================================
					servermodel.updateGameStatus();
				}

			} catch (IOException e) {
				stop = true;
				logger.info(e.getLocalizedMessage());
			} catch (ClassNotFoundException e) {
				stop = true;
				logger.info(e.getLocalizedMessage());
			}
		}
	}
	/**
	 * This method outputs all players going right round
	 * @author martin
	 * 
	 */

	public synchronized void OutputAllplayers(Player curplayer) throws IOException {
		//Outputs all right players starting with the own player of this clientthread
		synchronized (objOutputStream) {
			objOutputStream.reset();
			objOutputStream.writeObject(curplayer);
			objOutputStream.flush();
		}
		logger.info("Player " + curplayer.getPlayerName() + " sent to " + player.getPlayerName());
		if (curplayer.getRightPlayer() != player) {
			OutputAllplayers(curplayer.getRightPlayer());
		}
	}

	public Player getPlayer() {
		return player;
	}

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

}
