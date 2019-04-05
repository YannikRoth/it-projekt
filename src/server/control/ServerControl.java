package server.control;

import server.model.ServerModel;
import server.view.ServerView;

public class ServerControl {
	
	private ServerModel model;
	private ServerView view;
	
	public ServerControl(ServerModel model, ServerView view) {
		this.model = model;
		this.view = view;
	}

}
