package server.model.gameplay;

import java.util.HashMap;

import globals.ResourceMapType;
import globals.ResourceType;
import globals.exception.IllegalParameterException;

/**
 * This map is a specialization of a hashmap and is ment to transfer cost amounts between players of each resource
 * 
 * @author rothy
 *
 */
public class ResourceMap extends HashMap<ResourceType, Integer>{

	/**
	 * ResourceMap constructor will generate a HashMap where each amount of resource can be obtained over the enum ResourceType
	 * If a resource is not required for a specific card or board, the amount of it should be zero (0)
	 * @param amountCoins
	 * @param amountWood
	 * @param amountStone
	 * @throws IllegalParameterException 
	 */
	
	private ResourceMapType type;
	
	public ResourceMap(ResourceMapType type, int amountCoins, int amountWood, int amountStone, int amountBrick, int amountOre, int amountPapyrus, int amountFabric, int amountGlas) throws IllegalParameterException {
		
		this.type = type;
		
		if(amountCoins < 0 || amountWood < 0 || amountStone < 0 || amountBrick < 0 || amountOre < 0 || amountPapyrus < 0 || amountFabric < 0 || amountGlas < 0) {
			throw new IllegalParameterException("Value can not be below zero");
		}
		this.put(ResourceType.COIN, amountCoins);
		this.put(ResourceType.WOOD, amountWood);
		
	}
	
	
}
