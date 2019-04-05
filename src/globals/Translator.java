package globals;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import server.ServiceLocator;

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
	
    public String getString(String identifier) {
        try {
            return resourceBundle.getString(identifier);
        } catch (MissingResourceException e) {
            logger.warning("translation for " + identifier + " in \"" + locale.getLanguage() + "\" not found");
            return "***";
        }
    }
}
