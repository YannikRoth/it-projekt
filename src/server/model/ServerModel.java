package server.model;

import java.io.Serializable;
import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;

import globals.CardAge;
import globals.exception.DataConsistencyException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import server.ServiceLocator;
import server.model.clienthandling.ServerClientThread;
import server.model.clienthandling.ServerRequestHandler;
import server.model.database.HighScore;
import server.model.gameplay.Board;
import server.model.gameplay.Card;
import server.model.gameplay.Player;
import server.model.gameplay.ServerActionLog;
import server.model.init.BoardLoader;
import server.model.init.CardLoader;

public class ServerModel implements Serializable{
	
	private Logger logger = ServiceLocator.getLogger();
	private String hostName;
	private String hostAddress;
	
	public ObservableList<ServerActionLog> serverActionData;
	
	private ArrayList<Player> connectedPlayerList = new ArrayList<>();
	private Map<Player, ServerClientThread> players = new HashMap<>();
	private ServerRequestHandler requesthandler;
	
	public volatile int counter = 0;
	public volatile boolean gameEnd = false;
	private volatile int cardPlayCounter = 0;
	
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
	private List<Player> gameWinners = null;
	
	//end of gameplay specific varibales
	
	public ServerModel() {
		try {
			setHostName(Inet4Address.getLocalHost().getHostName());
			setHostAddress(Inet4Address.getLocalHost().getHostAddress());
		} catch (UnknownHostException e) {
			logger.warning(e.getLocalizedMessage());
		}
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
		
		serverActionData = FXCollections.observableArrayList();
		serverActionData.add(new ServerActionLog(getHostAddress(), "Server", "StartUp"));
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
		if(players.size() < NUMBEROFPLAYERS) {
			//add player to active player list
			players.put(client.getPlayer(), client);
			client.establishconnection();
			
			logger.info("successfully added client");
			ServiceLocator.getServerModel().getServerActionData().add(
					new server.model.gameplay.ServerActionLog(client.getSocket().getInetAddress().toString(), client.getPlayer().getPlayerName(), "Connected"));
			if(players.size() >= NUMBEROFPLAYERS) {
				logger.info("game Startet");
				ServiceLocator.getServerModel().getServerActionData().add(
						new server.model.gameplay.ServerActionLog(getHostAddress(), "Server", "Game started"));
				this.startGame();
			}else {
				for(Entry<Player, ServerClientThread> serverClientThread : players.entrySet()) {
					serverClientThread.getValue().sendUpdateOfPlayers();
				}
			}
		}else {
			logger.info("client could not be added");
		}
		

	}

	/**
	 * This method starts the game. It can only be started from the ServerModel itself as it is marked private!
	 * @author yannik roth
	 */
	private void startGame() {	
		//load cards to play into ArrayList
		loadGameCards();
		
		//assing player neighbors
		assignPlayerNeighbors();
		//set age initially to one
		this.cardAge = CardAge.ONE;
		
		//initially randomize 7 cards per player and assign them
		List<Card> ageOneCards = new ArrayList<>(this.activeCards.get(this.cardAge.getAgeValue()-1).values());
		handoutCards(ageOneCards);
		
		//start all threads
		for(Entry<Player, ServerClientThread> serverClientThread : players.entrySet()) {
			serverClientThread.getValue().start();
		}
	}
	
	/**
	 * This method shuffles the card set and assigns each player 7 cards
	 * @param cardsToHandOut
	 * @return void (updates the {@link Player} object)
	 * @author yannik roth
	 */
	public boolean handoutCards(List<Card> cardsToHandOut) {
		//consistency check
		try {
			if (cardsToHandOut.size() != (players.size() * 7) - 0) {
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
	
	public List<Player> evaluateWinner(List<Player> players){
		
		//TODO count points for cards of 3. age
		//add players to this list. Sort by winner -> Index 0 = winner; index max = looser.
		List<Player> scoreList = new ArrayList<>();
		
		for(Player p : players) {
			dealMilitaryPoints(p);
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
		logger.info("WinnerList: " + scoreList.toString());
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
		
		List<ArrayList<Card>> playerCards = new ArrayList<>();
		for(int i = 0; i< players.size(); i++) {
			playerCards.add(players.get(i).getPlayableCards());
		}
		
		if(direction.equals("right")) {
			playerCards.add(0, null);
			playerCards.set(0, playerCards.get(playerCards.size()-1));
			playerCards.remove(playerCards.size()-1);
		}else {
			playerCards.add(players.get(0).getPlayableCards());
			playerCards.remove(0);
		}
		
		for(int i = 0; i< players.size(); i++) {
			players.get(i).updateCardset(playerCards.get(i));
		}

	}
	
	public int getNumberOfPlayers() {
		return NUMBEROFPLAYERS;
	}
	public void setNumberOfPlayers(int i) {
		NUMBEROFPLAYERS = i;
	}

	public synchronized void updateGameStatus() {
		this.counter++;
		System.out.println("this is round: " + counter);
		
		if(counter >= NUMBEROFPLAYERS) {
			//pass cards to other players
			System.out.println("passing cards to neighbors");
			this.passCardsToNextPlayer(this.cardAge);
			this.counter=0;
		}
		
		//the code below could be transfered to another place, this is for quick and dirty testing
		cardPlayCounter++;
		if(this.cardPlayCounter == NUMBEROFPLAYERS * 6) {
			System.out.println("Entering age 2");
			for(Entry<Player, ServerClientThread> p : this.players.entrySet()) {
				Player player = p.getKey();
				this.dealMilitaryPoints(player);
			}
			//load second card age
			this.cardAge = CardAge.TWO;
			//initially randomize 7 cards per player and assign them
			List<Card> ageTwoCards = new ArrayList<>(this.activeCards.get(this.cardAge.getAgeValue()-1).values());
			handoutCards(ageTwoCards);
			//cardPlayCounter = 0;
		}
		
		if(this.cardPlayCounter == NUMBEROFPLAYERS * 6 * 2) {
			//loop through all client threads -> continue = false;
			System.out.println("Entering age 3");
			for(Entry<Player, ServerClientThread> p : this.players.entrySet()) {
				Player player = p.getKey();
				this.dealMilitaryPoints(player);
			}
			
			//game ends
			this.gameEnd = true;
			List<Player> allPlayers = new ArrayList<>(this.players.keySet());
			this.gameWinners = evaluateWinner(allPlayers);
			
			//clear all the cards from the player obj.
			for(Player p : allPlayers) {
				//add to highscore database
				HighScore hs = new HighScore(p.getPlayerName(), p.getWinningPoints());
				hs.savePersistent();
				p.clearCurrentPlayableCards();
			}
			
			HighScore.getBestPlayers(5).forEach(e -> System.out.println(e.toString()));
			
			
			//this.cardAge = CardAge.THREE;
			//List<Card> ageThreeCards = new ArrayList<>(this.activeCards.get(this.cardAge.getAgeValue()-1).values());
			//handoutCards(ageThreeCards);
		}
		System.out.println("CARD-ROUND: " + cardPlayCounter);
		
	}
	
	public synchronized List<Player> getWinners() {
		return this.gameWinners;
	}
	
	public ObservableList<ServerActionLog> getServerActionData() {
		return this.serverActionData;
	}
	
	public void setHostAddress(String hostAddress) {
		this.hostAddress = hostAddress;
	}
	
	public String getHostAddress() {
		return this.hostAddress;
	}
	public ArrayList<Player> getConnectedPlayerList() {
		return connectedPlayerList;
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}
}
