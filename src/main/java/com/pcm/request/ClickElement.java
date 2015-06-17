package com.pcm.request;

import junit.framework.Assert;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class ClickElement {

	public static void byXPath(WebDriver driver, String xpath) throws Exception {
		
		try{
			// TODO Auto-generated method stub
			//Htmlunit
			//HtmlElement element = (HtmlElement) driver.getByXPath(xpath).get(0);
			//element.click();

			driver.findElement(By.xpath(xpath)).click();
			
			System.out.println("[CLICK] xpath: " + xpath + " in " + driver.getTitle());
			
		} catch (Exception e){
			Assert.fail("[CLICK] Error on clicking element with xpath: " + xpath + " : " + e.getMessage());
		}
	}	
}
