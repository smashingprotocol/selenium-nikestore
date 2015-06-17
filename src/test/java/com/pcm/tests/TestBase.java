package com.pcm.tests;


import java.io.IOException;

import org.junit.AfterClass;
import org.openqa.selenium.WebDriver;


public class TestBase {
	
	public static WebDriver driver;
		
	@AfterClass
	public static void afterClass() throws InterruptedException, IOException{

		try
			{
				
				driver.close();
				driver.quit();
				//System.out.println("Closing Firefox Driver...");
			} catch (Exception e){
				System.out.println(e); 
		}
		
		
		
		
		
	}
	
	
}
