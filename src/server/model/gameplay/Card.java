package server.model.gameplay;

import java.util.Map;

import globals.CardAge;
import globals.ResourceMapType;
import globals.ResourceType;
import globals.exception.IllegalParameterException;

public class Card extends AbstractPlayable{
	
	private Map<ResourceType, Integer> cost = null;
	private CardAge cardAge;
	
	/**
	 * Constructor is to be filled after CSV importer has been finalized
	 * @throws IllegalParameterException 
	 */
	
	public Card() throws IllegalParameterException {
		cost = new ResourceMap(ResourceMapType.COST,1,0,0,0,0,0,0,0);
		cardAge = CardAge.ONE;
	}

}
