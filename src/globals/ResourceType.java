package globals;

import java.io.Serializable;
import java.util.List;

public enum ResourceType implements Serializable {
	WOOD, STONE, BRICK, ORE, PAPYRUS, FABRIC, GLAS, COIN, WINNINGPOINTS, MILITARYPLUSPOINTS, MILITARYMINUSPOINTS;

	public String toStringTranslate() {
		Translator t = Translator.getTranslator();
		return t.getString("column." + this.name().toLowerCase());
	}
}
