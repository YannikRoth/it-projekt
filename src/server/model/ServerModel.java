package server.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import globals.CardAge;
import globals.CardType;
import globals.CardType.CardColor;
import server.ServiceLocator;
import server.model.clienthandling.ServerClientThread;
import server.model.clienthandling.ServerRequestHandler;
import server.model.gameplay.Board;
import server.model.gameplay.Card;
import server.model.gameplay.Player;
import server.model.init.BoardLoader;
import server.model.init.CardLoader;

public class ServerModel implements Serializable{
	
	private Logger logger = ServiceLocator.getLogger();
	
	private Map<Player, ServerClientThread> players = new HashMap<>();
	private ServerRequestHandler requesthandler;
	
	//cards and boards are imported when class is initialized
	//KEY = Item ID
	// Value = Card or Board Object
	private Map<Integer, Card> cards;
	private Map<Integer, Board> boards;
	
	private int NUMBEROFPLAYERS = 2;
	
	//gameplay specific variables
	//index0 = age 1; index2 = age 3
	//private List<Set<Entry<Integer, Card>>> activeCards = new ArrayList<>();
	private List<Map<Integer, Card>> activeCards = new ArrayList<>();
	private CardAge cardAge;
	//end of gameplay specific varibales
	
	public ServerModel() {
		//load masterdata and store them into class maps
		this.cards = new HashMap<>();
		CardLoader.importCards(this);
		logger.info(this.cards.size() + " cards have been sucessfully imported");
		this.boards = new HashMap<>();
		BoardLoader.importBoards(this);
		logger.info(this.boards.size() + " boards have been sucessfully imported");
		
		//loadGameCards();
		
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
	/**
	 * 
	 * @author martin
	 *
	 */
	public void addClient(ServerClientThread client) {
		// TODO Bedingungen wenn neuer Client erlaubt ist und wann nicht
		if(players.size() < NUMBEROFPLAYERS) {
			//add player to active player list
			players.put(client.getPlayer(), client);
			client.getPlayer().setBoard(boards.get(7));
			logger.info("successfully added client");
			if(players.size() >= NUMBEROFPLAYERS) {
				logger.info("game Startet");
				this.startGame();
			}
		}else {
			logger.info("client could not be added");
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
		this.cardAge = CardAge.ONE;
		
		for(Entry<Player, ServerClientThread> serverClientThread : players.entrySet()) {
			serverClientThread.getValue().start();
		}
	}		
	
	/**
	 * Assign neighbors to players
	 * @author yannik roth
	 */
	private void assignPlayerNeighbors() {
		List<Player> activePlayers = new ArrayList<>(this.players.keySet());
		int amountPlayer = activePlayers.size();
		
		for(int i = 0; i < amountPlayer; i++) {
			if(i == 0) {
				//this is the first player
				activePlayers.get(i).setLeftPlayer(activePlayers.get(amountPlayer-1));
				activePlayers.get(i).setRightPlayer(activePlayers.get(i+1));
			}else if(i == amountPlayer -1) {
				//this is the last player
				activePlayers.get(i).setLeftPlayer(activePlayers.get(i-1));
				activePlayers.get(i).setRightPlayer(activePlayers.get(0));
			}else {
				activePlayers.get(i).setLeftPlayer(activePlayers.get(i-1));
				activePlayers.get(i).setRightPlayer(activePlayers.get(i+1));
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
		
		Map<Integer, Card> ageOne = new HashMap<>();
		Map<Integer, Card> ageTwo = new HashMap<>();
		Map<Integer, Card> ageThree = new HashMap<>();
		
		cards.entrySet().stream()
		//filter by min amount of players
		.filter(entry -> entry.getValue().getCardMinPlayer() <= players.size())
		.forEach(entry -> {
			//age1
			if(entry.getValue().getCardAgeValue() ==1) {
				ageOne.put(entry.getKey(), entry.getValue());
			//age2
			}else if(entry.getValue().getCardAgeValue() == 2) {
				ageTwo.put(entry.getKey(), entry.getValue());
			//age3
			}else if(entry.getValue().getCardAgeValue() == 3) {
				ageThree.put(entry.getKey(), entry.getValue());
			}
		});
		
		activeCards.add(0, ageOne);
		activeCards.add(1, ageTwo);
		activeCards.add(2, ageThree);
		
	}

	public Board getBoard(int i) {
		return this.boards.get(i);
	}
	
	/**
	 * This method evaluates the winner with the given game rules.
	 * @param n amount of players. Normally these players will be a KeySet of the <code>Map<Player, []Thread> </code>
	 * which are active in this game session
	 * @return a list of players sorted by winning points. Winner is in index 0 and looser in last index.
	 * @author Roman Leuenberger
	 */
	
	public List<Player> evaluateWinner(Player...players ){
		
		//TODO count points for cards of 3. age
		//add players to this list. Sort by winner -> Index 0 = winner; index max = looser.
		List<Player> scoreList = new ArrayList<>();
		
		for(Player p : players) {
			//check player performace
			//on the player-object there is a list called "cards" which contains all cards
			//that the player has played thorughout the game.
			//To get the amount of coins, you can use the method player.getCoins;
			dealMilitaryPoints(p);
			//p.addWinningPoints(p.getMilitaryPlusPoints() - p.getMilitaryMinusPoints());
			p.addWinningPoints(p.getMilitaryStrength());
			p.addWinningPoints(p.getCoins()/3); //get amount of coins
			ArrayList<Card> cardsPlayedByPlayer = (ArrayList<Card>) p.getPlayedCards(); //get a list of all played cards
			
			//evaluate and add points for green cards
			int countOfSchriften = 0;
			int countOfKompass = 0;
			int countOfMeter = 0;
			for (Card c : cardsPlayedByPlayer) {
				if (c.isSciencePointsSchriften()) {
					countOfSchriften += 1;
				}
				if (c.isSciencePointsKompass()) {
					countOfKompass += 1;
				}
				if (c.isSciencePointsMeter()) {
					countOfMeter += 1;
				}
			}
			//add winning points based on count of symbol ^2
			Map<String, Integer> sciencePoints = new HashMap<>();
			sciencePoints.put("schrift", countOfSchriften);
			sciencePoints.put("kompass", countOfKompass);
			sciencePoints.put("meter", countOfMeter);
			p.addWinningPoints((int)Math.pow(sciencePoints.get("schrift"), 2));
			p.addWinningPoints((int)Math.pow(sciencePoints.get("kompass"), 2));
			p.addWinningPoints((int)Math.pow(sciencePoints.get("meter"), 2));
			
			List<Integer> setsScienceCards = new ArrayList<Integer>();
			for (int i : sciencePoints.values()) {
				setsScienceCards.add(i);
			}
			setsScienceCards.sort(Comparator.comparing(e -> e));
			
			p.addWinningPoints(setsScienceCards.get(0) == null ? 0 : setsScienceCards.get(0) * 7);
			scoreList.add(p);
		}
		scoreList.sort(Comparator.comparing(p -> p.getWinningPoints()));
		return scoreList;
	}
	//method for comparing all the military strength and deal military points
	public void dealMilitaryPoints (Player p) {
			int strengthOfPlayer = p.getMilitaryStrength();
			int strengthOfLeftPlayer = p.getLeftPlayer().getMilitaryStrength();
			int strengthOfRightPlayer = p.getRightPlayer().getMilitaryStrength();
			
			int milPoints = this.cardAge == CardAge.ONE ? 1 : this.cardAge == CardAge.TWO ? 3 : 5;
			
			if(strengthOfPlayer < strengthOfLeftPlayer) {
				p.updateMilitaryMinusPoints(-1);
			}
			if(strengthOfPlayer > strengthOfLeftPlayer) {
				p.updateMilitaryPlusPoints(milPoints);
			}
			if(strengthOfPlayer < strengthOfRightPlayer) {
				p.updateMilitaryMinusPoints(-1);
			}
			if(strengthOfPlayer > strengthOfRightPlayer) {
				p.updateMilitaryPlusPoints(milPoints);
			}
	}
	
	public int getNumberOfPlayers() {
		return NUMBEROFPLAYERS;
	}
	public void setNumberOfPlayers(int i) {
		NUMBEROFPLAYERS = i;
	}
	
}
