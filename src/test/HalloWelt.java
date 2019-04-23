package test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	}

}
