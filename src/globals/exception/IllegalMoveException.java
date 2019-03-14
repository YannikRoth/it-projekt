package globals.exception;

/**
 * This class represents an example of a possible custom exception that our game can throw
 * @author rothy
 *
 */

public class IllegalMoveException extends Exception{

	public IllegalMoveException(Exception e) {
		super(e);
	}
	
}
