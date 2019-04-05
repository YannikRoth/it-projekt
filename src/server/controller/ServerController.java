package server.controller;

import java.util.Optional;

import javafx.scene.control.TextInputDialog;
import server.model.ServerModel;
import server.model.gameplay.ServerAction;
import server.view.ServerView;

public class ServerController{
	private ServerModel model;
	private ServerView view;

	public ServerController(ServerModel model, ServerView view) {
		this.model = model;
		this.view = view;
		
		AddViewButtonListeners();
	}

	/**
	 * Add listeners to the buttons in view
	 * @author david
	 * 
	 */
	private void AddViewButtonListeners() {
		view.getButtonChangePort().setOnAction((event) -> {
			TextInputDialog dialog = new TextInputDialog();
			dialog.setTitle("Change Port");
			dialog.setHeaderText("Choose new server port:");
			
			Optional<String> result = dialog.showAndWait();
			result.ifPresent(name -> {
				//TODO: Notify Clients and Change ServerSocket Port (I think it's a model task...)
				view.serverActionData.add(new ServerAction("localhost", "Server", "Port changed to: " + name));
			});
		});
		
		view.getButtonRestartServer().setOnAction((event) -> {
			//TODO: Restart ServerSocket
			view.serverActionData.add(new ServerAction("localhost", "Server", "Server restarted"));
		});
	}
}
