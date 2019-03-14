package test;

import java.io.Serializable;
import java.util.HashMap;

public class YannikMap<T> extends HashMap<String, T> implements Serializable{
	
	public YannikMap(T cost, T reason) {
		super();
		this.put("costCoin", cost);
		this.put("desc", reason);
	}
	
	
}
