package client.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import server.model.gameplay.Player;

public class LobbyModel {
	private ObservableList<Player> lobbyPlayerData;
	
	public LobbyModel() {
		lobbyPlayerData = FXCollections.observableArrayList();
	}
	
	public ObservableList<Player> getLobbyPlayerData() {
		return this.lobbyPlayerData;
	}
}
