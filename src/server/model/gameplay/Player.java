package server.model.gameplay;

import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import globals.CardType.CardColor;
import globals.Globals;
import globals.ResourceMapType;
import globals.ResourceType;
import server.ServiceLocator;
import server.model.ServerModel;

/**
 * This class represents a player object. It will be created when a client connects to the server
 * @author yannik roth
 *
 */
public class Player implements Serializable{
	private transient Logger logger = ServiceLocator.getLogger();
	private transient ServerModel model = ServiceLocator.getServerModel();
	
	//environment of the player
	private int playerID;
	private Player leftPlayer;
	private Player rightPlayer;
	
	//currentPlayerCard (which are shown in GUI)
	private ArrayList<Card> currentPlayableCards = new ArrayList<>();
	
	//TODO add board to player
	private Board playerBoard;
	
	//resources
	private ResourceMap resources; //only resources with single resource type
	private ArrayList<HashMap<ResourceType, Integer>> alternateResources; //only resources with alternating resource types
	private List<Card> cards; //the cards that have been played by this player
	private List<Card> worldWonderCards; //the cards/stages of world wonder which been played by this player, needed for a special Gilde-card
	private Map<Card, Map<Player, Integer>> cardsTradingNeeded = new HashMap<>();
	
	//free cards (building chains) -> saved in lower case!
	private Set<String> freePlayableCards = new HashSet<>();
	
	//military and winning points
//	private int militaryStrength = 0; //military strength of player (sum)
//	private int militaryPlusPoints = 0; //military plus points of player (sum)
//	private int militaryMinusPoints = 0; //military minus points of player(sum) -> to be used after conflict evaluation
	private int winningPoints = 0; //winning points of the player (sum)
	private int sciencePoints = 0; //these values are calculated after the game has ended
	
	//Player name has to be unique!!
	private String playerName = new String();
	
	//game handling
	private transient Socket socket;
	
	public Player(String name) {
		setPlayerName(name);
		this.setPlayerID(ServiceLocator.getNewPlayerId());
		this.resources = new ResourceMap(ResourceMapType.PRODUCE);
		for (ResourceType r : ResourceType.values()) {
			this.resources.put(r, 0);
		}
		this.alternateResources = new ArrayList<>();
		this.cards = new ArrayList<>();
		this.worldWonderCards = new ArrayList<>();
		
		//init player according to rules
		this.addCoins(3);
	}
	
	/**
	 * This method updates the resource "account" of the player
	 * if the value is positive, it will be added. if the value is negative, it will be subtracted
	 * @author yannik roth
	 * @param rm
	 */
	private void updateResource(ResourceMap rm) {
		//delete all unused resources, paramter is value cnodition
		Set<Entry<ResourceType, Integer>> clearedResources = clearMap(rm, 0);
		
		//if the paramter map is a cost map, all entries must be negated
		if(rm.getResourceMapType().equals(ResourceMapType.COST)) {
			for(Entry<ResourceType,Integer> e : clearedResources) {
				if(e.getKey().equals(ResourceType.COIN)) {
					int newCoinAmount = this.resources.get(ResourceType.COIN) - rm.get(ResourceType.COIN);
					this.resources.put(ResourceType.COIN, newCoinAmount);
				}
			}
			return;
		}
		
		//non alternating card
		if(clearedResources.size() == 1) {
			logger.info("adding resources to regular resource map");
			for(Entry<ResourceType, Integer> entry: clearedResources) {
				Integer currentAmount = this.resources.get(entry.getKey());
				//add amount to existing amount
				this.resources.put(entry.getKey(), currentAmount+entry.getValue());
			}
		}
		//alternating cards
		else if(clearedResources.size() >= 2){
			logger.info("adding resource to alternate resource map");
			HashMap<ResourceType, Integer> temp = new HashMap<>();
			for(Entry<ResourceType, Integer> entry: clearedResources) {
				temp.put(entry.getKey(), entry.getValue());
				//TODO: Show in View, maybe observable??
			}
			this.alternateResources.add(temp);
		}
	}
	
