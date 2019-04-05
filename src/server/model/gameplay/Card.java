package server.model.gameplay;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import globals.CardAge;
import globals.CardType;
import globals.ResourceMapType;
import globals.ResourceType;
import server.ServiceLocator;
import server.model.init.CardLoader;

/**
 * Represent a card in the system
 * @author rothy
 *
 */

public class Card implements Serializable{
	
	private Logger logger = ServiceLocator.getLogger();
	
	//resource maps for cost and production
	private Map<ResourceType, Integer> cost = new ResourceMap(ResourceMapType.COST);;
	private Map<ResourceType, Integer> produce = new ResourceMap(ResourceMapType.PRODUCE);
	
	//special card information
	private int winningPoints = 0;
	private int militaryPoints = 0;
	
	//further attributes concerning gameplay
	private int id;
	private CardAge cardAge;
	private String cardName;
	private CardType cardType;
	private int minPlayer;
	
	//free cards because of this card
	private List<String> freeCards = new ArrayList<>();
	
	//special card enablers
	private boolean canTradeLeft;
	private boolean canTradeRight;
	private boolean canTradeZiegel;
	private boolean canTradeErz;
	private boolean canTradeHolz;
	private boolean canTradeStein;
	private boolean canTradeGlas;
	private boolean canTradeStoff;
	private boolean canTradePapyrus;
	private boolean produceAlternate;
	
	//provides science points
	private boolean sciencePointsSchriften;
	private boolean sciencePointsKompass;
	private boolean sciencePointsMeter;
	
	//special gilden functions
	private int coinsfromWonderStage;
	private int pointsFromWonderStage;
	private int pointsFromNeigbourWonderStage;
	private int coinsForBrownCards;
	private int coinsForBrownNeigbourCards;
	private int pointsForBrownCards;
	private int pointsForNeigbourBrownCards;
	private int coinsForGreyCards;
	private int coinsForGreyNeigbourCards;
	private int pointsForGreyCards;
	private int pointsForNeigbourGreyCards;
	private int coinsForYellowCards;
	private int coinsForYellowNeigbourCards;
	private int pointsForYellowCards;
	private int pointsForNeigbourYellowCards;
	private int pointsForPurpleCards;
	private int pointsForNeigbourBlueCards;
	private int pointsForNeigbourGreenCards;
	private int pointsForNeigbourRedCards;
	private int pointsforNeigbourDefeattoken;


	
	private int tradeCostafter;

	
	@Deprecated
	public Card(ResourceMap cost, ResourceMap produce, CardAge age, int minPlayer) {
		this.cost = cost;
		this.produce = produce;
		this.cardAge = age;
		this.minPlayer = minPlayer;
		
	}

