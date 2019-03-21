package server.model.gameplay;

import globals.exception.IllegalParameterException;

public class Board {
	
	private WorldWonder[] worldWonders = new WorldWonder[3];
	
	public Board(WorldWonder[] w) throws IllegalParameterException {
		worldWonders = w;
	}
	

}
