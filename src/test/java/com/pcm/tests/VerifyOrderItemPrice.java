package com.pcm.tests;

import java.io.IOException;
import java.util.Properties;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Test;

import com.pcm.engine.Config;
import com.pcm.includes.Cart;
import com.pcm.includes.Checkout;
import com.pcm.includes.SignIn;
import com.pcm.utility.StatusLog;
import com.pcm.utility.TakeScreenShot;
import com.pcm.verify.verifyXPath;



public class VerifyOrderItemPrice {

	public String qty;
	public String email;
	public String password;
	public String env;
	public boolean testStatus;
	public String edpno;
	public String cartOrderTotal;
	public String verifyOrderSubtTotal;
	
	Properties sys = System.getProperties(); //Get property values in the build.xml junit tagged.
	
	
	@Test
	public void Verify_Order_Item_Price() throws Exception{
		
		try{
			
			//setup driver,properties and includes
			Properties pr = Config.properties(); //create a method for the pcm.properies
			Config.setup(pr.getProperty("TEST_HOST"));
			env = sys.getProperty("pcmHost"); //prod or stage
			
			//set test data
			String[] sku = new String[]{pr.getProperty("SEARCH_SKU_YOUSAVE"),pr.getProperty("SEARCH_SKU_IPAD")}; //set the property with multiple values
			

			qty = "1";
			email = pr.getProperty("CHECKOUT_USER_TAX_EMAIL_" + env);
			password = pr.getProperty("CHECKOUT_USER_PASSWORD");
			
			//Login user via header
			SignIn.login(Config.driver,email,password);
			testStatus = verifyXPath.isfound(Config.driver,pr.getProperty("HEADER_LINK_SIGNOUT_XPATH"));
			Assert.assertTrue("User is logged in (Sign out link is display)",testStatus);
			
			
			int ctr = 2; //number of sku to test.
			for(int i = 0; i<ctr; i++ ){
				
				//Go to cart and Clear the cart then quick add sku.
				Cart.clearcart(Config.driver);
				Cart.quickaddsku(Config.driver,sku[i]);
				
				//If item goes to Engraving (ipad) page.
				if(verifyXPath.isfound(Config.driver,pr.getProperty("CART_BTN_BUYNOWWITHOUTENRAVE_XPATH"))){
					Cart.addIpodWithoutEngraving(Config.driver);
				}
				
				
				//Get the Cart Order Total
				cartOrderTotal = verifyXPath.getText(Config.driver,pr.getProperty("CART_LABEL_ORDERTOTAL_XPATH"));

				//Proceed to checkout
				Cart.proceedtocheckout(Config.driver); 
				
				//Verify the Cart Total Order is the Sub Total in Checkout's Order Review
				cartOrderTotal = cartOrderTotal.replaceAll("[$, ]", "");
				verifyOrderSubtTotal = verifyXPath.getText(Config.driver, pr.getProperty("CHECKOUT_LABEL_ORSUBTOTAL_XPATH")).replaceAll("[$, ]", "");
				
				Assert.assertEquals(verifyOrderSubtTotal,cartOrderTotal);
				
				System.out.println("[VERIFY ] Verify Order Sub Total of  " + verifyOrderSubtTotal + " matches cart Sub Total of " + cartOrderTotal);
				
				StatusLog.printlnPassedResult(Config.driver,"[PCM] Verify Order Item Price: " + sku[i]);
				
				//Go back to Shopping Cart
				Checkout.returntoCart(Config.driver);
				
			}
			
			
		
		} catch (Exception e){
			StatusLog.printlnFailedResult(Config.driver,"[PCM] Verify Order Item Price");
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
