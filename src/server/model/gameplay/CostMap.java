package server.model.gameplay;

import java.util.HashMap;

import globals.ResourceType;

public class CostMap extends HashMap<ResourceType, Integer>{

	public CostMap(int costCoins, int costWood, int costStone) {
		this.put(ResourceType.COIN, costCoins);
		this.put(ResourceType.WOOD, costWood);
	}
	
}
