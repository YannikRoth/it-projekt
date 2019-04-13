package client.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;
/**
 * 
 * @author Phillip
 *
 */

public class ClientModel extends Thread {

	private BufferedReader socketIn;
	private OutputStreamWriter socketOut;
	
	public static void main(String[] args) {
		ClientModel client = new ClientModel();
		client.connect();
	}
	
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
	
}
