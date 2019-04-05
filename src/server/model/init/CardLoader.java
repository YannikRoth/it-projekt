package server.model.init;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

import globals.ResourceType;
import server.ServiceLocator;
import server.model.gameplay.Card;
import server.model.gameplay.ResourceMap;

/**
 * This class needs to import all card from our db/csv and create card objects
 *
 */
public class CardLoader {
	private static Logger logger = ServiceLocator.getLogger();
	private static Map<Integer, String> field_mapping = new HashMap<>();
	
	/**
	 * Add field definitions
	 * KEY: field column
	 * VALUE: field column name
	 */
	static {
		field_mapping.put(0, "id");
		field_mapping.put(1, "cardAge");
		field_mapping.put(2, "cardName");
		field_mapping.put(3, "cardType");
		field_mapping.put(4, "minPlayer");
		field_mapping.put(5, "costInCoins");
		field_mapping.put(6, "costInZiegel");
		field_mapping.put(7, "costInErz");
		field_mapping.put(8, "costInHolz");
		field_mapping.put(9, "costInStein");
		field_mapping.put(10, "costInGlas");
		field_mapping.put(11, "costInStoff");
		field_mapping.put(12, "costInPapyrus");
		field_mapping.put(13, "freeCardName1");
		field_mapping.put(14, "freeCardName2");
		field_mapping.put(15, "victoryPoints");
		field_mapping.put(16, "militaryPoints");
		field_mapping.put(17, "coinsFromCard");
		field_mapping.put(18, "canTradeLeft");
		field_mapping.put(19, "canTradeRight");
		field_mapping.put(20, "tradeCostafter");
		field_mapping.put(21, "canTradeZiegel");
		field_mapping.put(22, "canTradeErz");
		field_mapping.put(23, "canTradeHolz");
		field_mapping.put(24, "canTradeStein");
		field_mapping.put(25, "canTradeGlas");
		field_mapping.put(26, "canTradeStoff");
		field_mapping.put(27, "canTradePapyrus");
		field_mapping.put(28, "produceZiegel");
		field_mapping.put(29, "produceErz");
		field_mapping.put(30, "produceHolz");
		field_mapping.put(31, "produceStein");
		field_mapping.put(32, "produceGlas");
		field_mapping.put(33, "produceStoff");
		field_mapping.put(34, "producePapyrus");
		field_mapping.put(35, "produceAlternate"); //probably not relevant
		field_mapping.put(36, "sciencePointsSchriften");
		field_mapping.put(37, "sciencePointsKompass");
		field_mapping.put(38, "sciencePointsMeter");
		field_mapping.put(39, "coinsfromWonderStage");
		field_mapping.put(40, "pointsFromWonderStage");
		field_mapping.put(41, "pointsFromNeigbourWonderStage");
		field_mapping.put(42, "coinsForBrownCards");
		field_mapping.put(43, "coinsForBrownNeigbourCards");
		field_mapping.put(44, "pointsForBrownCards");
		field_mapping.put(45, "pointsForNeigbourBrownCards");
		field_mapping.put(46, "coinsForGreyCards");
		field_mapping.put(47, "coinsForGreyNeigbourCards");
		field_mapping.put(48, "pointsForGreyCards");
		field_mapping.put(49, "pointsForNeigbourGreyCards");
		field_mapping.put(50, "coinsForYellowCards");
		field_mapping.put(51, "coinsForYellowNeigbourCards");
		field_mapping.put(52, "pointsForYellowCards");
		field_mapping.put(53, "pointsForNeigbourYellowCards");
		field_mapping.put(54, "pointsForPurpleCards");
		field_mapping.put(55, "pointsForNeigbourBlueCards");
		field_mapping.put(56, "pointsForNeigbourGreenCards");
		field_mapping.put(57, "pointsForNeigbourRedCards");
		field_mapping.put(58, "pointsforNeigbourDefeattoken");

		
	}
	

	/**
	 * This method imports the master data (card) from the CSV file
	 */
	public static void importCards() {
		try {
			CSVParser parser = new CSVParserBuilder()
				.withSeparator(';')
				.withIgnoreQuotations(true)
				.build();
				 
			CSVReader csvReader = new CSVReaderBuilder(new FileReader("./resource/masterdata/testCSV.csv"))
			    .withSkipLines(1) //first line is header line, do not import
			    .withCSVParser(parser)
			    .build();
			
			List<String[]> myEntries = csvReader.readAll();

			//i is the row
			for(int i = 0; i < myEntries.size(); i++) {
				//j is the column				
				for(int j = 0; j < myEntries.get(i).length; j++) {
					System.out.println(field_mapping.get(j) + ": " + myEntries.get(i)[j]);
					
					//TODO Card objects have to be created here
				}
				
				Card c = new Card(myEntries.get(i));
				logger.info("Card name " + c.getCardName());
				
				
			}

			csvReader.close();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	/**
	 * This method returns field_mapping map of card objects
	 * @param args
	 */
	public static Map<Integer, String> getFieldMapping(){
		return CardLoader.field_mapping;
	}
	
	//this is just temporary, will be deleted upon implementing server logic
	public static void main(String[] args) {
		CardLoader.importCards();
	}

}
