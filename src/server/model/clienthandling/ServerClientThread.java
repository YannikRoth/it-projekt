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
	private BufferedReader inputmessage;
	private PrintWriter outputmessage;
	private boolean start;

	// TODO ObjectInput/Output reader instead of inputstreamreader

	public ServerClientThread(Socket socket, ServerModel model) {
		// TODO: Add player name in constructor
		player = new Player("");
		servermodel = model;
		start = false;
		ArrayList<Card> Cards = new ArrayList<>();
		Cards.add(model.getCard(1));
		Cards.add(model.getCard(5));
		Cards.add(model.getCard(10));
		Cards.add(model.getCard(11));
		Cards.add(model.getCard(16));
		Cards.add(model.getCard(19));
		player.setPlayerName("Martin_" + ServiceLocator.getmanualCardId());
		player.updateCardset(Cards);
		this.socket = socket;
		try {

			objOutputStream = new ObjectOutputStream(this.socket.getOutputStream());
			objInputStream = new ObjectInputStream(this.socket.getInputStream());
			//used to know what the server wants
			this.inputmessage = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			//used to inform the server what we wanna do
			this.outputmessage = new PrintWriter(socket.getOutputStream()); 
			outputmessage.println("connectionestablished");
			outputmessage.flush();
			outputmessage.println("3");
			outputmessage.flush();			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.info("Error occured durring communication with client");
		}
	}

	@Override
	public void run() {
		logger.info("test1");
		String action;
		Card cardplayed;
		stop = false;
		while (!stop) {
			try {
				logger.info("test2");
				outputmessage.println("updateview");
				outputmessage.flush();
				OutputAllplayers(player);
				logger.info("test3");
				action = inputmessage.readLine();
				switch (action) {
				case "playcard":
					cardplayed = (Card) objInputStream.readObject();
					logger.info(
							cardplayed.getCardName() + "Cards received from Client with following Action: " + action);
					break;

				case "discard":
					cardplayed = (Card) objInputStream.readObject();
					logger.info(
							cardplayed.getCardName() + "Cards received from Client with following Action: " + action);
					break;
				default:
					logger.info("An error occured during the Communication - invalid input from the Client");
					break;
				}
			} catch (IOException e) {
				logger.info(e.getLocalizedMessage());
			} catch (ClassNotFoundException e) {
				logger.info(e.getLocalizedMessage());
			}
		}

	}
	
	public void OutputAllplayers(Player curplayer) throws IOException {
		
		objOutputStream.writeObject(curplayer);
		objOutputStream.flush();
		logger.info("Player "+curplayer.getPlayerName()+" sent to Client");	
		//iterrate through all players
		if (curplayer.getRightPlayer() != player) {
			logger.info("Player "+player.getRightPlayer().getPlayerName()+" test");
			OutputAllplayers(player.getRightPlayer());
				
		}
	}

	public Player getPlayer() {
		return player;
	}


}
