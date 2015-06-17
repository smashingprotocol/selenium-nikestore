package com.pcm.tests;

import java.io.IOException;
import java.util.Properties;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Test;

import com.pcm.engine.Config;
import com.pcm.includes.Cart;
import com.pcm.utility.StatusLog;
import com.pcm.utility.TableContainer;
import com.pcm.utility.TakeScreenShot;
import com.pcm.verify.verifyXPath;



public class ShoppingCartAdditionalInCartSavingsPriceDisplay {

	public String sku;
	public String withSlashed;
	public String withMIR;
	public String priceStrike;
	public String unitPrice;
	public String FinalPrice;
	public String qty;
	public String email;
	public String password;
	public String env;
	public boolean testStatus;
	public String edpno;
	public String cartOrderTotal;
	
	
	
	Properties sys = System.getProperties();
	
	
	@Test
	public void Shopping_Cart_Additional_InCart_Savings_Price_Display() throws Exception{
		
		try{
			
			//setup driver,properties and includes
			Properties pr = Config.properties(); //create a method for the pcm.properies
			Config.setup(pr.getProperty("TEST_HOST"));
			env = sys.getProperty("pcmHost"); //prod or stage
			
			//Define properties
			qty = "1";
			email = pr.getProperty("CHECKOUT_USER_EMAIL_" + env);
			password = pr.getProperty("CHECKOUT_USER_PASSWORD");
	
			//Get the data in the excel and quick add the skus
			int rowCtr = TableContainer.getRowCount();
			
			for(int i = 1; i <= rowCtr; i++){
				
				sku = TableContainer.getCellValue(i, "sku");
				withSlashed = TableContainer.getCellValue(i, "withSlashed");
				withMIR = TableContainer.getCellValue(i, "withMIR");
				priceStrike = TableContainer.getCellValue(i, "priceStrike");
				unitPrice = TableContainer.getCellValue(i, "unitPrice");
				FinalPrice = TableContainer.getCellValue(i, "FinalPrice");
				
				//Quick add the sku
				Cart.clearcart(Config.driver);
				Cart.quickaddsku(Config.driver, sku);
				
				//Verify the Strikethrough Price
				testStatus = verifyXPath.isfound(Config.driver, "//p[@class='listPrice priceStrike' and contains(text(),'" + priceStrike + "')]");
				Assert.assertTrue("[CART] Expected Strikethrough Price " + priceStrike + " is display.",testStatus);
				
				//Verify the Unit Price
				testStatus = verifyXPath.isfound(Config.driver, "//p[@class='unitPrice' and contains(text(),'" + unitPrice + "')]");
				Assert.assertTrue("[CART] Expected Unit Price of " + unitPrice + " is display.",testStatus);
				
				//Verify the Mail-in Rebate label
				if(Boolean.valueOf(withMIR)){
					
					testStatus = verifyXPath.isfound(Config.driver, pr.getProperty("CART_LABEL_MAILINREBATE_XPATH"));
					Assert.assertTrue("[CART] Mail-in Rebate label is display.",testStatus);
				} //end if
				
				//Verify the Final Price
				testStatus = verifyXPath.isfound(Config.driver, "//p[@class='itemTotalPrice' and contains(text(),'" + FinalPrice + "')]");
				Assert.assertTrue("[CART] Expected Final Price of " + FinalPrice + " is display.",testStatus);
				
				
				
			} //end for
			
			StatusLog.printlnPassedResult(Config.driver,"[PCM] Shopping Cart Additional InCart Savings Price Display");
		
		} catch (Exception e){
			StatusLog.printlnFailedResult(Config.driver,"[PCM] Shopping Cart Additional InCart Savings Price Display");
			Assert.fail(e.getMessage());
		}
	}
	

	@AfterClass
	public static void quit() throws IOException{
		TakeScreenShot.CurrentPage(Config.driver, "Last Page Test Result");
		Config.driver.close();
		Config.driver.quit();
		
	}
	
	
}