	/**
	 * This method plays a card. Player must be able to afford card to do so
	 * @param c
	 * @author yannik roth, Roman Leuenberger
	 * @return <code>true</code> if card can be afforded and was successfully added.
	 */
	public boolean playCard(Card c) {
		if(isAbleToAffordCard(c)) {
			this.updateResource(c.getCost());
			this.updateResource(c.getProduction());
			//add building chain if card provides free card
			c.getFreeCards().forEach(cardname -> this.freePlayableCards.add(cardname.toLowerCase()));
			
			this.cards.add(c);
			//update coins for brown or grey cards (applies for 4 yellow cards only)
			if(c.getId() == 60 || c.getId() == 90) {
				long countOfOwnBrownCards = this.cards.stream().filter(d -> d.getCardType().getColor() == CardColor.BROWN).count();
				long countOfBrownCardsLeftPlayer = this.getLeftPlayer().getPlayedCards().stream().filter(e -> e.getCardType().getColor() == CardColor.BROWN).count();
				long countOfBrownCardsRightPlayer = this.getRightPlayer().getPlayedCards().stream().filter(f -> f.getCardType().getColor() == CardColor.BROWN).count();
				this.addCoins((int)countOfOwnBrownCards + (int)countOfBrownCardsLeftPlayer + (int)countOfBrownCardsRightPlayer);
			}
			if(c.getId() == 76 || c.getId() == 96) {
				long countOfOwnGreyCards = this.cards.stream().filter(d -> d.getCardType().getColor() == CardColor.GREY).count();
				long countOfGreyCardsLeftPlayer = this.getLeftPlayer().getPlayedCards().stream().filter(e -> e.getCardType().getColor() == CardColor.GREY).count();
				long countOfGreyCardsRightPlayer = this.getRightPlayer().getPlayedCards().stream().filter(f -> f.getCardType().getColor() == CardColor.GREY).count();
				this.addCoins((int)countOfOwnGreyCards + (int)countOfGreyCardsLeftPlayer + (int)countOfGreyCardsRightPlayer);
			}
			
			//check if card was able to afford with trade, if true, pay opponents
			if (this.cardsTradingNeeded.containsKey(c)) {
				for (Player p : this.cardsTradingNeeded.get(c).keySet()) {
					p.addCoins(2*this.cardsTradingNeeded.get(c).get(p));
					this.addCoins(-2*this.cardsTradingNeeded.get(c).get(p));
				}
			}
			
			removeCardFromCurrentPlayabled(c);
			
			return true;
			
		}else {
			logger.info("Can not afford card");
			return false;
		}
	}
	/**
	 * This method plays a world wonder stage, which basically is a normal Card
	 * @param ww
	 * @return
	 * @author Roman Leuenberger
	 */
	public boolean playWorldWonder(WorldWonder ww) {
		Card wwCard = ww.getWorldWonderCard();
		if(isAbleToAffordCard(wwCard)) {
			this.playerBoard.updateIndexOfNextWorldWonderStage(this);
			this.updateResource(wwCard.getCost());
			this.updateResource(wwCard.getProduction());
			
			//Karte wird in die Liste worldWonderCards eingefügt. Dies würde dann gebraucht, wenn wir die Gilden vom 3. Zeitalter noch implementieren
			this.worldWonderCards.add(wwCard);
			this.updateMilitaryPlusPoints(wwCard.getMilitaryPoints());
			//this.militaryStrength += wwCard.getMilitaryPoints();
			this.winningPoints += wwCard.getWinningPoints();
			
			//check if card was able to afford with trade, if true, pay opponents
			if (this.cardsTradingNeeded.containsKey(wwCard)) {
				for (Player p : this.cardsTradingNeeded.get(wwCard).keySet()) {
					p.addCoins(2*this.cardsTradingNeeded.get(wwCard).get(p));
					this.addCoins(-2*this.cardsTradingNeeded.get(wwCard).get(p));
				}
			}
			
			return true;
		} else {
			logger.info("Can not afford card");
			return false;
		}
	}
	
	/**
	 * this private method is used to remove any not required items from a value map.
	 * @param rm A ResourceMap
	 * @param i Value condition to be excluded (normally zero)
	 * @return a <code>Set</code> of entries in the format [ResourceType, Integer]
	 * @author yannik roth
	 */
	private Set<Entry<ResourceType, Integer>> clearMap(ResourceMap rm, int i) {
		return rm.entrySet().stream().filter(e -> e.getValue() > i).collect(Collectors.toSet());
	}

