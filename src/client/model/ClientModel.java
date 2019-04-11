package client.model;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;


import server.model.clienthandling.ServerClientThread;

public class ClientModel extends Thread {

public static void main(String[] args) throws IOException {
	
	
//		try (Socket s = new Socket("LOCALHOST", 8080);){
//		
//			OutputStreamWriter ows = new OutputStreamWriter(s.getOutputStream());
//			ows.write("Hello");
//			ows.flush();
//			
//		}	catch (Exception err) {
//	
//			}
	

		Socket s = new Socket("LOCALHOST", 8080);
		ServerClientThread sct = new ServerClientThread(s);
		sct.run();
}
	
}
