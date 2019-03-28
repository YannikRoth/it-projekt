package server.model.gameplay;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import globals.ResourceMapType;
import globals.ResourceType;

/**
 * This class represents a player object. It will be created when a client connects to the server
 * @author rothy
 *
 */
public class Player {
	
	private int amountCoins;
	private int amountWood;
	private int amountBrick;
	private int amountGlas;
	
	private Map<ResourceType, Integer> resources;
	private ArrayList<HashMap<ResourceType, Integer>> alternateResource;
	private List<Card> cards;
	
	
	public Player() {
		this.resources = new ResourceMap(ResourceMapType.PRODUCE);
		this.alternateResource = new ArrayList<>();
		this.cards = new ArrayList<>();
	}
	
	/**
	 * This method provides evaluation if a card can be played or not. Check if player has enough resources
	 * @param Card c
	 * @return <code>true</code> if the card can be played because the player has enough resources
	 * @return <code>false</code> if the card can not be played because the player can't afford it
	 */
	public boolean isAbleToAffordCard(Card c) {
		//every resource that can be afforded with non-alternating cards will be TRUE
		Map<ResourceType, Boolean> checkedResources = new HashMap<>();
		
		//make a copy because elements will be deleted
		ArrayList<HashMap<ResourceType, Integer>> alternateResourceCopy = (ArrayList<HashMap<ResourceType, Integer>>) alternateResource.clone();
		
		//evaluate the easy ones : one card produces one or more resources at the same time
		for (Map.Entry<ResourceType, Integer> entry : c.getCost()
				.entrySet()
				.stream()
				.filter(v -> v.getValue() > 0)
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
		
		//evaluate the difficult onnes : one card produces alternating products
		for (Map.Entry<ResourceType, Boolean> entry : checkedResources
				.entrySet()
				.stream()
				.filter(v -> v.getValue() == false)
				.collect(Collectors.toSet())
				) {
			ResourceType searchResourceType = entry.getKey();
			
			int amountRequired = c.getCost().get(searchResourceType);
			
			for(HashMap<ResourceType, Integer> aR : alternateResourceCopy) {
				if(aR.get(searchResourceType)> 0) {
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

}
