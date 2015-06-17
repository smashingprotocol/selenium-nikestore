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



public class VerifyOrderCouponCodePriceSubTotal {

	public String sku;
	public String couponCode;
	public String withSlashed;
	public String withMIR;
	public String finalPrice;
	public Float floatFinalPrice = Float.valueOf("0.00");
	public String qty;
	public String email;
	public String password;
	public String env;
	public boolean testStatus;
	public String edpno;
	public String cartOrderTotal;
	public String cartOrderTotalwithCoupon;
	
	Properties sys = System.getProperties();
	
	@Test
	public void Verify_Order_Coupon_Code_Price_SubTotal() throws Exception{
		
		try{
			
			//setup driver,properties and includes
			Properties pr = Config.properties(); //create a method for the pcm.properies
			Config.setup(pr.getProperty("TEST_HOST"));
			env = sys.getProperty("pcmHost"); //prod or stage
			
			//Define properties
			qty = "1";
			email = pr.getProperty("CHECKOUT_USER_EMAIL_" + env);
			password = pr.getProperty("CHECKOUT_USER_PASSWORD");
			sku = pr.getProperty("SEARCH_SKU_PROMOCODE");
			couponCode = pr.getProperty("CART_COUPONCODE");
				
			//Clear cart and search and add to cart the test sku
			Cart.clearcart(Config.driver);
			Cart.quickaddsku(Config.driver, sku);

			
			//Store the Cart Order Total before Coupon
			cartOrderTotal = verifyXPath.getText(Config.driver,pr.getProperty("CART_LABEL_ORDERTOTAL_XPATH"));
			cartOrderTotal = cartOrderTotal.replace("$","");
			
			Cart.couponCodeSubmit(Config.driver, couponCode);
			Long wait = Long.parseLong(pr.getProperty("WAIT_SEC"));
			Thread.sleep(wait);
			
			//Store the Cart Order Total after Coupon
			cartOrderTotalwithCoupon = verifyXPath.getText(Config.driver,pr.getProperty("CART_LABEL_ORDERTOTAL_XPATH"));
			cartOrderTotalwithCoupon = cartOrderTotalwithCoupon.replace("$","");
				
			//Proceed to Guest Checkout and Verify Order Review Subtotal
			Cart.guestCheckout(Config.driver);
			testStatus = verifyXPath.isfound(Config.driver,"//p[@id='orderReviewSubTotal' and contains(text(),'" + cartOrderTotal + "')]");  //Verify the Order review sub total is equivalent to the Cart.
			Assert.assertFalse("[CHECKOUT] Order Review Sub Total of Guest is not the cart's Sub Total without Promo.",testStatus);
			
			testStatus = verifyXPath.isfound(Config.driver,"//p[@id='orderReviewSubTotal' and contains(text(),'" + cartOrderTotalwithCoupon + "')]");  //Verify the Order review sub total is equivalent to the Cart.
			Assert.assertTrue("[CHECKOUT] Order Review Sub Total of Guest is the cart's Sub Total with Promo code.",testStatus);
			
			StatusLog.printlnPassedResult(Config.driver,"[PCM] Verify Order with Coupon Code Price SubTotal for Guest Checkout");
			
			//Proceed to New Customer Checkout and Verify Order Review Subtotal
			ClickElement.byXPath(Config.driver, pr.getProperty("CHECKOUT_LINK_CREATEACT_XPATH"));
			
			testStatus = verifyXPath.isfound(Config.driver,"//p[@id='orderReviewSubTotal' and contains(text(),'" + cartOrderTotal + "')]");  //Verify the Order review sub total is equivalent to the Cart.
			Assert.assertFalse("[CHECKOUT] Order Review Sub Total of New Customer is not equals to the cart's without Promo.",testStatus);
			
			testStatus = verifyXPath.isfound(Config.driver,"//p[@id='orderReviewSubTotal' and contains(text(),'" + cartOrderTotalwithCoupon + "')]");  //Verify the Order review sub total is equivalent to the Cart.
			Assert.assertTrue("[CHECKOUT] Order Review Sub Total of New Customer is not equals to the cart's Sub Total with Promo Code.",testStatus);
	
			StatusLog.printlnPassedResult(Config.driver,"[PCM] Verify Order with Coupon Code Price SubTotal for New Customer.");
			
		} catch (Exception e){
			StatusLog.printlnFailedResult(Config.driver,"[PCM] Verify Order with Coupon Code Price SubTotal.");
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
