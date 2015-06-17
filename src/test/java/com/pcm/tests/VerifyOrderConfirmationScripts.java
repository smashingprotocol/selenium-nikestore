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

public class VerifyOrderConfirmationScripts {

	public String email;
	public String password;
	public String env;
	public boolean testStatus;
	public String edpno;
	public String cartOrderTotal;
	public boolean scriptsFound;
	public String sku;
	public String qty;
	public String zipCode;
	public String itemid;
	public String cardNo;
	public String exMonth;
	public String exYear;
	public String securityCode;
	
	
	
	
	Properties sys = System.getProperties(); //Get property values in the build.xml junit tagged.
	
	
	@Test
	public void Verify_Order_Confirmation_Scripts() throws Exception{
		
		try{
			
			//setup driver,properties and includes
			Properties pr = Config.properties(); //create a method for the pcm.properies
			Config.setup(pr.getProperty("TEST_HOST"));
			env = sys.getProperty("pcmHost"); //prod or stage
			
			email = pr.getProperty("CHECKOUT_USER_TAX_EMAIL_" + env);
			password = pr.getProperty("CHECKOUT_USER_PASSWORD");
			sku = pr.getProperty("SEARCH_SKU_DFLT");
			qty = "1";
			cardNo = pr.getProperty("CHECKOUT_CCNO_VISA");
			exMonth = pr.getProperty("CHECKOUT_EXPMONTH");
			exYear = pr.getProperty("CHECKOUT_EXPYEAR");
			securityCode = pr.getProperty("CHECKOUT_SCODE_VISA");
			
			//Login user via header
			SignIn.login(Config.driver,email,password);
			testStatus = verifyXPath.isfound(Config.driver,pr.getProperty("HEADER_LINK_SIGNOUT_XPATH"));
			Assert.assertTrue("User is logged in (Sign out link is display)",testStatus);
			
			//Search sku and add to cart
			//Cart.clearcart(Config.driver);
			Search.keyword(Config.driver, sku);
			Search.addtocart(Config.driver, sku, qty);
			
			//Go to cart and Proceed to checkout.
			Cart.navigate(Config.driver);
			//Get the Cart Order Total
			cartOrderTotal = verifyXPath.getText(Config.driver,pr.getProperty("CART_LABEL_ORDERTOTAL_XPATH"));
			cartOrderTotal = cartOrderTotal.replaceAll("[$, ]", "");
			
			Cart.proceedtocheckout(Config.driver);
			
			Checkout.enterCreditCardExisting(Config.driver,cardNo,exMonth,exYear,securityCode);
			Checkout.placeOrderAccept(Config.driver);
			
			//close the shopper approved modal
			ClickElement.byXPath(Config.driver, pr.getProperty("CHECKOUT_BTN_SACLOSEBTN_XPATH"));
			
			//Check the CJ Tracking script xpath in Order Confirmation
			scriptsFound = verifyXPath.isfound(Config.driver, pr.getProperty("CHECKOUT_ORDERCONFIRMATION_CJTRACKING_XPATH"));
			Assert.assertTrue("[CHECKOUT] CJ Tracking scripts is found in Order Confirmation.",scriptsFound);
			
			//Check the GTS Tracking script xpath in Order Confirmation
			scriptsFound = verifyXPath.isfound(Config.driver, pr.getProperty("CHECKOUT_ORDERCONFIRMATION_GTS_XPATH"));
			Assert.assertTrue("[CHECKOUT] GTS scripts is found in Order Confirmation.",scriptsFound);
			
			String OrderNumber = verifyXPath.getText(Config.driver, pr.getProperty("CHECKOUT_ORDERCONFIRMATION_ORNO_XPATH"));

			StatusLog.printlnPassedResult(Config.driver,"[PCM] Verify Order Confirmation Scripts: " + OrderNumber);
			
		} catch (Exception e){
			StatusLog.printlnFailedResult(Config.driver,"[PCM] Verify Order Confirmation Scripts: ");
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