	/**
	 * This method provides evaluation if a card can be played or not. Check if player has enough resources
	 * @param Card c The card which should be checked
	 * @return <code>true</code> if the card can be played because the player has enough resources
	 * @return <code>false</code> if the card can not be played because the player can't afford it
	 * @author yannik roth
	 */
	public synchronized Boolean isAbleToAffordCard(Card c) {
		//if card can be played for free because of an earlier played card, instantly return true
		if(this.freePlayableCards.contains(c.getCardName().toLowerCase())) {
			return true;
		};
		
		//every resource that can be afforded with non-alternating cards will be TRUE
		Map<ResourceType, Boolean> checkedResources = new HashMap<>();
		
		//make a copy because elements will be deleted
		ArrayList<HashMap<ResourceType, Integer>> alternateResourceCopy = (ArrayList<HashMap<ResourceType, Integer>>) alternateResources.clone();
		
		//sorting the alternate list so that the evaluation will not use a multi card if not required.
		alternateResourceCopy = (ArrayList<HashMap<ResourceType, Integer>>) 
				alternateResourceCopy.stream().sorted((e, d) -> e.size() - d.size()).collect(Collectors.toList());
		
		//evaluate the easy ones : one card produces one or more resources at the same time
		for (Map.Entry<ResourceType, Integer> entry : c.getCost()
				.entrySet()
				.stream()
				.filter(v -> v.getValue() > 0) //get only values with effective cost
				.collect(Collectors.toSet())
				) {
			ResourceType key = entry.getKey();
			Integer value = entry.getValue();
			
			if(this.resources.get(key) >= value) {
				//player can afford this resource
				checkedResources.put(key, true);
			}else {
				//player can not afford this resource, maybe more luck with alternating resources
				checkedResources.put(key, false);
			}
			
		}
		//evaluate the difficult ones : one card produces alternating products		
		Map<ResourceType, Integer> absoluteAmountAlternatingResources = getAbsoluteAlternateResourceAmount();		
		ArrayList<ResourceType> sortedRequiredResources = Globals.sortMapByValue(absoluteAmountAlternatingResources);	
		sortedRequiredResources = (ArrayList<ResourceType>) sortedRequiredResources.stream()
				.filter(f -> checkedResources.get(f) != null && checkedResources.get(f) != true).collect(Collectors.toList());
		
		//this is needed to get all missing resources
		for(ResourceType t : checkedResources.keySet()) {
			if (checkedResources.get(t) == false) {
				if (!sortedRequiredResources.contains(t)) {
				sortedRequiredResources.add(t);
				}
			}
		}
		
		HashMap<ResourceType, Integer> missingResources = new HashMap<>();
		
		for(ResourceType t : sortedRequiredResources) {	
			//ResourceType searchResourceType = entry.getKey();
			ResourceType searchResourceType = t;
			
			int amountRequired = c.getCost().get(searchResourceType);
			
			for(int i = 0; i<alternateResourceCopy.size(); i++) {
				HashMap<ResourceType, Integer> aR = alternateResourceCopy.get(i);
				if (aR != null && amountRequired > 0) {
					//map return null if value is not present, therefore convert to zero
					Integer amount = aR.get(searchResourceType) == null ? 0 : aR.get(searchResourceType);
					if (amount > 0) {
						amountRequired--;
						if (amountRequired - this.resources.get(searchResourceType) <= 0) {
							// iteration can finish because with non-alternating resources the card can be
							// afforded
							amountRequired = 0;
						}
						
						alternateResourceCopy.set(i, null);
					}
				}
			}
			
			if(amountRequired <= 0 ) {
				checkedResources.put(searchResourceType, true);
			}else {
				//should already be false, don't know if required
				checkedResources.put(searchResourceType, false);
				missingResources.put(searchResourceType, amountRequired - this.resources.get(t));
			}
			
		}
		
		//if the entire checkedResource map only contains "true" values, then the card can be afforded
		if(checkedResources.entrySet().stream().filter(b -> b.getValue() == false).count() <= 0) {
			return true;
		}
		if(isAbleToAffordCardWithTrade(c, checkedResources, missingResources)){
			ServiceLocator.getLogger().info("Card can be played by trade");
			return true;
		}else {
			ServiceLocator.getLogger().info("Card cannot  be played by trade");
			return false;
		}

	}
	
