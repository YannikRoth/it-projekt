package client.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
	
	public ClientModel() {
		//TODO: for example, to be remove
		refreshOtherPlayer(new Player("David"));
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
	
		try (Socket socket = new Socket(msg, 8080)) {
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
