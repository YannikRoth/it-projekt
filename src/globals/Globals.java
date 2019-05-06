package globals;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import server.ServiceLocator;

/**
 * This class holds all globals values and enums which are required throughout the project
 * @author rothy
 *
 */
public class Globals {
	
	private static int portNr = 8080;
	private static String defaultIPAddr = "127.0.0.1";
	
	public static String getDefaultIPAddr() {
		return defaultIPAddr;
	}
	public static void setDefaultIPAddr(String defaultIPAddr) {
		Globals.defaultIPAddr = defaultIPAddr;
	}
	public static void setPortNr(int portNumb) {
		portNr = portNumb > 0 ? portNumb : 8080;
		ServiceLocator.getLogger().info("Set port to: " + portNr);
	}
	public static int getPortNr() {
		return portNr;
	}
	
	/**
	 * Helper method to sort a map by value (must be integer)
	 * @param absoluteAmountAlternatingResources
	 * @return an ArrayList with Resourcetypes sorted by value ASC
	 * Info: The way this calculation is performed is not very 'nice' but it works!
	 * @author yannik roth
	 */
	public static ArrayList<ResourceType> sortMapByValue(Map<ResourceType, ? extends Integer> absoluteAmountAlternatingResources) {
		
		Map<ResourceType, Integer> tempReqResources = new HashMap<>();
		ArrayList<String> sortedReqResources = new ArrayList<>();
		for(ResourceType t : absoluteAmountAlternatingResources.keySet()) {
				tempReqResources.put(t, absoluteAmountAlternatingResources.get(t));
				sortedReqResources.add(tempReqResources.get(t) + "_"+t.toString());

		}
		//now sort the array
		sortedReqResources.sort(new Comparator<String>() {

			@Override
			public int compare(String o1, String o2) {
				return o1.compareTo(o2);
			}
			
		});
		
		ArrayList<ResourceType> requiredResources = new ArrayList<>();
		for(String s : sortedReqResources) {
			String[] temp = s.split("_");
			requiredResources.add(ResourceType.valueOf(temp[1]));
		}
		
		return requiredResources;
	}

}
