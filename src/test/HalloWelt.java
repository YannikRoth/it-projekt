package test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import globals.CardType;
import server.ServiceLocator;

public class HalloWelt {

	public static void main(String[] args) {
		//Test
		System.out.println("Hello World");
		
		Map<String, Integer> test = new HashMap<>();
		test.put("qw", 1);
		test.put("we", 3);
		
		System.out.println(test.size());
		
		List<Integer> test2 = new ArrayList<>();
		test2.add(2);
		test2.add(3);
		System.out.println(test.size());
		
		double i = Math.pow(2.00, 3.00);
		
		System.out.println(i);
		
		ArrayList<Integer> temp = new ArrayList<>();
		temp.add(2);
		temp.add(5);
		temp.add(3);
		temp.add(1);
		temp.sort(Comparator.comparing(e->e));
		for(Integer ii : temp) {
			System.out.println(ii);
		}
		
		System.out.println(ServiceLocator.getRandomNumberInRange(1, 2));
	}

}
