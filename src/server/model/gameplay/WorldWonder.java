package server.model.gameplay;

import java.util.Map;

import globals.ResourceMapType;
import globals.ResourceType;

/**
 * World wonders can be attached to a Board
 * @author yannik roth
 */

public class WorldWonder {
	//resource maps for cost and production
	protected Map<ResourceType, Integer> cost = new ResourceMap(ResourceMapType.COST);;
	protected Map<ResourceType, Integer> produce = new ResourceMap(ResourceMapType.PRODUCE);
	
	//further attributes for this stage
	protected Integer rewardinPoints;
	protected String rewardFreecard;
	protected Integer rewardinMilitary;
	protected Boolean rewardsFreeCardperAge;
	protected Integer rewardsScienceSchriften;
	protected Integer rewardsScienceKompass;
	protected Integer rewardsScienceMeter;
	protected Boolean stopsDiscardingCards;
	protected Boolean allowsTradeRawMaterial;
	protected Boolean allowsCopyPurpleCardOfNeighbour;

	
	//is either 1,2,3,4
	private int worldWonderCount;

}
