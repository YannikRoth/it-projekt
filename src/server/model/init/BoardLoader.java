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
import server.model.gameplay.Board;

public class BoardLoader {
	
	private static Logger logger = ServiceLocator.getLogger();
	private static Map<Integer, String> field_mapping = new HashMap<>();
	
	static {
		field_mapping.put(0,"id");
		field_mapping.put(1,"side");
		field_mapping.put(2,"boardName");
		field_mapping.put(3,"producingResource");
		field_mapping.put(4,"stage1CostinCoins");
		field_mapping.put(5,"stage1CostinZiegel");
		field_mapping.put(6,"stage1CostinErz");
		field_mapping.put(7,"stage1CostinHolz");
		field_mapping.put(8,"stage1CostinStein");
		field_mapping.put(9,"stage1CostinGlas");
		field_mapping.put(10,"stage1CostinStoff");
		field_mapping.put(11,"stage1CostinPapyrus");
		field_mapping.put(12,"stage1RewardsZiegel");
		field_mapping.put(13,"stage1RewardsErz");
		field_mapping.put(14,"stage1RewardsHolz");
		field_mapping.put(15,"stage1RewardsStein");
		field_mapping.put(16,"stage1RewardsGlas");
		field_mapping.put(17,"stage1RewardsStoff");
		field_mapping.put(18,"stage1RewardsPapyrus");
		field_mapping.put(19,"stage1RewardinCoins");
		field_mapping.put(20,"stage1RewardinPoints");
		field_mapping.put(21,"stage1RewardFreecard");
		field_mapping.put(22,"stage1RewardinMilitary");
		field_mapping.put(23,"stage1RewardsFreeCardperAge");
		field_mapping.put(24,"stage1RewardsScienceSchriften");
		field_mapping.put(25,"stage1RewardsScienceKompass");
		field_mapping.put(26,"stage1RewardsScienceMeter");
		field_mapping.put(27,"stage1StopsDiscardingCards");
		field_mapping.put(28,"stage1AllowsTradeRawMaterial");
		field_mapping.put(29,"stage1AllowsCopyPurpleCardOfNeighbour");
		field_mapping.put(30,"stage2CostinCoins");
		field_mapping.put(31,"stage2CostinZiegel");
		field_mapping.put(32,"stage2CostinErz");
		field_mapping.put(33,"stage2CostinHolz");
		field_mapping.put(34,"stage2CostinStein");
		field_mapping.put(35,"stage2CostinGlas");
		field_mapping.put(36,"stage2CostinStoff");
		field_mapping.put(37,"stage2CostinPapyrus");
		field_mapping.put(38,"stage2RewardsZiegel");
		field_mapping.put(39,"stage2RewardsErz");
		field_mapping.put(40,"stage2RewardsHolz");
		field_mapping.put(41,"stage2RewardsStein");
		field_mapping.put(42,"stage2RewardsGlas");
		field_mapping.put(43,"stage2RewardsStoff");
		field_mapping.put(44,"stage2RewardsPapyrus");
		field_mapping.put(45,"stage2RewardinCoins");
		field_mapping.put(46,"stage2RewardinPoints");
		field_mapping.put(47,"stage2RewardFreecard");
		field_mapping.put(48,"stage2RewardinMilitary");
		field_mapping.put(49,"stage2RewardsFreeCardperAge");
		field_mapping.put(50,"stage2RewardsScienceSchriften");
		field_mapping.put(51,"stage2RewardsScienceKompass");
		field_mapping.put(52,"stage2RewardsScienceMeter");
		field_mapping.put(53,"stage2StopsDiscardingCards");
		field_mapping.put(54,"stage2AllowsTradeRawMaterial");
		field_mapping.put(55,"stage2AllowsCopyPurpleCardOfNeighbour");
		field_mapping.put(56,"stage3CostinCoins");
		field_mapping.put(57,"stage3CostinZiegel");
		field_mapping.put(58,"stage3CostinErz");
		field_mapping.put(59,"stage3CostinHolz");
		field_mapping.put(60,"stage3CostinStein");
		field_mapping.put(61,"stage3CostinGlas");
		field_mapping.put(62,"stage3CostinStoff");
		field_mapping.put(63,"stage3CostinPapyrus");
		field_mapping.put(64,"stage3RewardsCoins");
		field_mapping.put(65,"stage3RewardsZiegel");
		field_mapping.put(66,"stage3RewardsErz");
		field_mapping.put(67,"stage3RewardsHolz");
		field_mapping.put(68,"stage3RewardsStein");
		field_mapping.put(69,"stage3RewardsGlas");
		field_mapping.put(70,"stage3RewardsStoff");
		field_mapping.put(71,"stage3RewardsPapyrus");
		field_mapping.put(72,"stage3RewardinCoins");
		field_mapping.put(73,"stage3RewardinPoints");
		field_mapping.put(74,"stage3RewardFreecard");
		field_mapping.put(75,"stage3RewardinMilitary");
		field_mapping.put(76,"stage3RewardsFreeCardperAge");
		field_mapping.put(77,"stage3RewardsScienceSchriften");
		field_mapping.put(78,"stage3RewardsScienceKompass");
		field_mapping.put(79,"stage3RewardsScienceMeter");
		field_mapping.put(80,"stage3StopsDiscardingCards");
		field_mapping.put(81,"stage3AllowsTradeRawMaterial");
		field_mapping.put(82,"stage3AllowsCopyPurpleCardOfNeighbour");
		field_mapping.put(83,"stage4CostinCoins");
		field_mapping.put(84,"stage4CostinZiegel");
		field_mapping.put(85,"stage4CostinErz");
		field_mapping.put(86,"stage4CostinHolz");
		field_mapping.put(87,"stage4CostinStein");
		field_mapping.put(88,"stage4CostinGlas");
		field_mapping.put(89,"stage4CostinStoff");
		field_mapping.put(90,"stage4CostinPapyrus");
		field_mapping.put(91,"stage4RewardsCoins");
		field_mapping.put(92,"stage4RewardsZiegel");
		field_mapping.put(93,"stage4RewardsErz");
		field_mapping.put(94,"stage4RewardsHolz");
		field_mapping.put(95,"stage4RewardsStein");
		field_mapping.put(96,"stage4RewardsGlas");
		field_mapping.put(97,"stage4RewardsStoff");
		field_mapping.put(98,"stage4RewardsPapyrus");
		field_mapping.put(99,"stage4RewardinCoins");
		field_mapping.put(100,"stage4RewardinPoints");
		field_mapping.put(101,"stage4RewardFreecard");
		field_mapping.put(102,"stage4RewardinMilitary");
		field_mapping.put(103,"stage4RewardsFreeCardperAge");
		field_mapping.put(104,"stage4RewardsScienceSchriften");
		field_mapping.put(105,"stage4RewardsScienceKompass");
		field_mapping.put(106,"stage4RewardsScienceMeter");
		field_mapping.put(107,"stage4StopsDiscardingCards");
		field_mapping.put(108,"stage4AllowsTradeRawMaterial");
		field_mapping.put(109,"stage4AllowsCopyPurpleCardOfNeighbour");


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
				
				Board b = new Board(myEntries.get(i));
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
	
	/**
	 * Jusrt temporary to test the importer
	 * @param args
	 */
	public static void main(String[] args) {
		BoardLoader.importBoards();
	}

	public static Map<Integer, String> getFieldMapping() {
		return field_mapping;
	}

}
