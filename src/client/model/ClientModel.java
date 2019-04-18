package client.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;
import java.util.logging.Logger;

import globals.Globals;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import server.ServiceLocator;
import server.model.gameplay.Player;
import server.model.gameplay.ServerAction;
/**
 * 
 * @author Phillip
 *
 */

public class ClientModel extends Thread {

	private BufferedReader socketIn;
	private OutputStreamWriter socketOut;
	private ObservableList<Player> otherPlayers = FXCollections.observableArrayList();
	
	//object streams are requried to communicate with the server
	private Socket clientConnection;
	private ObjectInputStream objInputStream;
	private ObjectOutputStream objOutputStream;
	
	private static final Logger logger = ServiceLocator.getLogger();
	
	public ClientModel() {
		//TODO: for example, to be remove
		refreshOtherPlayer(new Player("David"));
		connectToServer();
	}
	
	/**
	 * @autor yannik roth
	 */
	private void connectToServer() {
		logger.info("Trying to connect to server");
		try(Socket socket = new Socket(Globals.getDefaultIPAddr(), Globals.getPortNr())){
			this.clientConnection = socket;
			this.objInputStream = new ObjectInputStream(socket.getInputStream());
			this.objOutputStream = new ObjectOutputStream(socket.getOutputStream());
			
		}catch(Exception e) {
			logger.info("An error occured while connecting to the server" + e.getStackTrace());
		}
		
	}

//	public static void main(String[] args) {
//		ClientModel client = new ClientModel();
//		client.connect();
//	}
	
	private void connect() {
		// TODO Kommunikation mit dem Server hier abhandeln
		Scanner in = new Scanner(System.in);
		System.out.println("Enter IP address of server");
		String msg = in.nextLine();
	
		try (Socket socket = new Socket(msg, Globals.getPortNr())) {
			socketOut = new OutputStreamWriter(socket.getOutputStream());
			socketIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			this.start();
			System.out.println("Enter a chat message, or 'stop' to quit");
			while (!msg.equals("stop")) {
				msg = in.nextLine();
				socketOut.write(msg + "\n");
				socketOut.flush();
			}
		} catch (Exception e) {
			
		}
		in.close();
	}
	
	@Override
	public void run() {
		try {
			String message = "";
			while (message != null) {
				message = socketIn.readLine();
				System.out.println(message);
			}
		} catch (IOException e) {
		}
	}
	
	public ObservableList<Player> getOtherPlayers() {
		return otherPlayers;
	}

	/**
	 * add or refreshs the param player in otherplayer list
	 * @param p
	 * @author david
	 */
	public void refreshOtherPlayer(Player p) {
		if(!getOtherPlayers().contains(p))
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
}
