package server.model.init;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

import globals.ResourceType;
import server.model.gameplay.Card;
import server.model.gameplay.ResourceMap;

/**
 * This class needs to import all card from our db/csv and create card objects
 *
 */
public class CardLoader {
	
	private static Map<Integer, String> field_mapping = new HashMap<>();
	
	/**
	 * Add field definitions
	 */
	static {
		field_mapping.put(0, "id");
		field_mapping.put(1, "cardAge");
		field_mapping.put(2, "cardName");
		field_mapping.put(3, "cardType");
		field_mapping.put(4, "minPlayer");
		
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
