package test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.sun.media.jfxmedia.logging.Logger;

import globals.ResourceMapType;
import globals.ResourceType;
import server.ServiceLocator;
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
		for(int i = 0; i < model.getCards().size()-1;i++) {
			if(model.getCard(i) != null) {
				model.getCard(i).setCost(freeResourceMap);
			}
		}
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
		//play cards
		for (Card c : cardSetPlayer) {
			player.playCard(c);
		}
		System.out.println(player.getResources());
		System.out.println(player.getAlternateResources());
		for (Card c : cardSetLeftPlayer) {
			leftPlayer.playCard(c);
		}
		System.out.println(leftPlayer.getResources());
		System.out.println(leftPlayer.getAlternateResources());
		for (Card c : cardSetRightPlayer) {
			rightPlayer.playCard(c);
		}
		System.out.println(rightPlayer.getResources());
		System.out.println(rightPlayer.getAlternateResources());
	}

	@Test
	void evaluateWinnerTest() {
		System.out.println(player.getResources());
		System.out.println(leftPlayer.getResources());
		System.out.println(rightPlayer.getResources());
		List<Player> winnerList = model.evaluateWinner(player, leftPlayer, rightPlayer);
		System.out.println(player.getResources());
		System.out.println(leftPlayer.getResources());
		System.out.println(rightPlayer.getResources());
		assertEquals(winnerList.get(0).getPlayerName(), "David");
	}

}
