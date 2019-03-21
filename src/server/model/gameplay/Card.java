package server.model.gameplay;

import java.io.Serializable;
import java.util.Map;

import globals.CardAge;
import globals.ResourceMapType;
import globals.ResourceType;
import globals.exception.IllegalParameterException;

/**
 * Represent a card in the system
 * @author rothy
 *
 */

public class Card implements Serializable{
	
	private Map<ResourceType, Integer> cost = null;
	private Map<ResourceType, Integer> produce = null;
	private CardAge cardAge;
	private int minPlayer = 0;
	
	public Card(ResourceMap cost, ResourceMap produce, CardAge age, int minPlayer) {
		this.cost = cost;
		this.produce = produce;
		this.cardAge = age;
		this.minPlayer = minPlayer;
		
	}

}
