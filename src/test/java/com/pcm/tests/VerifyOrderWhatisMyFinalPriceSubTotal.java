package com.pcm.tests;

import java.io.IOException;
import java.text.DecimalFormat;
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
import com.pcm.utility.StringFormatter;
import com.pcm.utility.TableContainer;
import com.pcm.utility.TakeScreenShot;
import com.pcm.verify.verifyXPath;



public class VerifyOrderWhatisMyFinalPriceSubTotal {

	public String sku;
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
	
	
	
	Properties sys = System.getProperties();
	
	
	@Test
	public void Verify_Order_What_is_My_Final_Price_SubTotal() throws Exception{
		
		try{
			
			//setup driver,properties and includes
			Properties pr = Config.properties(); //create a method for the pcm.properies
			Config.setup(pr.getProperty("TEST_HOST"));
			env = sys.getProperty("pcmHost"); //prod or stage
	
			//Define properties
			qty = "1";
			email = pr.getProperty("CHECKOUT_USER_EMAIL_" + env);
			password = pr.getProperty("CHECKOUT_USER_PASSWORD");
				
			//Clear the cart first.
			Cart.clearcart(Config.driver);
			
	
			//Get the data in the excel and quick add the skus
			int rowCtr = TableContainer.getRowCount();
			
			
			for(int i = 1; i <= rowCtr; i++){
				sku = TableContainer.getCellValue(i, "sku");
				withSlashed = TableContainer.getCellValue(i, "withSlashed");
				withMIR = TableContainer.getCellValue(i, "withMIR");
				finalPrice = TableContainer.getCellValue(i, "finalPrice");
				
				floatFinalPrice = floatFinalPrice + Float.valueOf(finalPrice);
				
				
				Cart.quickaddsku(Config.driver, sku);		
			} //end for
			
			//convert the Final Price total from float to double to convert to have a format of 0,000.00
			String formatFinalPrice = StringFormatter.formatFloattoCommaDecimalNumString(floatFinalPrice);
			
			//Store the Cart Order Total
			cartOrderTotal = verifyXPath.getText(Config.driver,pr.getProperty("CART_LABEL_ORDERTOTAL_XPATH"));
			cartOrderTotal = cartOrderTotal.replace("$","");
			
			//Proceed to Guest Checkout and Verify Order Review Subtotal
			Cart.guestCheckout(Config.driver);
			testStatus = verifyXPath.isfound(Config.driver,"//p[@id='orderReviewSubTotal' and contains(text(),'" + cartOrderTotal + "')]");  //Verify the Order review sub total is equivalent to the Cart.
			Assert.assertFalse("[CHECKOUT] Order Review Sub Total of Guest is not the cart's Sub Total.",testStatus);
	
			testStatus = verifyXPath.isfound(Config.driver,"//p[@id='orderReviewSubTotal' and contains(text(),'" + formatFinalPrice + "')]");  //Verify the Order review sub total is equivalent to the Cart.
			Assert.assertTrue("[CHECKOUT] Order Review Sub Total of Guest is the total Final Price of the items.",testStatus);
			StatusLog.printlnPassedResult(Config.driver,"[PCM] Verify Order What is My Final Price SubTotal for Guest Checkout");
			
			//Proceed to New Customer Checkout and Verify Order Review Subtotal
			ClickElement.byXPath(Config.driver, pr.getProperty("CHECKOUT_LINK_CREATEACT_XPATH"));
			testStatus = verifyXPath.isfound(Config.driver,"//p[@id='orderReviewSubTotal' and contains(text(),'" + cartOrderTotal + "')]");  //Verify the Order review sub total is equivalent to the Cart.
			Assert.assertFalse("[CHECKOUT] Order Review Sub Total of New Customer is not equals to the cart's Sub Total.",testStatus);
	
			testStatus = verifyXPath.isfound(Config.driver,"//p[@id='orderReviewSubTotal' and contains(text(),'" + formatFinalPrice + "')]");  //Verify the Order review sub total is equivalent to the Cart.
			Assert.assertTrue("[CHECKOUT] Order Review Sub Total of New Customer is the total Final Price of the items.",testStatus);
			
			StatusLog.printlnPassedResult(Config.driver,"[PCM] Verify Order What is My Final Price SubTotal for New Customer");
		
		} catch (Exception e){
			StatusLog.printlnFailedResult(Config.driver,"[PCM] Verify Order What is My Final Price SubTotal");
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
