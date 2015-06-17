package com.pcm.includes;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Properties;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.pcm.engine.Config;
import com.pcm.form.SetInputField;
import com.pcm.form.SetSelectField;
import com.pcm.request.ClickElement;
import com.pcm.utility.TakeScreenShot;
import com.pcm.verify.verifyXPath;

public class NikeStore {

	public static Properties properties;
	
	public static void addToCart(WebDriver driver, String size, String qty, String email, String password) throws FileNotFoundException {
		
		FileReader reader = new FileReader("properties.properties");
		properties = new Properties();
		
		try{
			properties.load(reader);
			boolean addCartBtnFound = verifyXPath.isfound(driver, properties.getProperty("BTN_ADDTOCART_XPATH"));
			
			if(addCartBtnFound){
				
				//make the select field visible and select a size.
				WebElement sizeSelect = driver.findElement(By.xpath(properties.getProperty("SELECT_SIZE_XPATH")));
				JavascriptExecutor jse=(JavascriptExecutor) driver;
				jse.executeScript("arguments[0].setAttribute('style', 'display: block;')",sizeSelect);
				SetSelectField.byXPath(driver, properties.getProperty("SELECT_SIZE_XPATH"), size);
				System.out.println("[NIKE] Clicking Add to Cart...");
				ClickElement.byXPath(driver, properties.getProperty("BTN_ADDTOCART_XPATH"));
				ClickElement.byXPath(driver, properties.getProperty("BTN_LOGINMODAL_XPATH"));
				SetInputField.byXPath(driver, properties.getProperty("INPUT_EMAIL_XPATH"), email);
				SetInputField.byXPath(driver, properties.getProperty("INPUT_PASSWORD_XPATH"), password);
				
				ClickElement.byXPath(driver, properties.getProperty("BTN_LOGIN_XPATH"));
				
				TakeScreenShot.CurrentPage(Config.driver, "Nikeaddtocart");
			} //end if
			
		} catch (Exception e){
			System.out.println("[NIKE] Error in clicking Add to Cart");
		}
		
		
		
		
	}

	public static String storeCurrentURL(WebDriver driver) throws Exception {
		FileReader reader = new FileReader("properties.properties");
		properties = new Properties();
		properties.load(reader);
		
		boolean addCartBtnFound = verifyXPath.isfound(driver, properties.getProperty("BTN_ADDTOCART_XPATH"));
		
		if(addCartBtnFound){
			return driver.getCurrentUrl();
		}
		//return null;
		return driver.getCurrentUrl();
	}

}
