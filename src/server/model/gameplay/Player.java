package server.model.gameplay;

import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import globals.ResourceMapType;
import globals.ResourceType;
import javafx.beans.property.SimpleStringProperty;
import server.ServiceLocator;

/**
 * This class represents a player object. It will be created when a client connects to the server
 * @author rothy
 *
 */
public class Player implements Serializable{
	private Logger logger = ServiceLocator.getLogger();
	
	//resources
	private Map<ResourceType, Integer> resources; //only resources with single resource type
	private ArrayList<HashMap<ResourceType, Integer>> alternateResources; //only resources with alternating resource types
	private List<Card> cards;
	
	//TODO: Handle name
	//Player name has to be unique!!
	private SimpleStringProperty playerName = new SimpleStringProperty();
	//TODO: Implement every viewColumn as SimpleXXXProperty --> Every of them needs a getter-Method
	
	//game handling
	private Socket socket;
	
	public Player(String name) {
		setPlayerName(name);
		this.resources = new ResourceMap(ResourceMapType.PRODUCE);
		this.alternateResources = new ArrayList<>();
		this.cards = new ArrayList<>();
	}
	
	/**
	 * This method updates the resource "account" of the player
	 * if the value is positive, it will be added. if the value is negative, it will be subtracted
	 * @author yannik roth
	 * @param rm
	 * @throws Exception 
	 */
	private void updateResource(ResourceMap rm) {
		//delete all unused resources, paramter is value cnodition
		Set<Entry<ResourceType, Integer>> clearedResources = clearMap(rm, 0);
		
		//if the paramter map is a cost map, all entries must be negated
		if(rm.getResourceMapType().equals(ResourceMapType.COST)) {
			clearedResources.stream().map(e -> e.getValue()*(-1)).collect(Collectors.toSet());
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
			}
			this.alternateResources.add(temp);
		}
	}
	
	/**
	 * This method plays a card. Player must be able to afford card to do so
	 * @param c
	 * @author yannik roth
	 */
	public void playCard(Card c) {
		//TODO execute method is able to afford card as a precondition of this method
		this.updateResource(c.getCost());
		this.updateResource(c.getProduction());
		this.cards.add(c);
		//TODO further attributes
	}
	
	/**
	 * this private method is used to remove any not required items from a value map.
	 * @param rm A ResourceMap
	 * @param i Value condition to be excluded (normally zero)
	 * @return a <code>Set</code> of entries in the format [ResourceType, Integer]
	 */
	private Set<Entry<ResourceType, Integer>> clearMap(ResourceMap rm, int i) {
		return rm.entrySet().stream().filter(e -> e.getValue() > i).collect(Collectors.toSet());
	}

	/**
	 * This method provides evaluation if a card can be played or not. Check if player has enough resources
	 * @param Card c
	 * @return <code>true</code> if the card can be played because the player has enough resources
	 * @return <code>false</code> if the card can not be played because the player can't afford it
	 * @author yannik roth
	 */
	public boolean isAbleToAffordCard(Card c) {
		//every resource that can be afforded with non-alternating cards will be TRUE
		Map<ResourceType, Boolean> checkedResources = new HashMap<>();
		
		//make a copy because elements will be deleted
		ArrayList<HashMap<ResourceType, Integer>> alternateResourceCopy = (ArrayList<HashMap<ResourceType, Integer>>) alternateResources.clone();
		
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
		for (Map.Entry<ResourceType, Boolean> entry : checkedResources
				.entrySet()
				.stream()
				.filter(v -> v.getValue() == false)
				.collect(Collectors.toSet())
				) {
			ResourceType searchResourceType = entry.getKey();
			
			int amountRequired = c.getCost().get(searchResourceType);
			
			for(int i = 0; i<alternateResourceCopy.size(); i++) {
				HashMap<ResourceType, Integer> aR = alternateResourceCopy.get(i);
				if(aR != null && amountRequired > 0) {
					Integer amount = aR.get(searchResourceType) == null ? 0 : aR.get(searchResourceType);
					//the addition here is to check if the resource is enough when added to the other resource list.
					if(amount + this.resources.get(searchResourceType) > 0) {
						amountRequired--;
						// alternateResourceCopy.remove(aR);
						alternateResourceCopy.set(i, null);
					}
				}
			}
			
			if(amountRequired <= 0 ) {
				checkedResources.put(searchResourceType, true);
			}else {
				//should already be false, don't know if required
				checkedResources.put(searchResourceType, false);
			}
			
		}
		
		//if the entire checkedResource map only contains "true" values, then the card can be afforded
		if(checkedResources.entrySet().stream().filter(b -> b.getValue() == false).count() <= 0) {
			return true;
		}else {
			return false;
		}

	}

	public Socket getSocket() {
		return this.socket;
	}
	
	/**
	 * This method assigns a network socket to the player. It can be used from the server
	 * @param Socket s
	 */
	public void assignSocket(Socket s) {
		this.socket = s;
		logger.info("Assigned socket to player: " + this.playerName);
	}
	
	public String getPlayerName() {
		return playerName.get();
	}

	/**
	 * Player name should only be set by constructor
	 * Player name is the identifier of this class
	 * @param playerName
	 * @author david
	 */
	private void setPlayerName(String playerName) {
		//TODO: Check if other players has the same name - name has to be unique
		this.playerName.set(playerName);
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
		
		logger.warning("Called player method with no \"Player\" object: " + o.getClass().getName());
		return false;
	}
	
	@Override
	public String toString() {
		return this.playerName + ", wp=" + cards.stream().mapToInt(c -> c.getWinningPoints()).sum() +
				", mp=" + cards.stream().mapToInt(c -> c.getMilitaryPoints()).sum();
	}
	
}
