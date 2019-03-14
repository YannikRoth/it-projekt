package globals;

public enum CardAge {
	ONE(1), TWO(2), THREE(2);
	
	int ageValue = 0;
	
	private CardAge(int i) {
		ageValue = i;
	}

}
