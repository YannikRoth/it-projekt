package test;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class Translator_Test {
	static Locale locale = new Locale(System.getProperty("user.language"));
	static ResourceBundle resourceBundle = ResourceBundle.getBundle(Translator_Test.class.getName(), locale);
	
	public static void main(String[] args) throws MalformedURLException {
		System.out.println("Loaded resources for " + locale.getLanguage());
		
		String currentDir = System.getProperty("user.dir");
        System.out.println("Working Directory = " + currentDir);
		
        File file = new File(currentDir);
        URL[] urls = {file.toURI().toURL()};
        ClassLoader loader = new URLClassLoader(urls);
        ResourceBundle rb = ResourceBundle.getBundle("server.Translator_Test", locale, loader);
        
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
