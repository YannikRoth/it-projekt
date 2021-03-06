package test;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.junit.jupiter.api.Test;

import globals.ResourceMapType;
import globals.ResourceType;
import server.model.gameplay.Card;
import server.model.gameplay.Player;
import server.model.gameplay.ResourceMap;
import server.model.init.CardLoader;

class RealGamePlayTest {

	private ArrayList<Card> cardSet = new ArrayList<>();

	Map<Integer, Card> cards = CardLoader.importCards();
	private Player player = new Player("Yannik");
	
	//this list contains filtered lists of card per age
	private List<Set<Entry<Integer, Card>>> activeCards = new ArrayList<>();
	
	public RealGamePlayTest() {
		filterCards();
	}
	
	private void filterCards() {
		Map<Integer, Card> myCards = cards;
		List<Map<Integer, Card>> yearCards = new ArrayList<>();
		Map<Integer, Card> ageOne = new HashMap<>();
		Map<Integer, Card> ageTwo = new HashMap<>();
		Map<Integer, Card> ageThree = new HashMap<>();
		
		myCards.entrySet().stream()
		.filter(entry -> entry.getValue().getCardMinPlayer() <= 3)
		.forEach(entry -> {
			if(entry.getValue().getCardAgeValue() ==1) {
				ageOne.put(entry.getKey(), entry.getValue());
			}else if(entry.getValue().getCardAgeValue() == 2) {
				ageTwo.put(entry.getKey(), entry.getValue());
			}else if(entry.getValue().getCardAgeValue() == 3) {
				ageThree.put(entry.getKey(), entry.getValue());
			}
		});
		
		yearCards.add(0, ageOne);
		yearCards.add(1, ageTwo);
		yearCards.add(2, ageThree);
		
	}

	@Test
	void playFreeCard() {
		ArrayList<Card> set = new ArrayList<>();
		set.add(cards.get(2));
		player.updateCardset(set);
		
		assertTrue(player.playCard(cards.get(2)));
	}
	
	@Test
	void playCostCard() {
		ArrayList<Card> set = new ArrayList<>();
		set.add(cards.get(3));
		player.updateCardset(set);
		System.out.println(player.getCoins());
		assertTrue(player.playCard(cards.get(3)));
	}
	
	@Test
	void playCostCardWithAddCoins() {
		ArrayList<Card> set = new ArrayList<>();
		set.add(cards.get(3));
		set.add(cards.get(6));
		player.updateCardset(set);
		
		player.addCoins(3);
		assertTrue(player.playCard(cards.get(3)));
		assertTrue(player.playCard(cards.get(6)));
	}
	
	@Test
	void checkAffordWithSortedList() {
		ArrayList<Card> listcards = new ArrayList<>();
		new ArrayList<>(cards.values());
		listcards.add(cards.get(3));
		listcards.add(cards.get(25));
		player.updateCardset(listcards);
		player.addCoins(3);
		player.playCard(cards.get(3));
		player.playCard(cards.get(25));
		
		Card c = cards.get(2);
		ResourceMap rm = new ResourceMap(ResourceMapType.COST);
		rm.put(ResourceType.BRICK, 1);
		rm.put(ResourceType.STONE, 1);
		//rm.put(ResourceType.ORE, 1);
		c.setCost(rm);
		//assertTrue(player.playCard(c));
		assertTrue(player.isAbleToAffordCard(c));
	}
	
	@Test
	void checkBuildingChain() {
		ArrayList<Card> set = new ArrayList<>();
		set.add(cards.get(18));
		player.updateCardset(set);
		player.addCoins(3);
		player.playCard(cards.get(18)); //free card -> building chain to play card 51 for free
		assertTrue(player.isAbleToAffordCard(cards.get(51)));
	}
	
	@Test
	void checkTradeWithAlternateRes() {
		ArrayList<Card> set = new ArrayList<>();
		set.add(cards.get(9));//Karawanserei
		player.updateCardset(set);
		player.addCoins(2);
		
		Player playerLeft = new Player("left");
		ArrayList<Card> setLeft = new ArrayList<>();
		setLeft.add(cards.get(70));//Forstwirtschaft
		setLeft.add(cards.get(8));//S�gewerk
		playerLeft.updateCardset(setLeft);
		playerLeft.playCard(cards.get(70));
		playerLeft.playCard(cards.get(8));
		
		Player playerR = new Player("right");
		ArrayList<Card> setR = new ArrayList<>();
		setR.add(cards.get(11));//Holzplatz
		playerR.updateCardset(setR);
		playerR.playCard(cards.get(11));
		
		player.setLeftPlayer(playerLeft);
		player.setRightPlayer(playerR);
		
		assertTrue(player.isAbleToAffordCard(cards.get(9)));
	}

}
