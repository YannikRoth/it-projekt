package server.model.gameplay;

import java.text.SimpleDateFormat;
import java.util.Date;

import javafx.beans.property.SimpleStringProperty;

/**
 * Class to handle some actions on the server, listed in server view
 * @author david
 *
 */

public class ServerActionLog {
	private static final SimpleDateFormat tsformat = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");
	private final SimpleStringProperty timestamp;
    private final SimpleStringProperty ipAdress;
    private final SimpleStringProperty userName;
    private final SimpleStringProperty action;
 
    public ServerActionLog(String ipAdress, String playerName, String action) {
    	this.timestamp = new SimpleStringProperty(tsformat.format(new Date().getTime()));
        this.ipAdress = new SimpleStringProperty(ipAdress);
        this.userName = new SimpleStringProperty(playerName);
        this.action = new SimpleStringProperty(action);
    }
 
    public String getTimestamp() {
    	return timestamp.get();
    }
    public void setTimestamp(String timestamp) {
    	this.timestamp.set(timestamp);
    }
    
    public String getIpAdress() {
        return ipAdress.get();
    }
    public void setIpAdress(String ipAdress) {
    	this.ipAdress.set(ipAdress);
    }
        
    public String getUserName() {
        return userName.get();
    }
    public void setUserName(String userName) {
    	this.userName.set(userName);
    }
    
    public String getAction() {
        return action.get();
    }
    public void setAction(String action) {
        this.action.set(action);
    }
}
