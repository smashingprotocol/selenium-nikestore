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
import com.pcm.utility.TakeScreenShot;
import com.pcm.verify.verifyXPath;



public class VerifyOrderRebatesPriceSubTotalUpdateQty {

	public static Config config; //Initialized the config method.//Initialized the driver.
	public static Search search;
	public static SignIn signin;//Initialized to include Search class
	public Checkout checkout; //Initialized Checkout class
	public Cart cart;//Initialized to include Search class
	public String sku;
	public String qty;
	public String email;
	public String password;
	public String env;
	public boolean testStatus;
	public String edpno;
	public String cartOrderTotal;
	
	
	
	Properties sys = System.getProperties();
	
	
	@Test
	public void Verify_Order_Rebates_Price_SubTotal_Update_Qty() throws Exception{
		
		try{
			
			//setup driver,properties and includes
			config = new Config();
			Properties pr = config.properties(); //create a method for the pcm.properies
			config.setup(pr.getProperty("TEST_HOST"));
			env = sys.getProperty("pcmHost"); //prod or stage
			search = new Search();
			cart = new Cart();
			
			//set test data
			sku = pr.getProperty("SEARCH_SKU_INSTANTREBATES");
			qty = "1";
			email = pr.getProperty("CHECKOUT_USER_EMAIL_" + env);
			password = pr.getProperty("CHECKOUT_USER_PASSWORD");
			
			//Login user via header
			signin.login(config.driver,email,password);
			testStatus = verifyXPath.isfound(config.driver,pr.getProperty("HEADER_LINK_SIGNOUT_XPATH"));
			Assert.assertTrue("User is logged in (Sign out link is display)",testStatus);
			
			//Clear the cart first.
			cart.clearcart(config.driver);
			
			//Search for the Sku to checkout and add to cart
			search.keyword(config.driver,sku);
			search.addtocart(config.driver,sku,qty);
			System.out.println("[CART] " + config.driver.getTitle());
			
			//Go to PDP to get the EDPNo of the item
			ClickElement.byXPath(config.driver, pr.getProperty("SEARCH_LINK_PDP_XPATH"));
			edpno = verifyXPath.getAttributeValue(config.driver, pr.getProperty("PDP_INPUT_EDPNO_XPATH"), "value");
			
			//Navigate in Shopping Cart
			ClickElement.byXPath(config.driver,pr.getProperty("CART_LINK_XPATH"));  //Go to Shopping Cart.
			System.out.println("[CART] " + config.driver.getTitle());
			testStatus = verifyXPath.isfound(config.driver,pr.getProperty("CART_INPUT_QTY_XPATH"));  //Verify the qty is displayed on cart, it means cart is not empty.
			Assert.assertTrue("Shopping Cart is not Empty",testStatus);
			
			//Get the itemid by edp of the sku in cart:
			String itemid = cart.getItemID(config.driver, edpno);
			System.out.println(itemid);
			
			cartOrderTotal = verifyXPath.getText(config.driver,pr.getProperty("CART_LABEL_ORDERTOTAL_XPATH"));
			cartOrderTotal = cartOrderTotal.replace("$","");
			
			System.out.println("Cart Order Total: "+ cartOrderTotal);
			
			//Go to Checkout and verify the Sub Total:
			ClickElement.byXPath(config.driver,pr.getProperty("CART_BTN_CHECKOUT_XPATH"));
			testStatus = verifyXPath.isfound(config.driver,"//p[@id='orderReviewSubTotal' and contains(text(),'" + cartOrderTotal + "')]");  //Verify the Order review sub total is equivalent to the cart.
			Assert.assertTrue("[CHECKOUT] Order Review Sub Total is equals to the cart Sub Total.",testStatus);
			
			
			//Update the QTY of the item to more than 1 in Order Review
			checkout.updateOrderItemQTY(config.driver,itemid,"2");
			testStatus = verifyXPath.isfound(config.driver,pr.getProperty("CHECKOUT_LABEL_SUBTOTAL_NONEG_XPATH"));  //Verify the Order review sub total is equivalent to the cart.
			Assert.assertTrue("[CHECKOUT] Order Review Sub Total is a none negative value.",testStatus);
			
			testStatus = verifyXPath.isfound(config.driver,pr.getProperty("CHECKOUT_LABEL_SUBTOTAL_NONZERO_XPATH"));  //Verify the Order review sub total is equivalent to the cart.
			Assert.assertTrue("[CHECKOUT] Order Review Sub Total is a none zero value.",testStatus);
			
			
			//Revert the QTY of the item to 1 in Order Review
			checkout.updateOrderItemQTY(config.driver,itemid,"1");
			testStatus = verifyXPath.isfound(config.driver,pr.getProperty("CHECKOUT_LABEL_SUBTOTAL_NONEG_XPATH"));  //Verify the Order review sub total is equivalent to the cart.
			Assert.assertTrue("[CHECKOUT] Order Review Sub Total is a none negative value.",testStatus);
			
			testStatus = verifyXPath.isfound(config.driver,pr.getProperty("CHECKOUT_LABEL_SUBTOTAL_NONZERO_XPATH"));  //Verify the Order review sub total is equivalent to the cart.
			Assert.assertTrue("[CHECKOUT] Order Review Sub Total is a none zero value.",testStatus);
			
			
			
			System.out.println("[PCM] Verify Order Rebates Price SubTotal Update Qty...PASSED");
		
		} catch (Exception e){
			System.out.println("[PCM] Verify Order Rebates Price SubTotal Update Qty...FAILED");
			System.out.println(e);
			Assert.fail(e.getMessage());
		}
	}
	

	@AfterClass
	public static void quit() throws IOException{
		TakeScreenShot.CurrentPage(Config.driver, "Last Page Test Result");
		config.driver.close();
		config.driver.quit();
		
	}
	
	
}
