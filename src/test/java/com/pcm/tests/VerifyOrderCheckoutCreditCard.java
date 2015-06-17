package com.pcm.tests;

import java.io.IOException;
import java.util.Properties;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.pcm.engine.Config;
import com.pcm.includes.Cart;
import com.pcm.includes.Checkout;
import com.pcm.includes.PDP;
import com.pcm.includes.Search;
import com.pcm.includes.SignIn;
import com.pcm.request.ClickElement;
import com.pcm.utility.TableContainer;
import com.pcm.utility.TakeScreenShot;
import com.pcm.verify.verifyXPath;

public class VerifyOrderCheckoutCreditCard {

	public static Config config; //Initialized the config method.//Initialized the driver.
	public static Search search;
	public SignIn signin;//Initialized to include Search class
	public static Checkout checkout; //Initialized Checkout class
	public Cart cart;//Initialized to include Search class
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
	public String cardNo;
	public String exMonth;
	public String exYear;
	public String securityCode;
	
	
	
	
	Properties sys = System.getProperties(); //Get property values in the build.xml junit tagged.
	
	
	@Test
	public void Verify_Order_Checkout_CreditCard() throws Exception{
		
		try{
			
			//setup driver,properties and includes
			config = new Config();
			Properties pr = config.properties(); //create a method for the pcm.properies
			config.setup(pr.getProperty("TEST_HOST"));
			env = sys.getProperty("pcmHost"); //prod or stage
			
			PDP pdp = new PDP();
			search = new Search();
			cart = new Cart();
			email = pr.getProperty("CHECKOUT_USER_TAX_EMAIL_" + env);
			password = pr.getProperty("CHECKOUT_USER_PASSWORD");
			sku = pr.getProperty("SEARCH_SKU_DFLT");
			qty = "1";
			cardNo = pr.getProperty("CHECKOUT_CCNO_VISA");
			exMonth = pr.getProperty("CHECKOUT_EXPMONTH");
			exYear = pr.getProperty("CHECKOUT_EXPYEAR");
			securityCode = pr.getProperty("CHECKOUT_SCODE_VISA");
			
			//Login user via header
			SignIn.login(config.driver,email,password);
			testStatus = verifyXPath.isfound(config.driver,pr.getProperty("HEADER_LINK_SIGNOUT_XPATH"));
			Assert.assertTrue("User is logged in (Sign out link is display)",testStatus);
			
			//Search sku and add to cart
			//cart.clearcart(config.driver);
			search.keyword(config.driver, sku);
			search.addtocart(config.driver, sku, qty);
			
			
			//Go to cart and Proceed to checkout.
			cart.navigate(config.driver);
			cart.proceedtocheckout(config.driver);
			
			Checkout.enterCreditCardExisting(config.driver,cardNo,exMonth,exYear,securityCode);
			Checkout.placeOrderAccept(config.driver);
			
			String OrderNumber = verifyXPath.getText(config.driver, pr.getProperty("CHECKOUT_ORDERCONFIRMATION_ORNO_XPATH"));
			System.out.println("[PCM] Verify Order Checkout CreditCard with Order Number: " + OrderNumber + " ..PASSED");
			
			
		} catch (Exception e){
			System.out.println(e);
		}
	}
	

	@AfterClass
	public static void quit() throws IOException{
		TakeScreenShot.CurrentPage(Config.driver, "Last Page Test Result");
		config.driver.close();
		config.driver.quit();
		
	}
	
	
}
