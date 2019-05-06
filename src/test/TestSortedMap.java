package test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.Set;
import java.util.TreeSet;

public class TestSortedMap {
	
	public static void main(String[] args) {
		Map<String, Integer> myMap = new HashMap<>();
		myMap.put("b", 2);
		myMap.put("a", 4);
		myMap.put("c", 1);
		myMap.put("d", 5);
		System.out.println(myMap.size());		
		
		ArrayList<String> sortedList = new ArrayList<>();
		for(Entry<String, Integer> e : myMap.entrySet()) {
			System.out.println(e.getKey() +"->"+ e.getValue());
			sortedList.add(e.getValue()+"_"+e.getKey());
		}
		sortedList.sort(new Comparator<String>() {

			@Override
			public int compare(String o1, String o2) {
				return o1.compareTo(o2);
			}


		});
		
		for(String ss : sortedList) {
			System.out.println(ss);
		}
		
	}

}
