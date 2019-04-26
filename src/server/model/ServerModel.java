package server.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

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
	
	//gameplay specific variables
	//index0 = age 1; index2 = age 3
	private List<Set<Entry<Integer, Card>>> activeCards = new ArrayList<>();
	//end of gameplay specific varibales
	
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
			client.getPlayer().setBoard(boards.get(7));
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
		
		//load cards to play into ArrayList
		loadGameCards();
		assignPlayerNeighbors();
		
	}

	
	/**
	 * Assign neighbors to players
	 * @author yannik roth
	 */
	private void assignPlayerNeighbors() {
		List<Player> activePlayers = new ArrayList<>(this.players.keySet());
		int amountPlayer = activePlayers.size();
		
		//setting left players
		for(int i=0; i<=activePlayers.size(); i++) {
			//check if end of list, then add left neighbor as the first player
			if(i+1 > activePlayers.size()) {
				activePlayers.get(0).setLeftPlayer(activePlayers.get(i));
			}else {
				activePlayers.get(i).setLeftPlayer(activePlayers.get(i+1));
			}
		}
		
		//setting right players
		for(int i = 0; i>=activePlayers.size(); i--){
			//check if end of list, then add right neighbor as the last player
			if(i-1 < 0) {
				activePlayers.get(activePlayers.size()).setRightPlayer(activePlayers.get(0));
			}else {
				activePlayers.get(i).setRightPlayer(activePlayers.get(i-1));
			}
		}
		
	}

	/**
	 * This method initially loads all the effectiv required cards into the active cards array
	 * index0: Age 1
	 * index1: Age 2
	 * index2: Age 3
	 * @author yannik roth
	 */
	private void loadGameCards() {
		this.activeCards.clear();
		//age1
		this.activeCards.set(0, this.cards.entrySet().stream()
				.filter(c -> c.getValue().getCardAgeValue() == 1)
				.filter(c -> c.getValue().getCardMinPlayer() <= this.cards.size())
				.collect(Collectors.toSet()));
		//age2
		this.activeCards.set(1, this.cards.entrySet().stream()
				.filter(c -> c.getValue().getCardAgeValue() == 2)
				.filter(c -> c.getValue().getCardMinPlayer() <= this.cards.size())
				.collect(Collectors.toSet()));
		//age3
		this.activeCards.set(2, this.cards.entrySet().stream()
				.filter(c -> c.getValue().getCardAgeValue() == 3)
				.filter(c -> c.getValue().getCardMinPlayer() <= this.cards.size())
				.collect(Collectors.toSet()));
		
	}

	public Board getBoard(int i) {
		return this.boards.get(i);
	}
	
	/**
	 * This method evaluates the winner with the given game rules.
	 * @param n amount of players. Normally these players will be a KeySet of the <code>Map<Player, []Thread> </code>
	 * which are active in this game session
	 * @return a list of players sorted by winning points. Winner is in index 0 and looser in last index.
	 * 
	 */
	public List<Player> evaluateWinner(Player...players ){
		//add players to this list. Sort by winner -> Index 0 = winner; index max = looser.
		List<Player> scoreList = new ArrayList<>();
		
		for(Player p : players) {
			//check player performace
			//on the player-object there is a list called "cards" which contains all cards
			//that the player has played thorughout the game.
			//To get the amount of coins, you can use the method player.getCoins;
			p.getCoins(); //get amount of coins
			p.getPlayedCards(); //get a list of all played cards
		}
		
		return scoreList;
		
	}
	
}