	/**
	 * This method evaluates if player can play the card with a trade. It returns true if so. The method currently just
	 * checks if left or right player has the needed resource. It does not check if player has a discount on trading left or right
	 * @author Roman Leuenberger
	 * @param card
	 * @param checkedResources
	 * @param missingResources
	 * @return
	 */
	
	private synchronized Boolean isAbleToAffordCardWithTrade (Card card, Map<ResourceType, Boolean> checkedResources, HashMap<ResourceType, Integer> missingResources) {
		//card cannot be played with own resources...see if opponents got the required resources
		Map<Player, Map<ResourceType, Integer>> resourcesOfBothOpponents = new HashMap<>();
		Map<ResourceType, Integer> tempMapLeftPlayer = new HashMap<>();
		for(ResourceType type : leftPlayer.getResources().keySet()) {
			tempMapLeftPlayer.put(type, leftPlayer.getResources().get(type));
		}
		resourcesOfBothOpponents.put(leftPlayer, tempMapLeftPlayer);
		Map<ResourceType, Integer> tempMapRightPlayer = new HashMap<>();
		for(ResourceType type : rightPlayer.getResources().keySet()) {
			tempMapRightPlayer.put(type, rightPlayer.getResources().get(type));
			}
		resourcesOfBothOpponents.put(rightPlayer, tempMapRightPlayer);
				
		int amountOfUsedResourcesLeftPlayer = 0;
		int amountOfUsedResourcesRightPlayer = 0;
		Map<ResourceType, Integer> copy = (HashMap) missingResources.clone(); //make a copy because items will be removed in loop
		for (ResourceType t : copy.keySet()) {
			Integer amountRequired = missingResources.get(t);
			for (Player p : resourcesOfBothOpponents.keySet()) {
				if(resourcesOfBothOpponents.get(p).get(t) != null && resourcesOfBothOpponents.get(p).get(t) > 0) {
					amountRequired -= resourcesOfBothOpponents.get(p).get(t);
					if (p.equals(this.leftPlayer)) {
						amountOfUsedResourcesLeftPlayer += resourcesOfBothOpponents.get(p).get(t);
						}
					if (p.equals(this.rightPlayer)) {
						amountOfUsedResourcesRightPlayer += resourcesOfBothOpponents.get(p).get(t);
						}
					if (amountRequired <= 0) {
						int totalAmountOfNeededResources = amountOfUsedResourcesLeftPlayer + amountOfUsedResourcesRightPlayer;
						if (2*totalAmountOfNeededResources <= this.getCoins()) {
							checkedResources.put(t, true);
							missingResources.remove(t);
							Map<Player, Integer> tempMap = new HashMap<>();
							tempMap.put(leftPlayer, amountOfUsedResourcesLeftPlayer);
							tempMap.put(rightPlayer, amountOfUsedResourcesRightPlayer);
							this.cardsTradingNeeded.put(card, tempMap);
							}
						}
					}
				}
			}
		if(checkedResources.entrySet().stream().filter(b -> b.getValue() == false).count() <= 0) {
			return true;
		}
		
		//wasn't able to afford card with normal resources of opponents, check if possible with alternate ones
		Map<Player, ArrayList<HashMap<ResourceType, Integer>>> alternateResourcesOfBothOpponents = new HashMap<Player, ArrayList<HashMap<ResourceType, Integer>>>();
		if (!leftPlayer.getAlternateResources().isEmpty()) {
			alternateResourcesOfBothOpponents.put(leftPlayer, leftPlayer.getAlternateResources());
		}
		if (!rightPlayer.getAlternateResources().isEmpty()) {
			alternateResourcesOfBothOpponents.put(rightPlayer, rightPlayer.getAlternateResources());
		}
		

		copy.clear();
		copy = (HashMap) missingResources.clone();
		for (ResourceType type : copy.keySet()) {	
			Integer amountRequired = copy.get(type);
			System.out.println(alternateResourcesOfBothOpponents.toString());
			for (Player player : alternateResourcesOfBothOpponents.keySet()) {
				for (int hashMap = 0; hashMap < alternateResourcesOfBothOpponents.get(player).size(); hashMap++) {
					System.out.println(type);
					System.out.println(alternateResourcesOfBothOpponents.get(player).get(hashMap).get(type));
					if (alternateResourcesOfBothOpponents.get(player).get(hashMap) != null &&
						alternateResourcesOfBothOpponents.get(player).get(hashMap).get(type) != null &&
						alternateResourcesOfBothOpponents.get(player).get(hashMap).get(type) >= 1) {
						amountRequired -= alternateResourcesOfBothOpponents.get(player).get(hashMap).get(type);

						if (player.equals(this.leftPlayer)) {
							amountOfUsedResourcesLeftPlayer += alternateResourcesOfBothOpponents.get(player).get(hashMap).get(type);
						}
						if (player.equals(this.rightPlayer)) {
							amountOfUsedResourcesRightPlayer += alternateResourcesOfBothOpponents.get(player).get(hashMap).get(type);
						}
						System.out.println("amountRequired: " + amountRequired);
						if (amountRequired <= 0) {
							int totalAmountOfNeedeResources = amountOfUsedResourcesLeftPlayer + amountOfUsedResourcesRightPlayer;
							System.out.println("totalAmountOfNeedeResources: " + totalAmountOfNeedeResources);
							System.out.println("this.getCoins(): " + this.getCoins());
							if (2*totalAmountOfNeedeResources <= this.getCoins()) {
								checkedResources.put(type, true);
								missingResources.remove(type);
								Map<Player, Integer> tempMap = new HashMap<>();
								tempMap.put(leftPlayer, amountOfUsedResourcesLeftPlayer);
								tempMap.put(rightPlayer, amountOfUsedResourcesRightPlayer);
								this.cardsTradingNeeded.put(card, tempMap);
								}
							}
						} else {
							System.out.println("value in trading is null or opponets do not own required resouces");
						}
					}
				}
			}

		if(checkedResources.entrySet().stream().filter(b -> b.getValue() == false).count() <= 0) {
			return true;
		}
	
		return false;
	}

