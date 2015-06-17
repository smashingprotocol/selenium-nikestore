package com.pcm.includes;

import java.io.FileReader;
import java.util.Properties;

import org.junit.Assert;
import org.openqa.selenium.WebDriver;
import com.pcm.form.SetInputField;
import com.pcm.request.ClickElement;

public class Search {

	public static Properties properties;
	
	public static void keyword(WebDriver driver,String keyword) throws Exception {
		
		FileReader reader = new FileReader("pcm.properties");
		properties = new Properties();
		properties.load(reader);

		SetInputField.byXPath(driver,properties.getProperty("SEARCH_INPUT_XPATH"),keyword);
		ClickElement.byXPath(driver,properties.getProperty("SEARCH_LINK_XPATH"));
		
	}

	public static void addtocart(WebDriver driver, String sku, String qty) throws Exception{
		
		try{
		// Enter Quantity, Click Add to cart and Proceed to cart button on the modal
		SetInputField.byXPath(driver,"//input[@id='addCartQty" + sku + "']",qty);
		ClickElement.byXPath(driver,"//a[@id='addCartButtonFor" + sku + "']"); // Click the Add to cart button in the search.
		
		} catch (Exception e){
			Assert.fail("[SEARCH] ADD ITEM TO CART...FAILED" + e.getMessage());
		}
	}

}
