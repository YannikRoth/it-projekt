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
public class ServerClientThread extends Thread implements Serializable{
	private Socket socket;
	private ServerModel servermodel;
	private boolean stop;
	private Player player;
	private final Logger logger = ServiceLocator.getLogger();
	private ObjectInputStream objInputStream;
	private ObjectOutputStream objOutputStream;
	
	//TODO ObjectInput/Output reader instead of inputstreamreader
	
	public ServerClientThread(Socket socket, ServerModel model) {
		//TODO: Add player name in constructor
		player = new Player("");
		servermodel = model;
		ArrayList<Card> Cards = new ArrayList<>();
		Cards.add(model.getCard(3));
		Cards.add(model.getCard(5));
		Cards.add(model.getCard(10));
		Cards.add(model.getCard(11));
		Cards.add(model.getCard(16));
		Cards.add(model.getCard(19));
		logger.info(model.getCard(19)+" Card  added to player");
		player.updateCardset(Cards);
		this.socket = socket;
		try {
			
			this.objOutputStream = new ObjectOutputStream(this.socket.getOutputStream());
			this.objInputStream = new ObjectInputStream(this.socket.getInputStream());
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Override
	public void run() {
		Card cardplayed;
		// TODO Kommunikation zwischen Clientgerät, Clientthread und Gamehandling
		stop = false;
		logger.info(socket.toString());
		while(!stop) {
			try {
				logger.info(socket.toString());
				testclassserializable testt;
				testt = (testclassserializable) objInputStream.readObject();
				objOutputStream.writeObject(testt);
				objOutputStream.flush();
				logger.info("Players sent to Client");
//				cardplayed = (Card) objInputStream.readObject();
//				logger.info(cardplayed+"Cards received from Client");
			} catch (IOException | ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				logger.info("Error occured durring communication with client");
				stop = true;
			}

		}
	}
	
	public Player getPlayer() {
		return player;
	}
	
}
