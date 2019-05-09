package server.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
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
import globals.exception.DataConsistencyException;
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
	
	private int NUMBEROFPLAYERS = 3;
	
	//gameplay specific variables
	//index0 = age 1; index2 = age 3
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
			//client.getPlayer().setBoard(boards.get(7));
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
		//assing player neighbors
		assignPlayerNeighbors();
		//set age initially to one
		this.cardAge = CardAge.ONE;
		
		//initially randomize 7 cards per player and assign them
		List<Card> ageOneCards = new ArrayList<>(this.activeCards.get(this.cardAge.getAgeValue()-1).values());
		handoutCards(ageOneCards);
//		Collections.shuffle(ageOneCards);
//		int cardIndex = 0;
//		int loopIndex = 0;
//		int playerIndex = 1;
//		for(Entry<Player, ServerClientThread> entry : this.players.entrySet()) {
//			Player p = entry.getKey();
//			int start = loopIndex * 7;
//			int end = playerIndex * 7;
//			ArrayList<Card> tempCardList = new ArrayList<>();
//			while(start < end) {
//				tempCardList.add(ageOneCards.get(cardIndex));
//				cardIndex++;
//				start++;
//			}
//			p.updateCardset(tempCardList);
//			loopIndex++;
//			playerIndex++;
//		}
		
		for(Entry<Player, ServerClientThread> serverClientThread : players.entrySet()) {
			serverClientThread.getValue().start();
		}
		
	}
	
	/**
	 * This method shuffles the card set and assigns each player 7 cards
	 * @param cardsToHandOut
	 * @return void (updates the {@link Player} object)
	 */
	public boolean handoutCards(List<Card> cardsToHandOut) {
		//consistency check
		try {
			if (cardsToHandOut.size() != (players.size() * 7) - 1) {
				throw new DataConsistencyException("The card set passed to this methoded is invalid in its size");
			}
		} catch (DataConsistencyException e) {
			logger.info("The card set passed to this methoded is invalid in its size");
			e.printStackTrace();
			return false;
		}
		//shuffle the cardset
		Collections.shuffle(cardsToHandOut);
		int cardIndex = 0;
		int loopIndex = 0;
		int playerIndex = 1;
		for(Entry<Player, ServerClientThread> entry : this.players.entrySet()) {
			Player p = entry.getKey();
			int start = loopIndex * 7;
			int end = playerIndex * 7;
			ArrayList<Card> tempCardList = new ArrayList<>();
			while(start < end) {
				tempCardList.add(cardsToHandOut.get(cardIndex));
				cardIndex++;
				start++;
			}
			p.updateCardset(tempCardList);
			loopIndex++;
			playerIndex++;
		}
		return true;
		
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
	 * Performs a consistency check and fails of masterdata contains inconsistent data
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
		
		//performing consistency check (as for now only for age 1 and 2)
		//TODO if implementing age 3, this needs to be checked here as well
		if(activeCards.get(0).size() != players.size() * 7) {
			try {
				throw new DataConsistencyException("Consistency check failed while preparing cards for age 1. Take a look at masterdata?");
			} catch (DataConsistencyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				logger.info("Consistency check failed while preparing cards for age 1. Take a look at masterdata");
			}
		}
		if(activeCards.get(1).size() != players.size() * 7) {
			try {
				throw new DataConsistencyException("Consistency check failed while preparing cards for age 2. Take a look at masterdata");
			} catch (DataConsistencyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				logger.info("Consistency check failed while preparing cards for age 2. Take a look at masterdata");
			}
		}
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
			//p.addWinningPoints(p.getMilitaryStrength());
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
		Collections.reverse(scoreList);
		return scoreList;
	}
	
	/**
	 * Method for comparing all the military strength and deal military points
	 * @param p
	 * @author Roman Leuenberger
	 */
	public void dealMilitaryPoints (Player p) {
			int strengthOfPlayer = p.getMilitaryStrength();
			int strengthOfLeftPlayer = p.getLeftPlayer().getMilitaryStrength();
			int strengthOfRightPlayer = p.getRightPlayer().getMilitaryStrength();
			
			int milPoints = this.cardAge == CardAge.ONE ? 1 : this.cardAge == CardAge.TWO ? 3 : 5;
			
			if(strengthOfPlayer < strengthOfLeftPlayer) {
				p.addWinningPoints(-1);
			}
			if(strengthOfPlayer > strengthOfLeftPlayer) {
				p.addWinningPoints(milPoints);
			}
			if(strengthOfPlayer < strengthOfRightPlayer) {
				p.addWinningPoints(-1);
			}
			if(strengthOfPlayer > strengthOfRightPlayer) {
				p.addWinningPoints(milPoints);
			}
	}
	
	/**
	 * This method passes the cards from one player to the other. Direction is defined within the CardAge.
	 * Precondition: Left and Right player for each player must be set!
	 * Active players are taken from the Player Map.
	 * @return
	 * @author Yannik Roth
	 */
	public void passCardsToNextPlayer(CardAge age) {
		String direction = age.getTurnDirection().toLowerCase();
		List<Player> players = new ArrayList<>(this.players.keySet());

		for (int i = 0; i < players.size(); i++) {
			// this is the first player
			if (i == 0) {
				Player current = players.get(0);
				Player left = players.get(players.size() - 1);
				Player right = players.get(1);

				// take cards and give them to player on the left
				if (direction.equals("left")) {
					left.updateCardset(current.getPlayableCards());
				}
				// take cards and give them to player on the right
				else if (direction.equals("right")) {
					right.updateCardset(current.getPlayableCards());
				}
			}
			// this is the last player
			else if (i == players.size() - 1) {
				Player current = players.get(players.size() - 1);
				Player left = players.get(players.size() - 2);
				Player right = players.get(0);

				// take cards and give them to player on the left
				if (direction.equals("left")) {
					left.updateCardset(current.getPlayableCards());
				}
				// take cards and give them to player on the right
				else if (direction.equals("right")) {
					right.updateCardset(current.getPlayableCards());
				}

			}
			// this is a middle player
			else {
				Player current = players.get(i);
				Player left = players.get(i - 1);
				Player right = players.get(i + 1);

				// take cards and give them to player on the left
				if (direction.equals("left")) {
					left.updateCardset(current.getPlayableCards());
				}
				// take cards and give them to player on the right
				else if (direction.equals("right")) {
					right.updateCardset(current.getPlayableCards());
				}
			}
		}

	}
	
	public int getNumberOfPlayers() {
		return NUMBEROFPLAYERS;
	}
	public void setNumberOfPlayers(int i) {
		NUMBEROFPLAYERS = i;
	}
	
}
