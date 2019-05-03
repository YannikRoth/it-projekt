package test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
	
	//this list contains filtered lists of card per age
	private List<Set<Entry<Integer, Card>>> activeCards = new ArrayList<>();
	
	public RealGamePlayTest() {
		cardSet.add(model.getCard(2)); //free card produces fabric
		filterCards();
	}
	
	private void filterCards() {
		Map<Integer, Card> myCards = model.getCards();
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
