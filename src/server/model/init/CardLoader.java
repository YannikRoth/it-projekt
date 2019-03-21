package server.model.init;

import java.util.HashMap;
import java.util.Map;

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
		field_mapping.put(1, "cardName");
		field_mapping.put(2, "cardType");
	}
	

	
	public static void importCards() {
		
		//loop though all the fields in the CSV
		//true must be replaced
		
		CSVReader reader = new CSVReader(new FileReader("yourfile.csv"));
		
		Scanner scanner = new Scanner(new File("/Users/pankaj/abc.csv"));
        scanner.useDelimiter(",");
        while(scanner.hasNext()){
            System.out.print(scanner.next()+"|");
        }
        scanner.close();
		
		while(true) {
			Card c = new Card(null, null, null, 0);
		}
		
	}

}
