package server.model.gameplay;

import java.util.HashMap;

import globals.ResourceType;
import globals.exception.IllegalParameterException;

/**
 * This map is a specialization of a hashmap and is ment to transfer cost amounts between players of each resource
 * 
 * @author rothy
 *
 */
public class CostMap extends HashMap<ResourceType, Integer>{

	/**
	 * CostMap constructor will generate a HashMap where each resource cost can be obtained over the enum ResourceType
	 * If a resource is not required for a specific card or board, the cost of it should be zero (0)
	 * @param costCoins
	 * @param costWood
	 * @param costStone
	 * @throws IllegalParameterException 
	 */
	
	public CostMap(int costCoins, int costWood, int costStone, int costBrick, int costOre, int costPapyrus, int costFabric, int costGlas) throws IllegalParameterException {
		if(costCoins < 0 || costWood < 0 || costStone < 0 || costBrick < 0 || costOre < 0 || costPapyrus < 0 || costFabric < 0 || costGlas < 0) {
			throw new IllegalParameterException("Value can not be below zero");
		}
		this.put(ResourceType.COIN, costCoins);
		this.put(ResourceType.WOOD, costWood);
		
	}
	
	
}
