package com.pcm.tests;

import java.io.IOException;
import java.util.Properties;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Test;

import com.pcm.engine.Config;
import com.pcm.includes.Cart;
import com.pcm.includes.Checkout;
import com.pcm.includes.Search;
import com.pcm.includes.SignIn;
import com.pcm.request.ClickElement;
import com.pcm.utility.StatusLog;
import com.pcm.utility.TakeScreenShot;
import com.pcm.verify.verifyXPath;



public class VerifyOrderShippingFeeUpdate {

	public String freeshipsku;
	public String sku;
	public String qty;
	public String email;
	public String password;
	public String env;
	public boolean testStatus;
	public String edpno;
	public String cartOrderTotal;
	public String verifyOrderSubtTotal;
	public static boolean isfreeshipping;
	
	Properties sys = System.getProperties(); //Get property values in the build.xml junit tagged.
	
	
	@Test
	public void Verify_Order_Shipping_Fee_Update() throws Exception{
		
		try{
			
			//setup driver,properties and includes
			Properties pr = Config.properties(); //create a method for the pcm.properies
			Config.setup(pr.getProperty("TEST_HOST"));
			env = sys.getProperty("pcmHost"); //prod or stage
			
			//set test data
			freeshipsku = pr.getProperty("SEARCH_SKU_FREESHIP");
			sku = pr.getProperty("SEARCH_SKU_FLATSCREEN");
			qty = "1";
			email = pr.getProperty("CHECKOUT_USER_TAX_EMAIL_" + env);
			password = pr.getProperty("CHECKOUT_USER_PASSWORD");
			
			//Login user via header
			SignIn.login(Config.driver,email,password);
			testStatus = verifyXPath.isfound(Config.driver,pr.getProperty("HEADER_LINK_SIGNOUT_XPATH"));
			Assert.assertTrue("User is logged in (Sign out link is display)",testStatus);
			
			//Search SKU and Add to Cart.
			Cart.clearcart(Config.driver);
			Search.keyword(Config.driver, sku);
			Search.addtocart(Config.driver, sku, qty);
			
			Search.keyword(Config.driver, freeshipsku);
			Search.addtocart(Config.driver, freeshipsku, qty);
			
			//Proceed to checkout
			Cart.navigate(Config.driver);
			Cart.proceedtocheckout(Config.driver);
			
			//Select each shipping method and compute Grand Total
			isfreeshipping = verifyXPath.isfound(Config.driver, pr.getProperty("CHECKOUT_LABEL_FREESHIP_XPATH")); //Verify the Free label in shipping method amount.
			Assert.assertFalse("[CHECKOUT] Free Shipping Method is not found.",isfreeshipping);
			
			Checkout.deleteItem(Config.driver,sku);
			
			//Verify the Shipping method is found after deleting the no free shipping item
			isfreeshipping = verifyXPath.isfound(Config.driver, pr.getProperty("CHECKOUT_LABEL_FREESHIP_XPATH")); //Verify the Free label in shipping method amount.
			
			Assert.assertTrue("[CHECKOUT] Free Shipping Method is found.",isfreeshipping);
			StatusLog.printlnPassedResult(Config.driver,"[PCM] Verify Order Cart Calculate Shipping");
		
		} catch (Exception e){
			StatusLog.printlnFailedResult(Config.driver,"[PCM] Verify Order Cart Calculate Shipping");
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
