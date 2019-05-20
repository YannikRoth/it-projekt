package server.model.database;

public interface Persistent {
	
	/**
	 * This method updates or inserts the current object values of this persistent object into the database
	 * @author yannik roth
	 */
	public void savePersistent();

	
	/**
	 * This method return the SQL create statement of this persistent object
	 * @return SQL String to create this table
	 * @author yannik roth
	 */
	public String getCreateStatements();
	
	/**
	 * This method should return the table name of this persistent object
	 * @return String of tablename
	 * @author yannik roth
	 */
	public String getTableName();
	
	/**
	 * This method is required to identify the primary key of the persistent object
	 * @return the id (primary key) of the obj.
	 * @author yannik roth
	 */
	public int getId();

}