	/**
	 * Constructor of card. Will iterate through all fieldMappings.
	 * @param values array in the same order as the masterdata, usually given by default
	 */
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
			if(fieldName.equals("costInZiegel")) {
				this.cost.put(ResourceType.BRICK, Integer.parseInt(values[i]));
				System.out.println("Cost brick is: " + cost.get(ResourceType.BRICK));
				continue;
			}
			if(fieldName.equals("costInErz")) {
				this.cost.put(ResourceType.ORE, Integer.parseInt(values[i]));
				continue;
			}
			if(fieldName.equals("costInHolz")) {
				this.cost.put(ResourceType.WOOD, Integer.parseInt(values[i]));
				continue;
			}
			if(fieldName.equals("costInStein")) {
				this.cost.put(ResourceType.STONE, Integer.parseInt(values[i]));
				continue;
			}
			if(fieldName.equals("costInGlas")) {
				this.cost.put(ResourceType.GLAS, Integer.parseInt(values[i]));
				continue;
			}
			if(fieldName.equals("costInStoff")) {
				this.cost.put(ResourceType.FABRIC, Integer.parseInt(values[i]));
				continue;
			}
			if(fieldName.equals("costInPapyrus")) {
				this.cost.put(ResourceType.PAPYRUS, Integer.parseInt(values[i]));
				continue;
			}
			//TODO free card need to be added after all cards have been initialized
			if(fieldName.equals("freeCardName1")) {
				this.freeCards.add(values[i]);
				continue;
			}
			if(fieldName.equals("freeCardName2")) {
				this.freeCards.add(values[i]);
				continue;
			}
			if(fieldName.equals("militaryPoints")) {
				this.militaryPoints = Integer.parseInt(values[i]);
				continue;
			}
			if(fieldName.equals("coinsFromCard")) {
				this.produce.put(ResourceType.COIN, Integer.parseInt(values[i]));
				continue;
			}
			if(fieldName.equals("canTradeLeft")){
				this.canTradeLeft = values[i] == "true" ? true : false;
				continue;
			}
			if(fieldName.equals("canTradeRight")){
				this.canTradeRight = values[i] == "true" ? true : false;
				continue;
			}
			if(fieldName.equals("tradeCostafter")){
				//TODO if set to zero (0), it has no effect in trading
				this.tradeCostafter = Integer.parseInt(values[i]);
				continue;
			}
			if(fieldName.equals("canTradeZiegel")){
				this.canTradeZiegel = values[i] == "true" ? true : false;
				continue;
			}
			if(fieldName.equals("canTradeErz")){
				this.canTradeErz = values[i] == "true" ? true : false;
				continue;
			}
			if(fieldName.equals("canTradeHolz")){
				this.canTradeHolz = values[i] == "true" ? true : false;
				continue;
			}
			if(fieldName.equals("canTradeStein")){
				this.canTradeStein = values[i] == "true" ? true : false;
				continue;
			}
			if(fieldName.equals("canTradeGlas")){
				this.canTradeGlas = values[i] == "true" ? true : false;
				continue;
			}
			if(fieldName.equals("canTradeStoff")){
				this.canTradeStoff = values[i] == "true" ? true : false;
				continue;
			}
			if(fieldName.equals("canTradePapyrus")){
				this.canTradePapyrus = values[i] == "true" ? true : false;
				continue;
			}
			if(fieldName.equals("produceZiegel")){
				this.produce.put(ResourceType.BRICK, Integer.parseInt(values[i]));
				continue;
			}
			if(fieldName.equals("produceErz")){
				this.produce.put(ResourceType.ORE, Integer.parseInt(values[i]));
				continue;
			}
			if(fieldName.equals("produceHolz")){
				this.produce.put(ResourceType.WOOD, Integer.parseInt(values[i]));
				continue;
			}
			if(fieldName.equals("produceStein")){
				this.produce.put(ResourceType.STONE, Integer.parseInt(values[i]));
				continue;
			}
			if(fieldName.equals("produceGlas")){
				this.produce.put(ResourceType.GLAS, Integer.parseInt(values[i]));
				continue;
			}
			if(fieldName.equals("produceStoff")){
				this.produce.put(ResourceType.FABRIC, Integer.parseInt(values[i]));
				continue;
			}
			if(fieldName.equals("producePapyrus")){
				this.produce.put(ResourceType.PAPYRUS, Integer.parseInt(values[i]));
				continue;
			}
			if(fieldName.equals("produceAlternate")){
				this.produceAlternate = values[i] == "true" ? true : false;
				continue;
			}
			if(fieldName.equals("sciencePointsSchriften")){
				this.sciencePointsSchriften = values[i].equals("true") ? true : false;
				continue;
			}
			if(fieldName.equals("sciencePointsKompass")){
				this.sciencePointsKompass = values[i].equals("true") ? true : false;
				continue;
			}
			if(fieldName.equals("sciencePointsMeter")){
				this.sciencePointsMeter = values[i].equals("true") ? true : false;
				continue;
			}
			if(fieldName.equals("coinsfromWonderStage")){
				this.coinsfromWonderStage = Integer.parseInt(values[i]);
				continue;
				}
			if (fieldName.equals("pointsFromWonderStage")) {
				this.pointsFromWonderStage = Integer.parseInt(values[i]);
				continue;
			}
			if (fieldName.equals("pointsFromNeigbourWonderStage")) {
				this.pointsFromNeigbourWonderStage = Integer.parseInt(values[i]);
				continue;
			}
			if (fieldName.equals("coinsForBrownCards")) {
				this.coinsForBrownCards = Integer.parseInt(values[i]);
				continue;
			}
			if (fieldName.equals("coinsForBrownNeigbourCards")) {
				this.coinsForBrownNeigbourCards = Integer.parseInt(values[i]);
				continue;
			}
			if (fieldName.equals("pointsForBrownCards")) {
				this.pointsForBrownCards = Integer.parseInt(values[i]);
				continue;
			}
			if (fieldName.equals("pointsForNeigbourBrownCards")) {
				this.pointsForNeigbourBrownCards = Integer.parseInt(values[i]);
				continue;
			}
			if (fieldName.equals("coinsForGreyCards")) {
				this.coinsForGreyCards = Integer.parseInt(values[i]);
				continue;
			}
			if (fieldName.equals("coinsForGreyNeigbourCards")) {
				this.coinsForGreyNeigbourCards = Integer.parseInt(values[i]);
				continue;
			}
			if (fieldName.equals("pointsForGreyCards")) {
				this.pointsForGreyCards = Integer.parseInt(values[i]);
				continue;
			}
			if (fieldName.equals("pointsForNeigbourGreyCards")) {
				this.pointsForNeigbourGreyCards = Integer.parseInt(values[i]);
				continue;
			}
			if (fieldName.equals("coinsForYellowCards")) {
				this.coinsForYellowCards = Integer.parseInt(values[i]);
				continue;
			}
			if (fieldName.equals("coinsForYellowNeigbourCards")) {
				this.coinsForYellowNeigbourCards = Integer.parseInt(values[i]);
				continue;
			}
			if (fieldName.equals("pointsForYellowCards")) {
				this.pointsForYellowCards = Integer.parseInt(values[i]);
				continue;
			}
			if (fieldName.equals("pointsForNeigbourYellowCards")) {
				this.pointsForNeigbourYellowCards = Integer.parseInt(values[i]);
				continue;
			}
			if (fieldName.equals("pointsForPurpleCards")) {
				this.pointsForPurpleCards = Integer.parseInt(values[i]);
				continue;
			}
			if (fieldName.equals("pointsForNeigbourBlueCards")) {
				this.pointsForNeigbourBlueCards = Integer.parseInt(values[i]);
				continue;
			}
			if (fieldName.equals("pointsForNeigbourGreenCards")) {
				this.pointsForNeigbourGreenCards = Integer.parseInt(values[i]);
				continue;
			}
			if (fieldName.equals("pointsForNeigbourRedCards")) {
				this.pointsForNeigbourRedCards = Integer.parseInt(values[i]);
				continue;
			}
			if (fieldName.equals("pointsforNeigbourDefeattoken")) {
				this.pointsforNeigbourDefeattoken = Integer.parseInt(values[i]);
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
	
	/**
	 * This method provides production information about this specific card
	 * @return the production map  of this card as a ResouceMap
	 */
	public Map<ResourceType, Integer> getProduction(){
		return this.produce;
	}
	
	/**
	 * This method returns a cost item from the cost map
	 * @param t ResourceType
	 * @return Integer value of the amount of this resource
	 */
	public Integer getCostValue(ResourceType t) {
		return this.cost.get(t);
	}
	
	/**
	 * This method returns a production item from the produce map
	 * @param t ResourceType
	 * @return Integer value of the amount of this resource
	 */
	public Integer getProducationValue(ResourceType t) {
		return this.produce.get(t);
	}
	
	public String getCardName() {
		return this.cardName;
	}

}
