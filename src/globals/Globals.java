package globals;

import server.ServiceLocator;

/**
 * This class holds all globals values and enums which are required throughout the project
 * @author rothy
 *
 */
public class Globals {
	
	private static int portNr = 8080;
	
	public static void setPortNr(int portNumb) {
		portNr = portNumb > 0 ? portNumb : 8080;
		ServiceLocator.getLogger().info("Set port to: " + portNr);
	}
	public static int getPortNr() {
		return portNr;
	}

}
