package server.model.gameplay;

import java.io.Serializable;
import java.util.Map;

import globals.CardAge;
import globals.CardType;
import globals.ResourceMapType;
import globals.ResourceType;
import server.model.init.CardLoader;

/**
 * Represent a card in the system
 * @author rothy
 *
 */

public class Card implements Serializable{
	
	private Map<ResourceType, Integer> cost = new ResourceMap(ResourceMapType.COST);;
	private Map<ResourceType, Integer> produce = new ResourceMap(ResourceMapType.PRODUCE);
	
	private int id;
	private CardAge cardAge;
	private String cardName;
	private CardType cardType;
	private int minPlayer;
	
	@Deprecated
	public Card(ResourceMap cost, ResourceMap produce, CardAge age, int minPlayer) {
		this.cost = cost;
		this.produce = produce;
		this.cardAge = age;
		this.minPlayer = minPlayer;
		
	}
	
	public Card(String[] values) {
		Map<Integer, String> mapping = CardLoader.getFieldMapping();
		
		for(int i = 0; i< values.length; i++) {
			String fieldName = mapping.get(i);
			if(fieldName.equals("id")) {
				this.id = Integer.parseInt(values[i]);
				System.out.println(this.id);
				continue;
			}
			if(fieldName.equals("cardAge")) {
				for(CardAge cA : CardAge.values()) {
					if(cA.getAgeValue() == Integer.parseInt(values[i])){
						this.cardAge = cA;
					}
				}
				System.out.println(this.cardAge);
				continue;
			}
			if(fieldName.equals("cardName")) {
				this.cardName = values[i];
				System.out.println(cardName);
				continue;
			}
			if(fieldName.equals("cardType")) {
				this.cardType = CardType.valueOf(values[i]);
				System.out.println(this.cardType);
				continue;
			}
			if(fieldName.equals("minPlayer")) {
				this.minPlayer = Integer.parseInt(values[i]);
				System.out.println(minPlayer);
				continue;
			}
			if(fieldName.equals("costInCoins")) {
				this.cost.put(ResourceType.COIN, Integer.parseInt(values[i]));
				System.out.println("Cost is: " + cost.get(ResourceType.COIN));
				continue;
			}
			//TODO needs to be completely implemented
			//important: Field must exist in static block, otherwise it will fail
			
		}
	}
	
	/**
	 * Required for import --> select correct Array-Index
	 * @return int value of CardAge
	 */
	public int getCardAgeValue() {
		return this.cardAge.getAgeValue();
	}
	
	/**
	 * This method provides cost information about this specific card
	 * @return the cost of this card as a ResourceMap
	 */
	public Map<ResourceType, Integer> getCost(){
		return this.cost;
	}

}
