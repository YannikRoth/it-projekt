package server.model.clienthandling;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Logger;

import server.ServiceLocator;
import server.model.gameplay.Player;
/**
 * 
 * @author martin
 *
 */
public class ServerClientThread extends Thread{
	private Socket socket;
	private Player player;
	private final Logger logger = ServiceLocator.getLogger();
	
	
	public ServerClientThread(Socket socket) {
		player = new Player();
		this.socket = socket;
	}
	@Override
	public void run() {
		// TODO Kommunikation zwischen Clientgerät, Clientthread und Gamehandling
		try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				PrintWriter out = new PrintWriter(socket.getOutputStream());) {
			out.print("Verbindung erfolgreich aufgebaut");
			
			while (true) {
				
			}	
			
		} catch (IOException e) {
			logger.warning(e.toString());
		}
	}
	
	public Player getPlayer() {
		return player;
	}
	
}
