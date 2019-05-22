package server.model.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

import server.ServiceLocator;

public class ConnectionManager {

	private static Logger logger = ServiceLocator.getLogger();
	private static Connection con;
	
	/**
	 * This factory method return an SQL connection. Returns null on error
	 * @return SQL connection or null on error
	 * @author yannik roth
	 */
	public static Connection getDbConnection() {
		try {
			if(con==null) {
				con = DriverManager.getConnection("jdbc:sqlite:./store/sevenwonders.db");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return con;
	}
	

}