	/**
	 * This method counts all alternating resources. This is never used for gameplay logig as this would make no sense. However, this method
	 * is required to complete the card play evaluation with our implemented logic.
	 * @return a Map containing all resources with their absolute (summarized) amount.
	 * @author yannik roth
	 */
	private Map<ResourceType, Integer> getAbsoluteAlternateResourceAmount() {
		Map<ResourceType, Integer> result = new HashMap<>();
		for(Map<ResourceType, Integer> map : alternateResources) {
			Set<Entry<ResourceType, Integer>> mapentry = map.entrySet();
			for(Entry<ResourceType, Integer> setEntry : mapentry) {
				int currentAmount = result.get(setEntry.getKey()) == null ? 0 : result.get(setEntry.getKey());
				currentAmount += setEntry.getValue() == null ? 0 : setEntry.getValue();
				result.put(setEntry.getKey(), currentAmount);
			}
		}
		return result;
	}

	public Socket getSocket() {
		return this.socket;
	}
	
	/**
	 * This method assigns a network socket to the player. It can be used from the server
	 * @param Socket s which is to be assigned
	 */
	public void assignSocket(Socket s) {
		this.socket = s;
		logger.info("Assigned socket to player: " + this.playerName);
	}
	
	/**
	 * Adds coins to the player.
	 * Used initially of the game of when player sells card
	 * @param i
	 */
	public void addCoins(int i) {
		int newAmountOfCoins = this.resources.get(ResourceType.COIN)+i;
		this.resources.put(ResourceType.COIN, newAmountOfCoins);
	}
	
	/**
	 * Get coins from player
	 * @return
	 */
	public int getCoins() {
		return this.resources.get(ResourceType.COIN);
	}
	
	public String getPlayerName() {
		return playerName;
	}

	/**
	 * Player name should only be set by constructor
	 * Player name is the identifier of this class
	 * @param playerName
	 * @author david
	 */
	private void setPlayerName(String playerName) {
		this.playerName = playerName;
	}
	
