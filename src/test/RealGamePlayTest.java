package test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import server.model.ServerModel;
import server.model.gameplay.Card;
import server.model.gameplay.Player;
import server.model.init.CardLoader;

class RealGamePlayTest {

	private ArrayList<Card> cardSet = new ArrayList<>();
	private ServerModel model = new ServerModel();
	private Player player = new Player("Yannik");
	
	public RealGamePlayTest() {
		CardLoader.importCards(model);
		cardSet.add(model.getCard(2)); //free card produces fabric
	}
	
	@Test
	void playFreeCard() {
		assertTrue(player.playCard(model.getCard(2)));
	}
	
	@Test
	void playCostCard() {
		assertFalse(player.playCard(model.getCard(3)));
	}
	
	@Test
	void playCostCardWithAddCoins() {
		player.addCoins(3);
		assertTrue(player.playCard(model.getCard(3)));
		assertTrue(player.playCard(model.getCard(6)));
	}

}
