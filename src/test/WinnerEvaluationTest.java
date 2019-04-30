package test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.junit.jupiter.api.Test;

import globals.ResourceMapType;
import server.model.ServerModel;
import server.model.gameplay.Card;
import server.model.gameplay.Player;
import server.model.gameplay.ResourceMap;
import server.model.init.CardLoader;

class WinnerEvaluationTest {
	
	private ArrayList<Card> cardSetPlayer = new ArrayList<>();
	private ArrayList<Card> cardSetLeftPlayer = new ArrayList<>();
	private ArrayList<Card> cardSetRightPlayer = new ArrayList<>();
	private ServerModel model = new ServerModel();
	private Player player = new Player("Yannik");
	private Player leftPlayer = new Player ("Roman");
	private Player rightPlayer = new Player ("David");
	
	public WinnerEvaluationTest() {
		CardLoader.importCards(model);
		//seat players
		player.setLeftPlayer(leftPlayer);
		player.setRightPlayer(rightPlayer);
		leftPlayer.setLeftPlayer(player);
		leftPlayer.setRightPlayer(rightPlayer);
		rightPlayer.setLeftPlayer(leftPlayer);
		rightPlayer.setRightPlayer(player);
		//assign cards to cardSet of player
		cardSetPlayer.add(model.getCard(4));
		cardSetPlayer.add(model.getCard(5));
		cardSetPlayer.add(model.getCard(7));
		cardSetPlayer.add(model.getCard(13));
		cardSetPlayer.add(model.getCard(15));
		cardSetPlayer.add(model.getCard(18));
		//assign card to cardSet of LeftPlayer
		cardSetLeftPlayer.add(model.getCard(20));
		cardSetLeftPlayer.add(model.getCard(21));
		cardSetLeftPlayer.add(model.getCard(22));
		cardSetLeftPlayer.add(model.getCard(26));
		//assign card to cardSet of RightPlayer
		cardSetRightPlayer.add(model.getCard(27));
		cardSetRightPlayer.add(model.getCard(28));
		cardSetRightPlayer.add(model.getCard(29));
		cardSetRightPlayer.add(model.getCard(33));
		cardSetRightPlayer.add(model.getCard(50));
		
		
	}

	@Test
	void evaluateWinnerTest() {
		model.evaluateWinner(player, leftPlayer, rightPlayer);
	}

}
