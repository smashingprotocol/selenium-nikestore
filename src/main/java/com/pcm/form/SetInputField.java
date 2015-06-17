package com.pcm.form;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;

public class SetInputField {

	public static void byXPath(WebDriver driver, String xpath, String value) {
		// TODO Auto-generated method stub
		//htmlunit
		//final HtmlInput searchInput = driver.getFirstByXPath(xpath);
		//searchInput.setValueAttribute(value);
		
		driver.findElement(By.xpath(xpath)).clear();
		driver.findElement(By.xpath(xpath)).sendKeys(value);
		
		
		System.out.println("[INPUT] Enter a " + value + " for xpath:" + xpath + " in " + driver.getTitle());
		
	}

	public static void byXPathThenSubmit(WebDriver driver, String xpath,
			String value) {
		
		driver.findElement(By.xpath(xpath)).clear();
		driver.findElement(By.xpath(xpath)).sendKeys(value);
		driver.findElement(By.xpath(xpath)).sendKeys(Keys.ENTER);
		
		System.out.println("[INPUT] Enter and Submit " + value + " for xpath:" + xpath + " in " + driver.getTitle());
				
	} //end submitByXPath

}
