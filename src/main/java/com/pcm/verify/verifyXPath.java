package com.pcm.verify;

import java.util.List;

import junit.framework.Assert;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class verifyXPath {

	static List<WebElement> xpathList;
	static String attributeValue;
	static String innerText;
	
	public static boolean isfound(WebDriver driver, String xpath) throws Exception{
		
		try{
			// TODO Auto-generated method stub
			//driver.getByXPath(xpath).get(0);    htmlunit
			driver.findElement(By.xpath(xpath));
			System.out.println("[VERIFY] " + xpath + " is found in " + driver.getTitle());
			return true;
		}catch (Exception e){
			System.out.println("[VERIFY] " + xpath + " is not found in " + driver.getTitle());
			return false;
			
		}
		
		
		
		
	}

	public static int count(WebDriver driver, String xpath) throws Exception{
		
		try{
			xpathList = driver.findElements(By.xpath(xpath));
			int counter = xpathList.size();
			return counter;
			
		} catch (Exception e){
			
			Assert.fail("[PCM] Error on getting the number of xpath: " + xpath + ": " + e.getMessage());
			return 0;
			
		}
	}

	
	public static String getAttributeValue(WebDriver driver, String xpath,
			String attribute) {
		attributeValue = driver.findElement(By.xpath(xpath)).getAttribute(attribute);
			System.out.println(xpath + "attributes " + attribute + " has a value of " + attributeValue);
			return attributeValue;
				
		// TODO Auto-generated method stub
		
	}

	public static String getText(WebDriver driver, String xpath) {
		
		try{
			innerText = driver.findElement(By.xpath(xpath)).getText();
			return innerText;
		} catch (Exception e){
			Assert.fail("[CART] Unable to get the text value of xpath: " +xpath);
			return null;
		}
		
	}


	
}
