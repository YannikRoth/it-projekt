package server;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class ServiceLocator {

	private static ServiceLocator instance = null;
	private static Logger logger;
	
	
	static {
		//init logger
		logger = Logger.getLogger(ServiceLocator.class.getName());
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat("YYY-MM-dd");
			
			FileHandler fhandler = new FileHandler("./log/serverlog"+
					dateFormat.format(Calendar.getInstance().getTime())+".log");
			
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
	
	public static void main(String[] args) {
		System.out.println("This is test");
		ServiceLocator.logger.info("Hello Yannik");
		logger.info("asdasd");
	}
	
	
}
