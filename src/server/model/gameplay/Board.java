package server.model.gameplay;

import java.util.Map;
import java.util.logging.Logger;

import globals.ResourceType;
import globals.exception.IllegalParameterException;
import server.ServiceLocator;
import server.model.init.BoardLoader;

/**
 * This is the comment
 * @author rothy
 *
 */
public class Board {
	private Logger logger = ServiceLocator.getLogger();
	/**
	 * Stores world wonder whereas index 0 is world wonder 1
	 * If null, no world wonder available
	 */
	private WorldWonder[] worldWonders = new WorldWonder[4];
	
	private int boardID;
	private String boardSide;
	private String boardName;
	private ResourceType producingResource;
	
	public Board(String[] values) {
		Map<Integer, String> mapping = BoardLoader.getFieldMapping();
		
		//first, init the world wonders
		this.worldWonders[0] = new WorldWonder();
		this.worldWonders[1] = new WorldWonder();
		this.worldWonders[2] = new WorldWonder();
		this.worldWonders[3] = new WorldWonder();
		
		for(int i = 0; i< values.length; i++) {
			String fieldName = mapping.get(i);
			
			if(fieldName.equals("id")) {
				this.boardID = Integer.parseInt(values[i]);
				continue;
			}
			if(fieldName.equals("side")) {
				this.boardSide = values[i];
				continue;
			}
			if(fieldName.equals("boardName")) {
				this.boardName = values[i];
				continue;
			}
			if(fieldName.equals("producingResource")) {
				this.producingResource = ResourceType.valueOf(values[i]);
				continue;
			}
			
			//filling the worldwonders
			if(fieldName.substring(0, 6).equals("stage1")) {
				String temp = fieldName.substring(6);
				//filling the worldwonders
				if(temp.substring(0,4).equals("Cost") || temp.substring(0, 5).equals("Gains")) {
					initWonderStageMaps(1, temp.toLowerCase(), values[i]);
				}else {
					//fill rest of the attributes
					fillAttribute(1, temp.toLowerCase(), values[i]);
				}
				continue;
				
			}
			if(fieldName.substring(0, 6).equals("stage2")) {
				String temp = fieldName.substring(6);
				//filling the worldwonders
				if(temp.substring(0,4).equals("Cost") || temp.substring(0, 5).equals("Gains")) {
					initWonderStageMaps(2, temp.toLowerCase(), values[i]);
				}else {
					//fill rest of the attributes
					fillAttribute(1, temp.toLowerCase(), values[i]);
				}
				continue;
			}
			if(fieldName.substring(0, 6).equals("stage3")) {
				String temp = fieldName.substring(6);
				//filling the worldwonders
				if(temp.substring(0,4).equals("Cost") || temp.substring(0, 5).equals("Gains")) {
					initWonderStageMaps(3, temp.toLowerCase(), values[i]);
				}else {
					//fill rest of the attributes
					fillAttribute(1, temp.toLowerCase(), values[i]);
				}
				continue;
			}
			if(fieldName.substring(0, 6).equals("stage4")) {
				String temp = fieldName.substring(6);
				//filling the worldwonders
				if(temp.substring(0,4).equals("Cost") || temp.substring(0, 5).equals("Gains")) {
					initWonderStageMaps(4, temp.toLowerCase(), values[i]);
				}else {
					//fill rest of the attributes
					fillAttribute(1, temp.toLowerCase(), values[i]);
				}
				continue;
			}
			
		}
	}
	
