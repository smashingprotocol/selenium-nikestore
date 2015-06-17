package com.pcm.form;

import java.util.Properties;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Select;

public class SetSelectField {

	public static Properties properties;
	
	public static void byXPath(WebDriver driver, String xpath, String value) {
		
		Select select = new Select(driver.findElement(By.xpath(xpath)));
		select.selectByValue(value);
		
		System.out.println("[SELECT] Selecting value from select field: " + xpath);
			
	} 
}
