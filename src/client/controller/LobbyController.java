package client.controller;

import client.ClientMVC;
import client.model.LobbyModel;
import client.view.LobbyView;
import javafx.application.Platform;

public class LobbyController {

	private LobbyModel model;
	private LobbyView view;
	
	public LobbyController(LobbyModel model, LobbyView view) {
		this.model = model;
		this.view = view;
		
		processQuitButton();
		processNewGameButton();
	}
	
	public void processQuitButton() {
		view.getQuitButton().setOnAction((e) -> {
			Platform.exit();
			System.exit(0);
		});
	}
	
	public void processNewGameButton() {
		view.getNewGameButton().setOnAction((e) -> {
			ClientMVC newGame = new ClientMVC();
//			this.view.stop();
		});
	}

}
