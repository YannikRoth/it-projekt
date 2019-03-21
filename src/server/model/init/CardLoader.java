package server.model.init;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

import server.model.gameplay.Card;

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
		field_mapping.put(2, "cardType");
		
	}
	

	/**
	 * This method imports the masterdata
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
				}
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
	
	//this is just temporary, will be deleted upon implementing server logic
	public static void main(String[] args) {
		CardLoader.importCards();
	}

}
