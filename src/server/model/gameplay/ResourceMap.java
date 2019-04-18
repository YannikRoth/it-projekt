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
	
	private ResourceMapType type;
	
	public ResourceMap(ResourceMapType type) {
		this.type = type;
	}
	
	/**
	 * Override get method. If a value is not found, it returns zero
	 * @author yannik roth
	 */
	@Override
	public Integer get(Object o) {
		Integer i = (super.get(o) == null ? 0 : super.get(o));
		return i;
	}
	
	public ResourceMapType getResourceMapType() {
		return this.type;
	}
	
	
}
