package com.pcm.includes;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Properties;

import junit.framework.Assert;

import org.openqa.selenium.WebDriver;

import com.pcm.request.ClickElement;
import com.pcm.verify.verifyXPath;

public class PDP {

	public static Properties properties;
	
	public String getEDPNo(WebDriver driver, String sku) throws FileNotFoundException {
		
		FileReader reader = new FileReader("pcm.properties");
		properties = new Properties();
		
		Search search = new Search();
		String EDP;
		
		
		try{
			
			properties.load(reader);
			
			search.keyword(driver, sku);
			//click the desription in search result of sku
			ClickElement.byXPath(driver, properties.getProperty("SEARCH_LINK_PDP_XPATH"));
			EDP = verifyXPath.getAttributeValue(driver, properties.getProperty("PDP_INPUT_EDPNO_XPATH"), "value");
			
			return EDP;
			
		} catch (Exception e){
			Assert.fail("[PDP] Error in getting the EDPNo in PDP" + e.getMessage());
			return null;
		} 
		
		
	}

}
