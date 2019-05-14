package server.model.gameplay;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import globals.ResourceMapType;
import globals.ResourceType;
import globals.exception.IllegalParameterException;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;

/**
 * This map is a specialization of a hashmap and is ment to transfer cost amounts between players of each resource
 * 
 * @author rothy
 *
 */
public class ResourceMap extends HashMap<ResourceType, Integer>{
	
	private ResourceMapType type;
	private transient ObservableMap<ResourceType, Integer> resourcesObservable = FXCollections.observableHashMap();
	private transient ObservableList<ResourceType> resourcesListObservable = FXCollections.observableArrayList();
	
	
	public ResourceMap(ResourceMapType type) {
		this.type = type;
		
		handleObservable();
	}
	
	/**
	 * Handle map entrys in list
	 * @author david
	 */
	public void handleObservable() {
		resourcesObservable.addListener((MapChangeListener.Change<? extends ResourceType, ? extends Integer> change) -> {
			boolean removed = change.wasRemoved();
			if (removed != change.wasAdded()) {
				// no put for existing key
				if (removed) {
					resourcesListObservable.remove(change.getKey());
				} else {
					resourcesListObservable.add(change.getKey());
				}
				Collections.sort(resourcesListObservable, new Comparator<ResourceType>() {
					@Override
					public int compare(ResourceType r1, ResourceType r2) {
						return r1.ordinal() - r2.ordinal();
					}
				});
			}
		});
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
	
	/**
	 * Override put-Method to synchronize observable list
	 * @author david
	 */
	@Override
	public Integer put(ResourceType key, Integer value) {
		resourcesObservable.put(key, value);
		return super.put(key, value);
	}
	
	/**
	 * Override remove-Method to synchronize observable list
	 * @author david
	 */
	@Override
	public Integer remove(Object key) {
		resourcesObservable.remove(key);
		return super.remove(key);
	}
	
	/**
	 * Override clear-Method to synchronize observable list
	 * @author david
	 */
	@Override
	public void clear() {
		resourcesObservable.clear();
		super.clear();
	}
	
	/**
	 * Function to refresh observable map after refresh player from server
	 * Observable maps and lists are not synchronizable
	 * @author david & yannik
	 */
	public void refreshObservableMap() {
		if(this.resourcesObservable != null) {
			this.resourcesObservable.clear();
		}else {
			resourcesObservable = FXCollections.observableHashMap();
			resourcesListObservable = FXCollections.observableArrayList();
			handleObservable();
		}
		
		for(Entry<ResourceType, Integer> entry : this.entrySet()) {
			ResourceType t = entry.getKey();
			Integer v = entry.getValue();
			resourcesObservable.put(t, v);
		}
	}
	
	public ObservableMap<ResourceType, Integer> getResourcesObservable() {
		return this.resourcesObservable;
	}
	
	public ObservableList<ResourceType> getResourcesListObservable() {
		return this.resourcesListObservable;
	}
	
	public ResourceMapType getResourceMapType() {
		return this.type;
	}
}
