package server.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import server.ServiceLocator;
import server.model.gameplay.Board;
import server.model.gameplay.Card;
import server.model.init.BoardLoader;
import server.model.init.CardLoader;

public class ServerModel {
	
	private Logger logger = ServiceLocator.getLogger();
	
	private ArrayList<ServerClientThread> Clients = new ArrayList<ServerClientThread>();
	
	//host cardset, are added whiel imported
	private Map<Integer, Card> cards;
	private Map<Integer, Board> boards;
	
	public ServerModel() {
		//load masterdata and store them into class maps
		this.cards = new HashMap<>();
		CardLoader.importCards(this);
		logger.info(this.cards.size() + " cards have been sucessfully imported");
		this.boards = new HashMap<>();
		BoardLoader.importBoards(this);
		logger.info(this.boards.size() + " boards have been sucessfully imported");
		
	}
	
	public void addCardToMap(Card c) {
		this.cards.put(c.getId(), c);
	}
	
	public void addBoardToMap(Board b) {
		this.boards.put(b.getId(), b);
	}
	
	public void addClient(ServerClientThread Client) {
		//TODO Bedingungen wenn neuer Client erlaubt ist und wann nicht
		if(true) {
			Clients.add(Client);
		}
	}
}
