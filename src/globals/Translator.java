package globals;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import server.ServiceLocator;
/**
 * Class for translation in User-Interfaces
 * Based on system language
 * @author david
 *
 */
public class Translator {
	// Supported languages for translations
	final Locale[] locales = new Locale[] { 
			new Locale("de"),//first is the default language
			new Locale("en")
	};
	private static Logger logger = ServiceLocator.getLogger();
	private static Locale defaultLocale;
	private static Locale locale;
	private static Translator t;
	private static ResourceBundle resourceBundle;
	
	public static Translator getTranslator() {
		if(t == null)
			t = new Translator();
		return t;
	}
	
	private Translator() {
		setLanguage(System.getProperty("user.language"));
	}
	
	public static Locale getDefaultLocale() {
		return defaultLocale;
	}
	
	/**
	 * Set the new language in parameter, if the programm supports the language
	 * if the programm don't support, default language will be german (de)
	 * @author david
	 * @param Language (de, en, ...)
	 */
	public void setLanguage(String Language) {
		for (Locale l : locales) {
			if(Language.equalsIgnoreCase(l.getLanguage()))
			{
				if(locale == null)
					defaultLocale = l;
				locale = l;
				break;
			}
		}
		if(locale == null)
		{
			locale = locales[0];
			defaultLocale = locale;
		}
		
		
		String currentDir = System.getProperty("user.dir");
		File file = new File(currentDir + "\\resource");
		URL[] urls = new URL[1];
		try {
			urls[0] = file.toURI().toURL();
		} catch (MalformedURLException e) {
			logger.warning("Can't create URL from UserDir: " + file.getAbsolutePath());
		}
		ClassLoader loader = new URLClassLoader(urls);
		resourceBundle = ResourceBundle.getBundle("locale.Translator", locale, loader);
		
		logger.info("Loaded resources for \"" + locale.getLanguage() + "\"");
	}
	
	/**
	 * 
	 * @param As parameter the identifier of *.properties-Files is to use
	 * @return the text in setup language
	 * @author david
	 */
    public String getString(String identifier) {
        try {
            return resourceBundle.getString(identifier);
        } catch (MissingResourceException e) {
            logger.warning("translation for " + identifier + " in \"" + locale.getLanguage() + "\" not found");
            return "***";
        }
    }
    
    public Locale getLocale() {
    	return this.locale;
    }
}
