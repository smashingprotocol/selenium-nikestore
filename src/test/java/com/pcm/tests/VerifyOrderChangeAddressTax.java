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

public class VerifyOrderChangeAddressTax {

	public String sku;
	public String qty;
	public String email;
	public String password;
	public String env;
	public boolean testStatus;
	
	Properties sys = System.getProperties();
	
	@Test
	public void Verify_Order_Change_Address_Tax() throws Exception{
		
		try{
			
			//setup driver,properties and includes
			Properties pr = Config.properties(); //create a method for the pcm.properies
			Config.setup(pr.getProperty("TEST_HOST"));
			env = sys.getProperty("pcmHost"); //prod or stage
			
			//set test data
			sku = pr.getProperty("SEARCH_SKU_DFLT");
			qty = "1";
			email = pr.getProperty("CHECKOUT_USER_TAX_EMAIL_" + env); //Set the email user from prod or stage
			
			password = pr.getProperty("CHECKOUT_USER_PASSWORD");
			
			//Login user via header
			SignIn.login(Config.driver,email,password);
			Assert.assertTrue("User is logged in (Sign out link is display)", verifyXPath.isfound(Config.driver,pr.getProperty("HEADER_LINK_SIGNOUT_XPATH")));
			
			//Clear the cart first.
			Cart.clearcart(Config.driver);
			
			//Search sku and add to cart
			Search.keyword(Config.driver,sku);
			Search.addtocart(Config.driver,sku,qty);
			System.out.println("[CART] " + Config.driver.getTitle());
			
			//Navigate shopping cart
			Cart.navigate(Config.driver); 
			Cart.proceedtocheckout(Config.driver);
			
			
			//Change the Address in Verify Order
			
			//Tax amount is 0 for Non Tax state
			Checkout.changeShipAddbyZipcode(Config.driver,pr.getProperty("CHECKOUT_ZIP_NONTAX")); //Change Address to non tax state.
			testStatus = verifyXPath.isfound(Config.driver,pr.getProperty("CHECKOUT_LABEL_NOTAX_XPATH")); //Verify Estimated Sales Tax amount is NOT 0.
			Assert.assertTrue("Estimated Sales Tax 0", testStatus);
			StatusLog.printlnPassedResult(Config.driver,"[PCM] Verify Order Change Address to Non Tax");
			
			//Tax amount is 0 for APO
			Checkout.changeShipAddbyZipcode(Config.driver,pr.getProperty("CHECKOUT_ZIP_APO")); //Change Address to non tax state.
			testStatus = verifyXPath.isfound(Config.driver,pr.getProperty("CHECKOUT_LABEL_NOTAX_XPATH")); //Verify Estimated Sales Tax amount is NOT 0.
			Assert.assertTrue("Estimated Sales Tax 0", testStatus);
			StatusLog.printlnPassedResult(Config.driver,"[PCM] Verify Order Change Address to APO Tax");
			
			//Tax amount is not 0
			Checkout.changeShipAddbyZipcode(Config.driver,pr.getProperty("CHECKOUT_ZIP_RECYCFEE")); //Change Address to non tax state.
			testStatus = verifyXPath.isfound(Config.driver,pr.getProperty("CHECKOUT_LABEL_TAX_XPATH")); //Verify Estimated Sales Tax amount is 0.
			
			Assert.assertTrue("Estimated Sales is not 0", testStatus);
			StatusLog.printlnPassedResult(Config.driver,"[PCM] Verify Order Change Address with Tax");
		
		} catch (Exception e){
			StatusLog.printlnFailedResult(Config.driver,"[PCM] Verify Order Change Address Tax");
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
