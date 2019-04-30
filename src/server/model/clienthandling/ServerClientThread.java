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

	// TODO ObjectInput/Output reader instead of inputstreamreader

	public ServerClientThread(Socket socket, ServerModel model) {
		// TODO: Add player name in constructor
		player = new Player("");
		servermodel = model;
		ArrayList<Card> Cards = new ArrayList<>();
		Cards.add(model.getCard(1));
		Cards.add(model.getCard(5));
		Cards.add(model.getCard(10));
		Cards.add(model.getCard(11));
		Cards.add(model.getCard(16));
		Cards.add(model.getCard(19));
		player.setPlayerName("Martin");
		logger.info(model.getCard(19) + " Card  added to player");
		player.updateCardset(Cards);
		this.socket = socket;

	}

	@Override
	public void run() {
		Card cardplayed;
		// TODO Kommunikation zwischen Clientgerät, Clientthread und Gamehandling
		stop = false;
		int i = 0;
		testclassserializable testt;
		logger.info(socket.toString());
		try {

			objOutputStream = new ObjectOutputStream(this.socket.getOutputStream());
			objInputStream = new ObjectInputStream(this.socket.getInputStream());

		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.info("Error occured durring communication with client");
		}
		while (true) {
			try {
				i++;
				objOutputStream.writeObject(player);
				objOutputStream.flush();
				logger.info("Player "+player.getPlayerName()+" sent to Client --- attempt " + i);
				
				cardplayed = (Card) objInputStream.readObject();
				logger.info(cardplayed.getCardName() + "Cards received from Client --- attempt"+ i);
			} catch (IOException e) {
				logger.info( e.getLocalizedMessage());
			} catch (ClassNotFoundException e) {
				logger.info(e.getLocalizedMessage());
			}
		}

	}

	public Player getPlayer() {
		return player;
	}

}
