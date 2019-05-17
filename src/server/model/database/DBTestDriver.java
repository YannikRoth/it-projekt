package server.model.database;

import java.sql.Connection;
import java.util.List;

public class DBTestDriver {

	public static void main(String[] args) {
		//System.out.println(HighScore.class.getSimpleName());
		DatabaseHandler.clearTable(HighScore.class);
		
		Connection con = ConnectionManager.getDbConnection();
		
		HighScore t1 = new HighScore("Yannik", 3);
		HighScore t2 = new HighScore("David", 6);
		HighScore t3 = new HighScore("Martin", 8);
		HighScore t4 = new HighScore("Roman", 10);
		HighScore t5 = new HighScore("Philipp", 1);
		
		t1.savePersistent();
		t2.savePersistent();
		t3.savePersistent();
		t4.savePersistent();
		t5.savePersistent();
		
		t1.setWinningPoints(100);
		t1.savePersistent();
		
		List<HighScore> winners =  HighScore.getBestPlayers(3);
		winners.forEach(e -> System.out.println(e.getPlayerName()));
		
		//DatabaseHandler.clearTable(t1.getTableName());
	}
}
