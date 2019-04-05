package server.model.init;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

import server.ServiceLocator;
import server.model.gameplay.Card;

public class BoardLoader {
	
	private static Logger logger = ServiceLocator.getLogger();
	private static Map<Integer, String> field_mapping = new HashMap<>();
	
	static {
		field_mapping.put(0,"id");
		field_mapping.put(1,"boardName");
		field_mapping.put(2,"producingRessource");
		field_mapping.put(3,"stage1CostinCoins");
		field_mapping.put(4,"stage1CostinZiegel");
		field_mapping.put(5,"stage1CostinErz");
		field_mapping.put(6,"stage1CostinHolz");
		field_mapping.put(7,"stage1CostinStein");
		field_mapping.put(8,"stage1CostinGlas");
		field_mapping.put(9,"stage1CostinStoff");
		field_mapping.put(10,"stage1CostinPapyrus");
		field_mapping.put(11,"stage1RewardsRawMaterial");
		field_mapping.put(12,"stage1RewardsBasicMaterial");
		field_mapping.put(13,"stage1RewardinPoints");
		field_mapping.put(14,"stage1RewardFreecard");
		field_mapping.put(15,"stage1RewardinMilitary");
		field_mapping.put(16,"stage1RewardsFreeCardperAge");
		field_mapping.put(17,"stage1RewardsSciencePoint");
		field_mapping.put(18,"stage1StopsDiscardingCards");
		field_mapping.put(19,"stage1AllowsTradeRawMaterial");
		field_mapping.put(20,"stage1PointsForPurpleNeigbourCards");
		field_mapping.put(21,"stage2CostinCoins");
		field_mapping.put(22,"stage2CostinZiegel");
		field_mapping.put(23,"stage2CostinErz");
		field_mapping.put(24,"stage2CostinHolz");
		field_mapping.put(25,"stage2CostinStein");
		field_mapping.put(26,"stage2CostinGlas");
		field_mapping.put(27,"stage2CostinStoff");
		field_mapping.put(28,"stage2CostinPapyrus");
		field_mapping.put(29,"stage2RewardsRawMaterial");
		field_mapping.put(30,"stage2RewardsBasicMaterial");
		field_mapping.put(31,"stage2RewardinPoints");
		field_mapping.put(32,"stage2RewardFreecard");
		field_mapping.put(33,"stage2RewardinMilitary");
		field_mapping.put(34,"stage2RewardsFreeCardperAge");
		field_mapping.put(35,"stage2RewardsSciencePoint");
		field_mapping.put(36,"stage2StopsDiscardingCards");
		field_mapping.put(37,"stage2AllowsTradeRawMaterial");
		field_mapping.put(38,"stage2PointsForPurpleNeigbourCards");
		field_mapping.put(39,"stage3CostinCoins");
		field_mapping.put(40,"stage3CostinZiegel");
		field_mapping.put(41,"stage3CostinErz");
		field_mapping.put(42,"stage3CostinHolz");
		field_mapping.put(43,"stage3CostinStein");
		field_mapping.put(44,"stage3CostinGlas");
		field_mapping.put(45,"stage3CostinStoff");
		field_mapping.put(46,"stage3CostinPapyrus");
		field_mapping.put(47,"stage3RewardsRawMaterial");
		field_mapping.put(48,"stage3RewardsBasicMaterial");
		field_mapping.put(49,"stage3RewardinPoints");
		field_mapping.put(50,"stage3RewardFreecard");
		field_mapping.put(51,"stage3RewardinMilitary");
		field_mapping.put(52,"stage3RewardsFreeCardperAge");
		field_mapping.put(53,"stage3RewardsSciencePoint");
		field_mapping.put(54,"stage3StopsDiscardingCards");
		field_mapping.put(55,"stage3AllowsTradeRawMaterial");
		field_mapping.put(56,"stage3PointsForPurpleNeigbourCards");
	}
	
	public static void importBoards() {
		try {
			CSVParser parser = new CSVParserBuilder()
				.withSeparator(';')
				.withIgnoreQuotations(true)
				.build();
				 
			CSVReader csvReader = new CSVReaderBuilder(new FileReader("./resource/masterdata/board.csv"))
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
				
				//TODO create board obj.
				//Card c = new Card(myEntries.get(i));
				//logger.info("Card name " + c.getCardName());
				
				
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

}
