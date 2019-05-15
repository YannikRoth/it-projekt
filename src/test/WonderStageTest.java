package test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import globals.ResourceType;
import server.model.gameplay.Board;
import server.model.gameplay.Card;
import server.model.gameplay.Player;
import server.model.gameplay.WorldWonder;
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
		assertTrue(testPlayer.isAbleToAffordCard(boards.get(7).getNextWorldWonderStage().getWorldWonderCard()));
		
	}

}
