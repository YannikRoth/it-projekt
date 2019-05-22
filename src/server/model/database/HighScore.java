package server.model.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import globals.Translator;

/**
 * This class represents the winners table which are stored in a database table
 * @author yannik
 *
 */
public class HighScore implements Persistent{

	private static int pkCounter = 0;
	private static Connection con = ConnectionManager.getDbConnection();
	
	/**
	 * Check if table exists, if not let it be created
	 */
	static {
		DatabaseHandler.checkTable(new HighScore());
	}
	
	private int id;
	private String playerName;
	private int winningPoints;
	private LocalDate dateOfAchievment;
	
	private HighScore() {
		//for internal purposes
	}
	
	public HighScore(String playerName, int winningPoints) {
		this(playerName, winningPoints, LocalDate.now());
		
	}
	
	public HighScore(String playerName, int winningPoints, LocalDate date) {
		this.id = new PkHandler().getNextPk();
		this.playerName = playerName;
		this.winningPoints = winningPoints;
		this.dateOfAchievment = date;
	}
	
	public HighScore(int id, String playerName, int winningPoints, LocalDate date) {
		this.id = id;
		this.playerName = playerName;
		this.winningPoints = winningPoints;
		this.dateOfAchievment = date;
	}
	
	public static List<HighScore> getBestPlayers(int limit){
		List<HighScore> resultList = new ArrayList<>();;
		
		String sql = "SELECT * FROM $tableName";
		sql = sql.replace("$tableName", new HighScore().getTableName());
		try {
			PreparedStatement pstmt = con.prepareStatement(sql);
			ResultSet results = pstmt.executeQuery();
			resultList = new ArrayList<>();
			while (results.next()) {
				int i = results.getInt(1);
				String playerName = results.getString(2);
				int winningPoints = results.getInt(3);
				LocalDate dateOfAchievement = LocalDate.parse(results.getString(4));
				resultList.add(new HighScore(i, playerName, winningPoints, dateOfAchievement));
			}

			resultList = resultList.stream().sorted((p1, p2) -> p2.getWinningPoints().compareTo(p1.getWinningPoints())).limit(limit)
					.collect(Collectors.toList());

		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return resultList;
	}
	
	public void setWinningPoints(int i) {
		this.winningPoints = i;
	}
	

	public LocalDate getDateOfAchievment() {
		return this.dateOfAchievment;
	}
	
	public Integer getWinningPoints() {
		return this.winningPoints;
	}
	
	public String getPlayerName() {
		return this.playerName;
	}
	
	/**
	 * translated HighScore information
	 * @author david
	 */
	@Override
	public String toString() {
		String s = Translator.getTranslator().getString("text.winnerlist");
		s = s.replaceAll("<name>", this.playerName);
		s = s.replaceAll("<winningpoints>", Integer.toString(this.winningPoints));
		s = s.replaceAll("<date>", this.dateOfAchievment.toString());
		return s;
	}
	
	@Override
	public void savePersistent() {
		
		String sql = null;
		//check if entry exists, if yes perform update
		if(DatabaseHandler.checkExistancy(this)) {
			sql = "UPDATE $tableName SET playerName=?, winningPoints=?, dateOfAchievement=? WHERE id=?";
			sql = sql.replace("$tableName", this.getTableName());
			try {
				PreparedStatement ps = con.prepareStatement(sql);
				ps.setString(1, this.getPlayerName());
				ps.setInt(2, this.getWinningPoints());
				ps.setString(3, this.getDateOfAchievment().toString());
				ps.setInt(4, this.getId());
				ps.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}else {
			sql = "INSERT INTO $tableName(id, playerName, winningPoints, dateOfAchievement) VALUES(?,?,?,?)";
			sql = sql.replace("$tableName", this.getTableName());
			try {
				PreparedStatement ps = con.prepareStatement(sql);	
				ps.setInt(1, this.getId());
				ps.setString(2, this.getPlayerName());
				ps.setInt(3, this.getWinningPoints());
				ps.setString(4, this.getDateOfAchievment().toString());
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
		sb.append("playerName varchar(255) NOT NULL,");
		sb.append("winningPoints integer NOT NULL,");
		sb.append("dateOfAchievement date NOT NULL");
		sb.append(")");
		System.out.println(sb.toString());
		return sb.toString();
	}

	@Override
	public String getTableName() {
		return HighScore.class.getSimpleName();
	}
	
	@Override
	public int getId() {
		return this.id;
	}

	
	
}
