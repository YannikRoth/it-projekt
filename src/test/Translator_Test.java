package test;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class Translator_Test {
	static Locale locale = new Locale(System.getProperty("user.language"));
	static ResourceBundle resourceBundle = ResourceBundle.getBundle(Translator_Test.class.getName(), locale);
	
	public static void main(String[] args) {
		System.out.println("Loaded resources for " + locale.getLanguage());
		
		System.out.println(getString("program.menu.file"));
		
		locale = new Locale("en");
		resourceBundle = ResourceBundle.getBundle(Translator_Test.class.getName(), locale);
		System.out.println("Loaded resources for " + locale.getLanguage());
		System.out.println(getString("program.menu.file"));
		
	}
	
    public static String getString(String identifier) {
        try {
            return resourceBundle.getString(identifier);
        } catch (MissingResourceException e) {
            e.printStackTrace();
            return "";
        }
    }
}
