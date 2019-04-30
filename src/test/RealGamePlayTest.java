package test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import globals.ResourceMapType;
import globals.ResourceType;
import server.model.ServerModel;
import server.model.gameplay.Card;
import server.model.gameplay.Player;
import server.model.gameplay.ResourceMap;
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
	
	@Test
	void checkAffordWithSortedList() {
		player.addCoins(3);
		player.playCard(model.getCard(3));
		player.playCard(model.getCard(25));
		
		Card c = model.getCard(2);
		ResourceMap rm = new ResourceMap(ResourceMapType.COST);
		rm.put(ResourceType.BRICK, 1);
		rm.put(ResourceType.STONE, 1);
		//rm.put(ResourceType.ORE, 1);
		c.setCost(rm);
		assertTrue(player.playCard(c));
	}
	
	@Test
	void checkBuildingChain() {
		player.addCoins(3);
		player.playCard(model.getCard(18)); //free card -> building chain to play card 51 for free
		assertTrue(player.isAbleToAffordCard(model.getCard(51)));
		assertFalse(player.isAbleToAffordCard(model.getCard(53)));
	}

}
