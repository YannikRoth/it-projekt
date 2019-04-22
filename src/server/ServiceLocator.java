package server;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import server.model.ServerModel;

public class ServiceLocator {

	private static ServiceLocator instance = null;
	private static Logger logger;
	private static ServerModel serverModelInstance = null;
	
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
}
