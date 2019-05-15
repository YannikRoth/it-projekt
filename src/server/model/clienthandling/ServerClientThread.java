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
import test.testclassserializable;

/**
 * 
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

	public synchronized void establishconnection() {

		// this sends the own player obj to the server and tells him the game is
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

	@Override
	public void run() {
		// tells the client to start its gameview
		// =================================================================
		try {
			objOutputStream.reset();
			objOutputStream.writeObject(ServerAction.STARTGAME);
			objOutputStream.flush();
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
						//this.sleep(1000);
						ServerClientThread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				// if the game ends the client of the thread will be notified with the winners
				// and the thread stops
				// =========================================================
				if (servermodel.gameEnd) {
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
								cardplayed = (Card) objInputStream.readObject();
								boolean check = this.player.playCard(cardplayed);
								if(check == false) {
									logger.warning("card could not be played, maybe a consistency problem?");
								}
							}
							logger.info(cardplayed.getCardName() + " Cards received from " + player.getPlayerName()
									+ " with following Action: " + action);
							break;

						case DISCARD:
							synchronized (objInputStream) {
								cardplayed = (Card) objInputStream.readObject();
								this.player.discardCard(cardplayed);
							}
							logger.info(cardplayed.getCardName() + "Cards received from " + player.getPlayerName()
									+ " with following Action: " + action);
							break;
						case BUILDWONDER:
							synchronized (objInputStream) {
								cardplayed = (Card) objInputStream.readObject();
								boolean check = this.player.playWorldWonder(this.player.getBoard().getNextWorldWonderStage());
								this.player.removeCardFromCurrentPlayabled(cardplayed);
								if(check == false) {
									logger.warning("card could not be played, maybe a consistency problem?");
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

	public void OutputAllplayers(Player curplayer) throws IOException {
		// System.out.println("Sending players " + curplayer.getPlayerName() + " " +
		// curplayer.getPlayableCards());
		// System.out.println("Resources" + curplayer.getResources());
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
