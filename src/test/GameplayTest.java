package test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Map.Entry;

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
	private Card[] testCardSet = new Card[4];
	
	
	public GameplayTest() {
		model = new ServerModel();
		CardLoader.importCards(model);
		player = new Player("Yannik");
		this.testCardSet[0] = model.getCard(1); //brick OR stone OR ore wood
		this.testCardSet[1] = model.getCard(2); // fabric
		this.testCardSet[2] = model.getCard(3); // brick OR ore
		this.testCardSet[3] = model.getCard(16); //1 glas
		
	}
	
	@Test
	void printAllCards() {
		for(Entry<Integer, Card> c : model.getCards().entrySet()) {
			System.out.println(c.getKey() + " -> " + c.getValue().getCardName());
		}
	}
	
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
	
	private void addResourcesToPlayer() {
		for(Card c : testCardSet) {
			this.player.playCard(c);
		}
	}
	

}
