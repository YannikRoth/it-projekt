package test;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import globals.ResourceMapType;
import globals.ResourceType;
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
		//create freeCost Resourcemap
		ResourceMap freeResourceMap = new ResourceMap(ResourceMapType.COST);
		freeResourceMap.put(ResourceType.BRICK, 0);
		freeResourceMap.put(ResourceType.COIN, 0);
		freeResourceMap.put(ResourceType.FABRIC, 0);
		freeResourceMap.put(ResourceType.GLAS, 0);
		freeResourceMap.put(ResourceType.ORE, 0);
		freeResourceMap.put(ResourceType.PAPYRUS, 0);
		freeResourceMap.put(ResourceType.STONE, 0);
		freeResourceMap.put(ResourceType.WOOD, 0);
		//make every Card free of costs
		/*
		for(int i = 0; i < model.getCards().size()-1;i++) {
			if(model.getCard(i) != null) {
				model.getCard(i).setCost(freeResourceMap);
			}
		}
		*/
		//assign cards to cardSet of player
		//cardSetPlayer.add(model.getCard(3));
		//cardSetPlayer.add(model.getCard(11));
		//cardSetPlayer.add(model.getCard(25));
		//cardSetPlayer.add(model.getCard(42));
		//cardSetPlayer.add(model.getCard(85));
		//player.getPlayableCards().add(model.getCard(3));
		//player.getPlayableCards().add(model.getCard(11));
		//player.getPlayableCards().add(model.getCard(25));
		//player.getPlayableCards().add(model.getCard(42));
		player.getPlayableCards().add(model.getCard(15));

		
		//assign card to cardSet of LeftPlayer
		cardSetLeftPlayer.add(model.getCard(3));
		//cardSetLeftPlayer.add(model.getCard(11));
		leftPlayer.getPlayableCards().add(model.getCard(3));
		//leftPlayer.getPlayableCards().add(model.getCard(11));
		//assign card to cardSet of RightPlayer
		//cardSetRightPlayer.add(model.getCard(6));
		//cardSetRightPlayer.add(model.getCard(42));
		//rightPlayer.getPlayableCards().add(model.getCard(6));
		//rightPlayer.getPlayableCards().add(model.getCard(42));
		
		//play cards
		for (Card c : cardSetPlayer) {
			player.playCard(c);
		}
		
		//System.out.println(player.getResources());
		//System.out.println(player.getAlternateResources());
		for (Card c : cardSetLeftPlayer) {
			leftPlayer.playCard(c);
		}

		for (Card c : cardSetRightPlayer) {
			rightPlayer.getPlayableCards().add(c);
			rightPlayer.playCard(c);
		}
	
	}
/*
	@Test
	void evaluateWinnerTest() {
		
		//System.out.println(player.getResources());
		//System.out.println(leftPlayer.getResources());
		//System.out.println(rightPlayer.getResources());
		List<Player> players = new ArrayList<>();
		players.add(player);
		players.add(leftPlayer);
		players.add(rightPlayer);
		List<Player> winnerList = model.evaluateWinner(players);
		//System.out.println(player.getResources());
		//System.out.println(leftPlayer.getResources());
		//System.out.println(rightPlayer.getResources());
		assertEquals(winnerList.get(0).getPlayerName(), "Yannik");
	}
	*/
	
	@Test
	void evaluateMissingResources() {
		System.out.println(player.getResources());
		System.out.println(leftPlayer.getResources());
		System.out.println(leftPlayer.getAlternateResources());
		System.out.println(rightPlayer.getResources());
		System.out.println(rightPlayer.getAlternateResources());
		player.addCoins(20);
		assertTrue(player.playCard(model.getCard(15)));
		System.out.println(player.getResources());
		System.out.println(leftPlayer.getResources());
		System.out.println(rightPlayer.getResources());
	}

}
