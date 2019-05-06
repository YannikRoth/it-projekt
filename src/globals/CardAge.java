package globals;

import java.io.Serializable;

public enum CardAge implements Serializable {
	ONE(1, "LEFT"), TWO(2, "RIGHT"), THREE(3, "LEFT");
	
	private int ageValue = 0;
	private String turnDirection;
	
	private CardAge(int i, String s) {
		ageValue = i;
		turnDirection = s;
	}
	
	public int getAgeValue() {
		return this.ageValue;
	}
	
	public String getTurnDirection() {
		return this.turnDirection;
	}

}
