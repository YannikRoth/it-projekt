package server;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import server.model.ServerModel;

public class ServiceLocator {

	private static ServiceLocator instance = null;
	private static Logger logger;
	private static ServerModel serverModelInstance = null;
	
	//hold playerId
	private static int playerCounter = 0;
	
	//card id for manual card creation
	private static int manualCardId = 1000;
	
	/**
	 * Class initialization
	 * @author yannik roth
	 */
	static {
		//init logger
		logger = Logger.getLogger(ServiceLocator.class.getName());
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat("YYY-MM-dd");
			
			FileHandler fhandler = new FileHandler("./log/serverlog"+
					dateFormat.format(Calendar.getInstance().getTime())+".log", true);
			
			logger.addHandler(fhandler);
			SimpleFormatter format = new SimpleFormatter();
			fhandler.setFormatter(format);
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		logger.info("static block of Service Locator has been executed");
	}
	
	/**
	 * Private constructor so the class can not be initialized
	 * @author yannik roth
	 */
	private ServiceLocator() {
		instance = new ServiceLocator();
	}
	
	/**
	 * Retrieve Service Locator instance
	 * Follows Singleton Pattern, creates a new one if instance is <code>null</code>.
	 * @author yannik roth
	 * @return
	 */
	public static ServiceLocator getInstance() {
		return instance == null ? new ServiceLocator() : instance;
	}
	
	/**
	 * Return logger for this service locator which is intended for server use.
	 * Log files are written to log directory within this project.
	 * Log files are not transmitted to server, as all *.log files are excluded
	 * from GIT
	 * @author yannik roth
	 * @return
	 */
	public static Logger getLogger() {
		return logger;
	}
	
	/**
	 * This method sets the current game model into the service locator.
	 * It can not be overrriden!
	 * @param Server model m
	 * @author yannik roth
	 */
	public static void setModel(ServerModel m) {
		if(serverModelInstance == null) {
			serverModelInstance = m;
		}else {
			//do not override model!
		}
	}
	
	/**
	 * Returns the server model of this game instance.
	 * @return ServerModel or <code>null </code> if instance is not set
	 */
	public static ServerModel getServerModel() {
		return ServiceLocator.serverModelInstance; 
	}
	
	/**
	 * Return a unique value on which a player can be identified
	 * @return an int of a unique id
	 */
	public static int getNewPlayerId() {
		playerCounter++;
		return playerCounter;
	}
	
	public static void updateManualCardId() {
		manualCardId++;
	}
	public static int getmanualCardId() {
		updateManualCardId();
		return manualCardId;
	}
	
	/**
	 * This is a helper method to get a random number in a specified range
	 * @param lowerBound (included)
	 * @param upperBound (included)
	 * @return A random int in the given range
	 * @author yannik roth
	 */
	public static int getRandomNumberInRange(int lowerBound, int upperBound) {
		Random rnd = new Random();
		int i = rnd.nextInt(upperBound-lowerBound)+ lowerBound;
		return i;
	}
	
}
