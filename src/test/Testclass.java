package test;

public class Testclass {

	public static void main(String[] args) {
		System.out.println("Welcome to IT-project");
		
		YannikMap<Integer> myMap = new YannikMap<Integer>(10, 12);
		
		System.out.println(myMap.get("costCoin"));

	}

}
