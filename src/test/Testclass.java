package test;

import java.util.ArrayList;

public class Testclass {

	public static void main(String[] args) {
		System.out.println("Welcome to IT-project");
		
		YannikMap<Integer> myMap = new YannikMap<Integer>(10, 12);
		
		System.out.println(myMap.get("costCoin"));
		
		ArrayList<String> test = new ArrayList<>();
		test.add("A");
		test.add("B");
		test.add("C");
		test.add("D");
		
		test.add(0, null);
		test.set(0, test.get(test.size()-1));
		test.remove(test.size()-1);
		
		test.add(test.get(0));
		test.remove(0);
		
		System.out.println(test);
		
		
		
		

	}

}
