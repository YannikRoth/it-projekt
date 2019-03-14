package server.model.database;

public interface Persistent {
	
	public void fillPersistent();
	public String getCreateStatements();

}
