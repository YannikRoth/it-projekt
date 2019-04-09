package globals;

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
				locale = l;
				break;
			}
		}
		if(locale == null)
			locale = locales[0];
		
		resourceBundle = ResourceBundle.getBundle(this.getClass().getName(), locale);
		
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
