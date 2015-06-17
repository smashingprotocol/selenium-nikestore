package com.pcm.tests;

import java.io.IOException;
import java.util.Properties;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Test;

import com.pcm.engine.Config;
import com.pcm.includes.Cart;
import com.pcm.includes.Search;
import com.pcm.request.ClickElement;
import com.pcm.utility.StatusLog;
import com.pcm.utility.TakeScreenShot;
import com.pcm.verify.verifyXPath;



public class VerifyOrderSavedCreditCard {

	public String sku;
	public String qty;
	public String email;
	public String password;
	public String env;
	public boolean testStatus;
	
	Properties sys = System.getProperties();
	
	
	@Test
	public void Verify_Order_Saved_CreditCard() throws Exception{
		
		try{
			
			//setup driver,properties and includes
			Properties pr = Config.properties(); //create a method for the pcm.properies
			Config.setup(pr.getProperty("TEST_HOST"));
			env = sys.getProperty("pcmHost"); //prod or stage
			
			//set test data
			sku = pr.getProperty("SEARCH_SKU_DFLT");
			qty = "1";
			email = pr.getProperty("CHECKOUT_USER_SAVEDCC_EMAIL");
			password = pr.getProperty("CHECKOUT_USER_PASSWORD");
			
			//Search for the Sku to checkout and add to cart
			Search.keyword(Config.driver,sku);
			Search.addtocart(Config.driver,sku,qty);
			System.out.println("[CART] " + Config.driver.getTitle());
			
			ClickElement.byXPath(Config.driver,pr.getProperty("CART_LINK_XPATH"));
			System.out.println("[CART] " + Config.driver.getTitle());
			verifyXPath.isfound(Config.driver, "//input[@class='cartQty cart-qty']"); //Verify the shopping cart item quantity field.
			
			Cart.existingLogin(Config.driver, email, password); //Login user in cart.
			
			//Verify the Card Numbe field is not display
			testStatus = verifyXPath.isfound(Config.driver,pr.getProperty("CHECKOUT_INPUT_CARDNO_XPATH"));
			Assert.assertFalse("[CHECKOUT] Credit Card field should not be display.",testStatus);
			
			//Verify the Security code field is display
			testStatus = verifyXPath.isfound(Config.driver,pr.getProperty("CHECKOUT_INPUT_SECCODE_XPATH"));
			Assert.assertTrue("[CHECKOUT] Security Code for Saved Card is found.",testStatus);
			
			
			StatusLog.printlnPassedResult(Config.driver,"[PCM] Verify Order Saved Credit Cart");
		
		} catch (Exception e){
			StatusLog.printlnFailedResult(Config.driver,"[PCM] Verify Order Saved Credit Cart");
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
