package server.model.gameplay;

import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
public class Player {
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
			
			for(HashMap<ResourceType, Integer> aR : alternateResourceCopy) {
				if(aR.get(searchResourceType) + this.resources.get(searchResourceType)> 0) {
					amountRequired--;
					alternateResourceCopy.remove(aR);
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
