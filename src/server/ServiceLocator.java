package server;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import globals.Translator;

public class ServiceLocator {

	private static ServiceLocator instance = null;
	private static Logger logger;
	private static Translator translator;
	
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
		
		translator = Translator.getTranslator();
		
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
	 * @return
	 */
	public static Logger getLogger() {
		return logger;
	}
	
	public static Translator getTranslator() {
		return translator;
	}
}
