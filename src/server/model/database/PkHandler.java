package server.model.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PkHandler implements Persistent{

	private int pkid;
	private Connection con = ConnectionManager.getDbConnection();
	
	static {
		DatabaseHandler.checkTable(new PkHandler());
	}
	
	public int getNextPk() {
		int nextPk = 0;
		String sql = "SELECT nextPk FROM $tableName";
		sql = sql.replace("$tableName", new PkHandler().getTableName());
		try {
			PreparedStatement pstmt = con.prepareStatement(sql);
			ResultSet results = pstmt.executeQuery();
			while (results.next()) {
				nextPk = results.getInt(1);
			}
			
			this.pkid = nextPk;
			this.pkid++;
			this.savePersistent();

		} catch (SQLException e) {
			nextPk = 0;
			e.printStackTrace();
		}
		
		return nextPk;
	}
	
	@Override
	public void savePersistent() {
		String sql = null;
		//check if entry exists, if yes perform update
		if(DatabaseHandler.checkExistancy(this)) {
			sql = "UPDATE $tableName SET nextPk=? WHERE id=?";
			sql = sql.replace("$tableName", this.getTableName());
			try {
				PreparedStatement ps = con.prepareStatement(sql);
				ps.setInt(1, this.pkid);
				ps.setInt(2, this.getId());
				ps.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}else {
			sql = "INSERT INTO $tableName(id, nextPk) VALUES(?,?)";
			sql = sql.replace("$tableName", this.getTableName());
			try {
				PreparedStatement ps = con.prepareStatement(sql);	
				ps.setInt(1, this.getId());
				ps.setInt(2, this.pkid);
				ps.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
	}

	@Override
	public String getCreateStatements() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("CREATE TABLE IF NOT EXISTS " + this.getTableName());
		sb.append("(");
		sb.append("id integer PRIMARY KEY,");
		sb.append("nextPk integer");
		sb.append(")");
		System.out.println(sb.toString());
		return sb.toString();
	}

	@Override
	public String getTableName() {
		return this.getClass().getSimpleName();
	}

	@Override
	public int getId() {
		return 0;
	}

}
