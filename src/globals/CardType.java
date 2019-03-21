package globals;

/**
 * This enum holds all card types.
 * @author rothy
 *
 */
public enum CardType {
	
	RAWMATERIAL(CardColor.BROWN), MANUFACTURING(CardColor.GREY), PROFAN(CardColor.GREEN), 
	RESEARCH(CardColor.GREEN), TRADE(CardColor.YELLOW), MILITARY(CardColor.RED), GILDEN(CardColor.VIOLET);
	
	/**
	 * private inner enum for card color of a card
	 * @author rothy
	 *
	 */
	private enum CardColor {
		BROWN, GREY, BLUE, GREEN, YELLOW, RED, VIOLET
	}
	
	private CardColor color = null;
	
	private CardType(CardColor c) {
		color = c;
	}
	
	public CardColor getColor() {
		return this.color;
	}
	


}