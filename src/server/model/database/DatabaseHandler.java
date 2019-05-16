package server.model.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;
import java.util.function.Function;
import java.util.logging.Logger;

import server.ServiceLocator;

public class DatabaseHandler {
	
	private static Connection con = ConnectionManager.getDbConnection();
	private static Logger logger = ServiceLocator.getLogger();
	
	/**
	 * Creates the table if it does not exist
	 * @author yannik roth
	 * @param p
	 */
	public static void checkTable(Persistent p) {
		try {
			Statement stmt = con.prepareStatement("SELECT * FROM " + p.getTableName());
		} catch (SQLException e) {
			logger.info("Table does not exist, creating it now...");
			try {
				Statement s = con.createStatement();
				s.execute(p.getCreateStatements());
			} catch (SQLException e1) {
				logger.info("table could not be created");
				e1.printStackTrace();
			}
		}
	}
	
	/**
	 * This method clears the table for any persistent object
	 * It is important that the persistent object has the same db name as its class name
	 * @author yannik roth
	 * @param c
	 */
	public static void clearTable(Class<? extends Persistent> c) {
		try {
			Statement stmt = con.createStatement();
			stmt.execute("DROP TABLE " + c.getSimpleName());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * This method checks if the entry already exists.
	 * @param p
	 * @return true if entry exists and false if entry does not exist
	 */
	public static boolean checkExistancy(Persistent p) {
		try {
			PreparedStatement stmt = con.prepareStatement("SELECT * FROM "+ p.getTableName() +" WHERE id=?");
			stmt.setInt(1, p.getId());
			ResultSet result = stmt.executeQuery();
			if(result.getFetchSize() <=0) {
				return false;
			}else {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

}
