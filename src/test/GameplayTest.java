package test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import globals.ResourceMapType;
import globals.ResourceType;
import server.model.ServerModel;
import server.model.gameplay.Card;
import server.model.gameplay.Player;
import server.model.gameplay.ResourceMap;
import server.model.init.CardLoader;

/**
 * 
 * @author yannik roth
 *
 */
class GameplayTest {

	/**
	 * Initializing variables
	 */
	private ServerModel model;
	private Player player;
	private Card[] testCardSet = new Card[7];
	
	
	public GameplayTest() {
		model = new ServerModel();
		CardLoader.importCards(model);
		player = new Player("Yannik");
		this.testCardSet[0] = model.getCard(1); //brick OR stone OR ore OR wood
		this.testCardSet[1] = model.getCard(2); // 1fabric
		this.testCardSet[2] = model.getCard(3); // brick OR ore
		this.testCardSet[3] = model.getCard(10); //1 brick
		this.testCardSet[4] = model.getCard(16); //1 glas
		this.testCardSet[5] = model.getCard(32); //1 wood or ore
		this.testCardSet[6] = model.getCard(58); //1 glas or fabric or paper
		ResourceMap rm = new ResourceMap(ResourceMapType.COST);
		rm.put(ResourceType.BRICK, 0);
		this.testCardSet[6].setCost(rm);
	}
	
//	@Test
//	void printAllCards() {
//		for(Entry<Integer, Card> c : model.getCards().entrySet()) {
//			System.out.println(c.getKey() + " -> " + c.getValue().getCardName());
//		}
//	}
	
	@Test
	void checkNotEnoughResources() {
		//this card has a cost of 1 glas, player currently has no resources
		assertFalse(player.isAbleToAffordCard(model.getCards().get(4)));
	}
	
	@Test
	void checkEnoughResources() {
		addResourcesToPlayer();
		assertTrue(player.isAbleToAffordCard(model.getCards().get(4)));
	}
	
	@Test
	void checkCoins() {
		addResourcesToPlayer();
		assertFalse(player.isAbleToAffordCard(model.getCards().get(3)));
		
	}
	
	@Test
	void checkAlternateEnoughResources1() {
		addResourcesToPlayer();
		Card c = addFakeCardToPlayer();
		assertTrue(player.isAbleToAffordCard(c));
		
	}
	
	@Test
	void checkAlternateEnoughResources2() {
		addResourcesToPlayer();
		Card c = addFakeCardToPlayer2();
		assertTrue(player.isAbleToAffordCard(c));
		
	}
	
	@Test
	void checkAlternateEnoughResources3() {
		addResourcesToPlayer();
		Card c = addFakeCardToPlayer3();
		assertTrue(player.isAbleToAffordCard(c));
		
	}
	
	@Test
	void checkAlternateEnoughResources4() {
		addResourcesToPlayer();
		Card c = addFakeCardToPlayer4();
		assertTrue(player.isAbleToAffordCard(c));
		
	}


	private void addResourcesToPlayer() {
		for(Card c : testCardSet) {
			this.player.playCard(c);
		}
	}
	
	private Card addFakeCardToPlayer() {
		Card fc = model.getCard(100);
		ResourceMap cost = new ResourceMap(ResourceMapType.COST);
		cost.put(ResourceType.STONE, 1);
		cost.put(ResourceType.BRICK, 2);
		fc.setCost(cost);
		return fc;
	}
	
	private Card addFakeCardToPlayer2() {
		Card fc = model.getCard(101);
		ResourceMap cost = new ResourceMap(ResourceMapType.COST);
		cost.put(ResourceType.STONE, 1);
		cost.put(ResourceType.BRICK, 1);
		fc.setCost(cost);
		return fc;
	}
	
	private Card addFakeCardToPlayer3() {
		Card fc = model.getCard(102);
		ResourceMap cost = new ResourceMap(ResourceMapType.COST);
		cost.put(ResourceType.FABRIC, 1);
		cost.put(ResourceType.BRICK, 2);
		cost.put(ResourceType.WOOD, 1);
		fc.setCost(cost);
		return fc;
	}
	
	private Card addFakeCardToPlayer4() {
		Card fc = model.getCard(103);
		ResourceMap cost = new ResourceMap(ResourceMapType.COST);
		cost.put(ResourceType.FABRIC, 1);
		cost.put(ResourceType.BRICK, 2);
		cost.put(ResourceType.WOOD, 2);
		fc.setCost(cost);
		return fc;
	}

}
