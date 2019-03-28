package server.model.gameplay;

import java.util.Map;

import globals.ResourceType;
import globals.exception.IllegalParameterException;

public abstract class AbstractPlayable {
	
	private Map<ResourceType, Integer> produce = null;
	private int playableID = -1;
	
	public AbstractPlayable() throws IllegalParameterException {
		produce = new ResourceMap(null, 0, 0, 0, 0, 0, 0, 0, 0);
	}

}
