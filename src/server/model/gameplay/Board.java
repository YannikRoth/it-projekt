package server.model.gameplay;

public class Board extends AbstractPlayable{
	
	private WorldWonder[] worldWonders = new WorldWonder[3];
	
	public Board(WorldWonder[] w) {
		worldWonders = w;
	}
	

}
