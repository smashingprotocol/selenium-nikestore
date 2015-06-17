package com.pcm.tests;

import java.io.IOException;
import java.util.Properties;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Test;

import com.pcm.engine.Config;
import com.pcm.includes.Cart;
import com.pcm.includes.Checkout;
import com.pcm.includes.Search;
import com.pcm.includes.SignIn;
import com.pcm.utility.StatusLog;
import com.pcm.utility.TableContainer;
import com.pcm.utility.TakeScreenShot;
import com.pcm.verify.verifyXPath;

public class VerifyOrderAvailableShippingMethodHawaii {

	public String email;
	public String password;
	public String env;
	public boolean testStatus;
	public String edpno;
	public String cartOrderTotal;
	public String verifyOrderSubtTotal;
	public static boolean isfreeshipping;
	public HSSFSheet ws;
	public String worksheet;
	public String sku;
	public String qty;
	public String zipCode;
	public String shipMtdValue;
	public String shipMtd;
	public Boolean shipMtdAvailable;
	public boolean teststatus = true;
	
	Properties sys = System.getProperties(); //Get property values in the build.xml junit tagged.
	
	
	@Test
	public void Verify_Order_Available_Shipping_Method_Hawaii() throws Exception{
		
		try{
			
			//setup driver,properties and includes
			Properties pr = Config.properties(); //create a method for the pcm.properies
			Config.setup(pr.getProperty("TEST_HOST"));
			env = sys.getProperty("pcmHost"); //prod or stage
			email = pr.getProperty("CHECKOUT_USER_HI_EMAIL_" + env);
			password = pr.getProperty("CHECKOUT_USER_PASSWORD");
			
			//Login user via header
			SignIn.login(Config.driver,email,password);
			testStatus = verifyXPath.isfound(Config.driver,pr.getProperty("HEADER_LINK_SIGNOUT_XPATH"));
			Assert.assertTrue("User is logged in (Sign out link is display)",testStatus);
			
			//Get table container value and declared as property values
			//get the number of test cases in the excel
			int rowCount = TableContainer.getRowCount();
	
			for(int i = 1; i <= rowCount; i++){
				
				sku = TableContainer.getCellValue(i,"sku");
				qty = TableContainer.getCellValue(i,"qty");
				zipCode = TableContainer.getCellValue(i,"zipCode");
				
				//Search SKU and Add to Cart.
				Cart.clearcart(Config.driver);
				Search.keyword(Config.driver, sku);
				Search.addtocart(Config.driver, sku, qty);
				
				//Proceed to checkout
				Cart.navigate(Config.driver);
				Cart.proceedtocheckout(Config.driver);
				
				//Total number of shipping methods
				int shipMtdCtr = Integer.parseInt(pr.getProperty("SHIP_MTD_COUNT"));
				System.out.println(shipMtdCtr);
				boolean ShippingMethodFound = true;
				
				
				//Verify the Shipping Method
				for(int s = 1; s <= shipMtdCtr; s++){
					
					//ship method property name
					shipMtd = "shipmethod." + String.valueOf(s);
					
					//Value of the ship method property (true,false)
					shipMtdValue = TableContainer.getCellValue(i,"isshipmethod." + String.valueOf(s));
					
					if (!shipMtdValue.isEmpty()){
						shipMtdAvailable = Boolean.valueOf(shipMtdValue);
						System.out.println("shipMtdAvailable: " + shipMtdAvailable);
						ShippingMethodFound = Checkout.verifyShipMethodAvailability(Config.driver,shipMtd);
						
						//if the shipping method found did not match the excel condition, entire test case is failed
						if(!shipMtdAvailable.equals(ShippingMethodFound)){
							System.out.println("[CHECKOUT] Shipping method found is " + shipMtdAvailable + " but result is " + ShippingMethodFound);
							teststatus = false;	
						}	
						
						System.out.println(teststatus);	
					} //end if
	
				} //end for
				
				//Assert true if no failure found.
				Assert.assertTrue("[PCM] Verify Order Available Shipping Method Hawaii..PASSED", teststatus);
				StatusLog.printlnPassedResult(Config.driver,"[PCM] Verify Order Available Shipping Method Hawaii");
				
				//Return to cart
				Checkout.returntoCart(Config.driver);
				
			} //end for
			
			
		} catch (Exception e){
			StatusLog.printlnFailedResult(Config.driver,"[PCM] Verify Order Available Shipping Method Hawaii");
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
