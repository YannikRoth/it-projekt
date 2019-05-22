package test;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Map;

import org.junit.jupiter.api.Test;

import globals.ResourceType;
import server.model.gameplay.Board;
import server.model.gameplay.Card;
import server.model.gameplay.Player;
import server.model.init.BoardLoader;
import server.model.init.CardLoader;

class WonderStageTest {

	@Test
	void test() {
		Map<Integer, Card> cards = CardLoader.importCards();
		Map<Integer, Board> boards = BoardLoader.importBoards();
		
		Player testPlayer = new Player("Testplayer");
		testPlayer.getResources().put(ResourceType.STONE, 1); // simulates our board
		
		//cards to verify
		Card c = cards.get(8); //forstwirtschaft
		Card c2 = cards.get(16); //glashütte
		
		//init the game play
		ArrayList<Card> pCards = new ArrayList<>();
		pCards.add(c);
		pCards.add(c2);
		testPlayer.updateCardset(pCards);
		
		//play the cards
		testPlayer.playCard(c);
		testPlayer.playCard(c2);
		
		//test to build wonder stage
		assertTrue(testPlayer.isAbleToAffordCard(boards.get(7).getNextWorldWonderStage(testPlayer).getWorldWonderCard()));
		
	}
	
	@Test
	void test_build_worldwonder_with_trade() {
		Map<Integer, Card> cards = CardLoader.importCards();
		Map<Integer, Board> boards = BoardLoader.importBoards();
		
		Player testPlayer1 = new Player("Testplayer 1");
		testPlayer1.getResources().put(ResourceType.STONE, 1);
		Player testPlayer2 = new Player("Testplayer 2");
		testPlayer2.getResources().put(ResourceType.STONE, 1);
		Player testPlayer3 = new Player("Testplayer 3");
		testPlayer3.getResources().put(ResourceType.STONE, 1);
		
		testPlayer1.setLeftPlayer(testPlayer3);	testPlayer1.setRightPlayer(testPlayer2);
		testPlayer2.setLeftPlayer(testPlayer1);	testPlayer2.setRightPlayer(testPlayer3);
		testPlayer3.setLeftPlayer(testPlayer2);	testPlayer3.setRightPlayer(testPlayer1);
		
		//test to build wonder stage
		assertTrue(testPlayer1.isAbleToAffordCard(boards.get(7).getNextWorldWonderStage(testPlayer1).getWorldWonderCard()));
		assertTrue(testPlayer2.isAbleToAffordCard(boards.get(7).getNextWorldWonderStage(testPlayer2).getWorldWonderCard()));
		assertTrue(testPlayer3.isAbleToAffordCard(boards.get(7).getNextWorldWonderStage(testPlayer3).getWorldWonderCard()));
	}

}
