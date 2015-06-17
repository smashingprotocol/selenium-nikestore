package com.pcm.engine;

import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;

public class Config {

	
	public static WebDriver driver;
	public static Properties properties;

	

	public static void setup(String host) throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		
		driver = new FirefoxDriver();
        driver.get(host);
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
        
        System.out.println(host + " Initialized...");
        
	}
	
	
	
	public static Properties properties() throws Exception{
		try{
			FileReader reader = new FileReader("pcm.properties");
			properties = new Properties();
			properties.load(reader);
			
		} catch (Exception e){
			e.printStackTrace();
		}
		
		return properties;
		
	}
		
				
}
	
	


	

