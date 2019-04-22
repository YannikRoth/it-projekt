package server.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import server.ServiceLocator;
import server.model.clienthandling.ServerClientThread;
import server.model.clienthandling.ServerRequestHandler;
import server.model.gameplay.Board;
import server.model.gameplay.Card;
import server.model.gameplay.Player;
import server.model.init.BoardLoader;
import server.model.init.CardLoader;

public class ServerModel {
	
	private Logger logger = ServiceLocator.getLogger();
	
	private Map<Player, ServerClientThread> players = new HashMap<>();
	private ServerRequestHandler requesthandler;
	
	//cards and boards are imported when class is initialized
	//KEY = Item ID
	// Value = Card or Board Object
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
		
		//open server request handler
		this.requesthandler = new ServerRequestHandler(this);
		requesthandler.start();
		
		//further code to follow
	}
	
	public void addCardToMap(Card c) {
		this.cards.put(c.getId(), c);
	}
	
	public void addBoardToMap(Board b) {
		this.boards.put(b.getId(), b);
	}
	
	public Card getCard(int cardId) {
		return this.cards.get(cardId);
	}

	public Map<Integer, Card> getCards() {
		return cards;
	}
	
	public void addClient(ServerClientThread client) {
		// TODO Bedingungen wenn neuer Client erlaubt ist und wann nicht
		if(true) {
			//clients.add(client);
			client.start();
			players.put(client.getPlayer(), client);
			logger.info("successfully added client");
		}else {
			logger.info("client could not be added");
		}
		
		if(players.size() >= 3) {
			startGame();
		}
	}

	private void startGame() {
		//send card set to all clients; iterate through player map
		//wait for answer of each client
		//Map<Card, ArrayList<String>> cardSet = new HashMap<>();
		
		/*
		 * Idea of martin:
		 * Send entire player object to client. Client then iterates through all cards and possible actions based on 
		 * "isAbleToAffordCard" are evaluated.
		 * Back to server, only the cardId is sent and the server updates the effectiv player-obj. The new player-onj is then sent 
		 * back to the client
		 */
		
		
	}

	public Board getBoard(int i) {
		return this.boards.get(i);
	}
	
}
