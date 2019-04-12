package test;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class Translator_Test {
	static Locale locale;
	static ResourceBundle resourceBundle;
	
	public static void main(String[] args) throws MalformedURLException {
		locale = new Locale(System.getProperty("user.language"));
		System.out.println("Loaded resources for " + locale.getLanguage());
		
		String currentDir = System.getProperty("user.dir");
        System.out.println("Working Directory = " + currentDir);
		
        File file = new File(currentDir + "\\resource");
        System.out.println("User dir: " + currentDir + "\\resource");
        
        URL[] urls = {file.toURI().toURL()};
        System.out.println("User URL: " + file.toURI().toURL() );
        
        ClassLoader loader = new URLClassLoader(urls);
        resourceBundle = ResourceBundle.getBundle("locale.Translator_Test", locale, loader);
        
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
