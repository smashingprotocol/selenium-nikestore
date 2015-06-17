package com.pcm.tests;

import java.io.IOException;
import java.util.Properties;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Test;

import com.pcm.engine.Config;
import com.pcm.includes.Cart;
import com.pcm.includes.Checkout;
import com.pcm.includes.PDP;
import com.pcm.includes.Search;
import com.pcm.includes.SignIn;
import com.pcm.utility.StatusLog;
import com.pcm.utility.TakeScreenShot;
import com.pcm.verify.verifyXPath;

public class VerifyOrderReviewQtyValidation {

	public String email;
	public String password;
	public String env;
	public boolean testStatus;
	public String edpno;
	public String cartOrderTotal;
	public String verifyOrderSubtTotal;
	public String sku;
	public String qty;
	public String zipCode;
	public String itemid;
	
	
	Properties sys = System.getProperties(); //Get property values in the build.xml junit tagged.
	
	
	@Test
	public void Verify_Order_Review_Qty_Validation() throws Exception{
		
		try{
			
			//setup driver,properties and includes
			Properties pr = Config.properties(); //create a method for the pcm.properies
			Config.setup(pr.getProperty("TEST_HOST"));
			env = sys.getProperty("pcmHost"); //prod or stage
			
			PDP pdp = new PDP();
			email = pr.getProperty("CHECKOUT_USER_TAX_EMAIL_" + env);
			password = pr.getProperty("CHECKOUT_USER_PASSWORD");
			sku = pr.getProperty("SEARCH_SKU_DFLT");
			qty = "1";
			
			//Login user via header
			SignIn.login(Config.driver,email,password);
			testStatus = verifyXPath.isfound(Config.driver,pr.getProperty("HEADER_LINK_SIGNOUT_XPATH"));
			Assert.assertTrue("User is logged in (Sign out link is display)",testStatus);
			
			//Search sku and add to cart
			Cart.clearcart(Config.driver);
			Search.keyword(Config.driver, sku);
			Search.addtocart(Config.driver, sku, qty);
			
			//get the edpno in PDP
			edpno = pdp.getEDPNo(Config.driver,sku);
			
			//Go to cart and Proceed to Checkout.
			Cart.navigate(Config.driver);
			itemid = Cart.getItemID(Config.driver, edpno);
			
			//Enter a invalid quantity from the list in declared in a property
			String[] orderQty = pr.getProperty("CHECKOUT_INVALIDQTY_LIST").split(",");
			int ctr = orderQty.length;
			for(int i = 0; i < ctr; i++ ){
			
				Cart.proceedtocheckout(Config.driver);	
				Checkout.updateOrderItemQTY(Config.driver, itemid, orderQty[i]);
				testStatus = verifyXPath.isfound(Config.driver, pr.getProperty("CHECKOUT_INVALID_ERRMSG_ORQTY_XPATH"));
				
				Assert.assertTrue("[CHECKOUT] Invalid quantity validation is successful.", testStatus);
				StatusLog.printlnPassedResult(Config.driver,"[PCM] Verify Order Review Qty Validation");
				
				Checkout.returntoCart(Config.driver);				
				
			}
			
		} catch (Exception e){
			StatusLog.printlnFailedResult(Config.driver,"[PCM] Verify Order Review Qty Validation");
			System.out.println(e);
		}
	}
	

	@AfterClass
	public static void quit() throws IOException{
		TakeScreenShot.CurrentPage(Config.driver, "Last Page Test Result");
		Config.driver.close();
		Config.driver.quit();
		
	}
	
	
}
