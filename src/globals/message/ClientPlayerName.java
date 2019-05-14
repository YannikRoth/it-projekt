package globals.message;

import java.io.Serializable;
/**
 * Init message from client to server to send playerName preference
 * @author david
 *
 */
public class ClientPlayerName implements Serializable{
	private String name = "";
	public ClientPlayerName(String name) {
		this.setName(name);
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

}