	private void fillAttribute(int i, String field, String amount) {
		if(!amount.equals("null") && this.worldWonders[i-1] != null) {
			if (field.equals("rewardinpoints")) {
				this.worldWonders[i - 1].rewardinPoints = Integer.parseInt(amount);
			}
			if (field.equals("rewardfreecard")) {
				this.worldWonders[i - 1].rewardFreecard = amount;
			}
			if (field.equals("rewardinmilitary")) {
				this.worldWonders[i - 1].rewardinMilitary = Integer.parseInt(amount);
			}
			if (field.equals("rewardsfreecardperage")) {
				Boolean b = amount.equals("true") ? true : false;
				this.worldWonders[i - 1].rewardsFreeCardperAge = b;
			}
			if (field.equals("rewardsscienceschriften")) {
				this.worldWonders[i-1].rewardsScienceSchriften = Integer.parseInt(amount);
			}
			if (field.equals("rewardssciencekompass")) {
				this.worldWonders[i-1].rewardsScienceKompass = Integer.parseInt(amount);
			}
			if (field.equals("rewardssciencemeter")) {
				this.worldWonders[i-1].rewardsScienceMeter = Integer.parseInt(amount);
			}
			if (field.equals("stopsdiscardingcards")) {
				Boolean b = amount.equals("true") ? true : false;
				this.worldWonders[i-1].stopsDiscardingCards = b;
			}
			if (field.equals("allowstraderawmaterial")) {
				Boolean b = amount.equals("true") ? true : false;
				this.worldWonders[i-1].allowsTradeRawMaterial = b;
			}
			if (field.equals("allowscopypurplecardofneighbour")) {
				Boolean b = amount.equals("true") ? true : false;
				this.worldWonders[i-1].allowsCopyPurpleCardOfNeighbour = b;
			}
		}
		
	}
	
	private void initWonderStageMaps(int i, String field, String amount) {
		if(!amount.equals("null") && this.worldWonders[i-1] != null) {
			if (field.equals("costincoins")) {
				this.worldWonders[i - 1].cost.put(ResourceType.COIN, Integer.parseInt(amount));
			}
			if (field.equals("costinziegel")) {
				this.worldWonders[i - 1].cost.put(ResourceType.BRICK, Integer.parseInt(amount));
			}
			if (field.equals("costinerz")) {
				this.worldWonders[i - 1].cost.put(ResourceType.ORE, Integer.parseInt(amount));
			}
			if (field.equals("costinholz")) {
				this.worldWonders[i - 1].cost.put(ResourceType.WOOD, Integer.parseInt(amount));
			}
			if (field.equals("costinstein")) {
				this.worldWonders[i - 1].cost.put(ResourceType.STONE, Integer.parseInt(amount));
			}
			if (field.equals("costinglas")) {
				this.worldWonders[i - 1].cost.put(ResourceType.GLAS, Integer.parseInt(amount));
			}
			if (field.equals("costinstoff")) {
				this.worldWonders[i - 1].cost.put(ResourceType.FABRIC, Integer.parseInt(amount));
			}
			if (field.equals("costinpapyrus")) {
				this.worldWonders[i - 1].cost.put(ResourceType.PAPYRUS, Integer.parseInt(amount));
			}
			
			if(field.equals("gainsziegel")) {
				this.worldWonders[i-1].produce.put(ResourceType.BRICK, Integer.parseInt(amount));
			}
			if(field.equals("gainserz")) {
				this.worldWonders[i-1].produce.put(ResourceType.ORE, Integer.parseInt(amount));
			}
			if(field.equals("gainsholz")) {
				this.worldWonders[i-1].produce.put(ResourceType.WOOD, Integer.parseInt(amount));
			}
			if(field.equals("gainsstein")) {
				this.worldWonders[i-1].produce.put(ResourceType.STONE, Integer.parseInt(amount));
			}
			if(field.equals("gainsglas")) {
				this.worldWonders[i-1].produce.put(ResourceType.GLAS, Integer.parseInt(amount));
			}
			if(field.equals("gainsstoff")) {
				this.worldWonders[i-1].produce.put(ResourceType.FABRIC, Integer.parseInt(amount));
			}
			if(field.equals("gainspapyrus")) {
				this.worldWonders[i-1].produce.put(ResourceType.PAPYRUS, Integer.parseInt(amount));
			}
			if(field.equals("gainincoins")) {
				this.worldWonders[i-1].produce.put(ResourceType.COIN, Integer.parseInt(amount));
			}
			
		}else {
			//this worldwonder does not exist for this board type
			this.worldWonders[i-1] = null;
		}
		
	}
	
	public int getId() {
		return this.boardID;
	}
	
	public ResourceType getProducingResource() {
		return this.producingResource;
	}

}
