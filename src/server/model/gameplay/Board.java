package server.model.gameplay;

import globals.exception.IllegalParameterException;

/**
 * This is the comment
 * @author rothy
 *
 */
public class Board {
	
	private WorldWonder[] worldWonders = new WorldWonder[3];
	
	/**
	 * 
	 * @param w
	 * @throws IllegalParameterException this is a test
	 */
	public Board(WorldWonder[] w) throws IllegalParameterException {
		worldWonders = w;
	}
	

}
