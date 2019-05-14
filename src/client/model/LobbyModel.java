package client.model;

import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import server.model.gameplay.Player;

public class LobbyModel {
	private ObservableList<Player> lobbyPlayerData;
	
	public LobbyModel() {
		lobbyPlayerData = FXCollections.observableArrayList();
	}
	
	public void setLobbyPlayerData(ArrayList<Player> players) {
		lobbyPlayerData.clear();
		lobbyPlayerData.addAll(players);

	}
	
	public ObservableList<Player> getLobbyPlayerData() {
		return this.lobbyPlayerData;
	}
}