	/**
	 * Compare two player objects, player name has to be unique
	 * @return true or false
	 * @author david
	 */
	@Override
	public boolean equals(Object o) {
		if(o instanceof Player)
			return this.playerName.equals(((Player)o).getPlayerName());
		else if(o != null)
			logger.warning("Called player method with no \"Player\" object: " + o.getClass().getName());
		else
			logger.warning("Called Player.equals() method with no instance of param called");
		
		return false;
	}
	
	public void updateCardset(ArrayList<Card> cards) {
		currentPlayableCards = cards;
	}
	
	@Override
	public String toString() {
		return this.playerName + ", wp=" + cards.stream().mapToInt(c -> c.getWinningPoints()).sum() +
				", mp=" + cards.stream().mapToInt(c -> c.getMilitaryPoints()).sum();
	}
	
	public void setBoard(Board b) {
		this.playerBoard = b;
		//update the player resource map
		int currentAmount = this.resources.get(b.getProducingResource());
		this.resources.put(b.getProducingResource(), currentAmount + 1);
	}
	
	public void setRightPlayer(Player p) {
		this.rightPlayer = p;
	}
	
	public Player getRightPlayer() {
		return this.rightPlayer;
	}
	
	public void setLeftPlayer(Player p) {
		this.leftPlayer = p;
	}
	
	public Player getLeftPlayer() {
		return this.leftPlayer;
	}
	public ArrayList<Card> getPlayableCards(){ 
		return currentPlayableCards; 
	}
	
	public List<Card> getPlayedCards(){
		return this.cards;
	}
	/**
	 * This method return the difference between MilPLUSpointsand MilMINUSpoints
	 * @author Yannik Roth, Roman Leuenberger
	 * @return
	 */
	public int getMilitaryStrength() {
		return this.resources.get(ResourceType.MILITARYPLUSPOINTS);
	}

	/**
	 * The method updates the militaryPlusPoints with the given amount by parameter
	 * @author Yannik Roth
	 * @param points
	 */
	public void updateMilitaryPlusPoints(int points) {
		int currentMilPoints = this.resources.get(ResourceType.MILITARYPLUSPOINTS);
		this.resources.put(ResourceType.MILITARYPLUSPOINTS, currentMilPoints + points);
	}
	
	/**
	 * The method updates the militaryMinusPoints with the given amount by parameter
	 * @author Yannik Roth
	 * @param points
	 */
	public void updateMilitaryMinusPoints(int points) {
		int currentMilPoints = this.resources.get(ResourceType.MILITARYMINUSPOINTS);
		this.resources.put(ResourceType.MILITARYMINUSPOINTS, currentMilPoints + points);
	}
	public int getWinningPoints() {
		return this.resources.get(ResourceType.WINNINGPOINTS);
	}
	
	public void addWinningPoints (int points) {
		int currentWinPoints = this.resources.get(ResourceType.WINNINGPOINTS);
		this.resources.put(ResourceType.WINNINGPOINTS, currentWinPoints + points);
	}
	
	public ResourceMap getResources() {
		return this.resources;
	}
	public ArrayList<HashMap<ResourceType, Integer>> getAlternateResources(){
		return this.alternateResources;
	}
	
	public Board getPlayerBoard() {
		return this.playerBoard;
	}
	
	/**
	 * This method is used to sell the card to the bank.
	 * The player will receive 3 coins
	 * @param Card c to be discarded/sold to the bank
	 * @return boolean if the operation was successful
	 * @author yannik roth
	 */
	public boolean discardCard(Card c) {
		this.addCoins(3);
		removeCardFromCurrentPlayabled(c);
		return true;
	}
	
	public void removeCardFromCurrentPlayabled(Card c) {
		Card cr = this.currentPlayableCards.remove(this.currentPlayableCards.indexOf(c));
		logger.info("removed card: " + cr);
		logger.info("now available cards: " + currentPlayableCards);
	}
	
	public Board getBoard() {
		return this.playerBoard;
	}
	
	/**
	 * Used when game ends. No card should be playable anymore
	 */
	public void clearCurrentPlayableCards() {
		this.currentPlayableCards.clear();
	}

	public int getPlayerID() {
		return playerID;
	}

	public void setPlayerID(int playerID) {
		this.playerID = playerID;
	}
}
