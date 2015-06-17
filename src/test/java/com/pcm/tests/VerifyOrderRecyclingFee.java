package com.pcm.tests;

import java.io.IOException;
import java.util.Properties;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Test;

import com.pcm.engine.Config;
import com.pcm.includes.Cart;
import com.pcm.includes.Search;
import com.pcm.includes.SignIn;
import com.pcm.request.ClickElement;
import com.pcm.utility.StatusLog;
import com.pcm.utility.TakeScreenShot;
import com.pcm.verify.verifyXPath;



public class VerifyOrderRecyclingFee {

	public String sku;
	public String qty;
	public String email;
	public String password;
	public String env;
	
	
	Properties sys = System.getProperties();
	
	
	@Test
	public void Verify_Order_Recycling_Fee() throws Exception{
		
		try{
			
			//setup driver,properties and includes
			Properties pr = Config.properties(); //create a method for the pcm.properies
			Config.setup(pr.getProperty("TEST_HOST"));
			env = sys.getProperty("pcmHost"); //prod or stage
			
			//set test data
			sku = pr.getProperty("SEARCH_SKU_RECYCFEE");
			qty = "1";
			email = pr.getProperty("CHECKOUT_USER_EMAIL_" + env);
			password = pr.getProperty("CHECKOUT_USER_PASSWORD");
			
			//Login user via header
			SignIn.login(Config.driver,email,password);
			Assert.assertTrue("User is logged in (Sign out link is display)", verifyXPath.isfound(Config.driver,pr.getProperty("HEADER_LINK_SIGNOUT_XPATH")));
			
			//Clear the cart first.
			Cart.clearcart(Config.driver);
			
			//Search for the Sku to checkout and add to cart
			Search.keyword(Config.driver,sku);
			Search.addtocart(Config.driver,sku,qty);
			System.out.println("[CART] " + Config.driver.getTitle());
			
			ClickElement.byXPath(Config.driver,pr.getProperty("CART_LINK_XPATH"));
			System.out.println("[CART] " + Config.driver.getTitle());
			verifyXPath.isfound(Config.driver, "//input[@class='cartQty cart-qty']");
			ClickElement.byXPath(Config.driver,pr.getProperty("CART_BTN_CHECKOUT_XPATH"));
			
			
			Assert.assertTrue("CA Recycling Fee label is found at Verify Order", verifyXPath.isfound(Config.driver,pr.getProperty("CHECKOUT_LABEL_RECYCFEE_XPATH")));
			
			StatusLog.printlnPassedResult(Config.driver,"[PCM] Verify Order Recycling Fee");
		
		} catch (Exception e){
			StatusLog.printlnFailedResult(Config.driver,"[PCM] Verify Order Recycling Fee");
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
