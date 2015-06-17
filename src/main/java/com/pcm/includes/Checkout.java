package com.pcm.includes;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

import junit.framework.Assert;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.pcm.form.SetInputField;
import com.pcm.form.SetSelectField;
import com.pcm.request.ClickElement;
import com.pcm.utility.TableContainer;
import com.pcm.verify.verifyXPath;

public class Checkout {

	public static Properties properties;
	public static String shipMtdPriceTxt;
	public static String shipMtdName;
	
	public static void changeShipAddbyZipcode(WebDriver driver, String zipcode) throws Exception {
		
		FileReader reader = new FileReader("pcm.properties");
		properties = new Properties();
		
		try{
			properties.load(reader);
			
			//If the zipcode is not yet selected as the shipping address, change the address.
			if(!verifyXPath.isfound(driver, "//div[@class='shippingAddCon']//div[contains(text(),'" + zipcode + "')]")){
				
				// TODO Auto-generated method stub
				ClickElement.byXPath(driver,properties.getProperty("CHECKOUT_LINK_CHANGESHIP_XPATH")); //click change to go to Address book
				verifyXPath.isfound(driver, properties.getProperty("CHECKOUT_BTN_ADDSHIPADD_XPATH")); //Verify the Add a Shipping Address button in Address Book
				
				int xpathctr = verifyXPath.count(driver, properties.getProperty("CHECKOUT_LINK_ADDBOOKEDIT_XPATH")) + 1; //Count the number of Edit Link in Address Book to get the no of addresses.
				System.out.println("[CHECKOUT] " + xpathctr + " other Address found in Shipping Address Book");
				
				boolean flag = false;
					System.out.println("[CHECKOUT] Looking for address with zipcode: " + zipcode + "...");
					for(int i = 1; i<xpathctr; i++){	
						flag = verifyXPath.isfound(driver, "(//div[@id='shipInfoCont']//div[@class='selectedAddress' or @class='shippingAddressBlock']) [position()=" + i + "]//p[contains(text(),'" + zipcode + "')]");  //Looking for the Address block that containts the text zipcode
						
						if(flag){
							String shipid = verifyXPath.getAttributeValue(driver,"(//a[@class='fBold btnEditAddress']) [position()=" + i + "]","shipid");  //Get the shipid of the Ship to Address button.
							System.out.println("//a[@class='buttonShipToAddress' and @shipid='" + shipid + "']");
							
							ClickElement.byXPath(driver,"//a[@class='buttonShipToAddress' and @shipid='" + shipid + "']"); //Click the Ship to Address that matches the zipcode
							break;
						} //end if
						
					} // end for
				
			} //end if
			
		}catch (Exception e){
			Assert.fail("[CHECKOUT] Change Shipping Address was not successful.");
			e.printStackTrace();
			
		}
		

	}

	public static void updateOrderItemQTY(WebDriver driver, String itemid, String qty) throws FileNotFoundException {

		FileReader reader = new FileReader("pcm.properties");
		properties = new Properties();
		
		try{
			properties.load(reader);
			//wait = Long.parseLong(properties.getProperty("WAIT_SEC"));
			//Thread.sleep(wait);
			
			ClickElement.byXPath(driver, properties.getProperty("CHECKOUT_LINK_EDITCART_XPATH"));  //Click the Edit your cart link in Order Review
			SetInputField.byXPath(driver, properties.getProperty("CHECKOUT_INPUT_QTY_XPATH"), qty); //Enter the qty value in the Order Review
			System.out.println("[CHECKOUT] Updating qty for itemid: " + itemid);
			ClickElement.byXPath(driver, properties.getProperty("CHECKOUT_LINK_UPDATEQTY_XPATH"));  //Click the Update link in Order Review
			
			
		} catch(Exception e) {
			Assert.fail("[CHECKOUT] Unable to Edit your Cart in Order Review: " + e.getMessage());
			
		}
	}

	public static void calculateShipMethodAll(WebDriver driver,boolean withFreeShipping) throws FileNotFoundException {
		
		FileReader reader = new FileReader("pcm.properties");
		properties = new Properties();
		
		try{
			properties.load(reader);
			Long wait = Long.parseLong(properties.getProperty("WAIT_SEC"));
			
			int shipmtdctr = verifyXPath.count(driver, properties.getProperty("CHECKOUT_RADIO_SHIPMTD_XPATH")) + 1; //Count the number of Shipping Method Radio Buttons.
			System.out.println("[CHECKOUT] " + shipmtdctr + " Shipping method found");

			for(int i = 1; i<shipmtdctr; i++){
				
				//Get the Value attribute of the radio button.
				String optionValue = verifyXPath.getAttributeValue(driver, "("+ properties.getProperty("CHECKOUT_RADIO_SHIPMTD_XPATH") + ") [position()=" + i + "]", "value"); 
				ClickElement.byXPath(driver, "("+ properties.getProperty("CHECKOUT_RADIO_SHIPMTD_XPATH") + ") [position()=" + i + "]"); //Select the radio button.
	
				//Store the Price attribute of the selected shipping mtd checkbox.
				shipMtdPriceTxt = verifyXPath.getAttributeValue(driver, "//label[@id='" + optionValue + "_price']","price"); 
				
				//Get the Shipping Fee in Order Review
				
				Thread.sleep(wait);
				//
				//wait.until(ExpectedConditions.textToBePresentInElementValue(properties.getProperty("CHECKOUT_LABEL_ORSHIPFEE_XPATH"), "$ " + shipMtdPriceTxt));
				String verifyOrderShipFee = verifyXPath.getText(driver, properties.getProperty("CHECKOUT_LABEL_ORSHIPFEE_XPATH")).replaceAll("[$, ]", "");
				
				
				
				
				if(shipMtdPriceTxt.equals("0") && withFreeShipping){
					shipMtdPriceTxt = "0.00";
				}
				
				System.out.println("Selected Shipping method price is: " + shipMtdPriceTxt);
				System.out.println("Shipping Fee in Order Review: " + verifyOrderShipFee);		
				
				Assert.assertEquals(shipMtdPriceTxt,verifyOrderShipFee);
				
			}
			
		}catch (Exception e){
			Assert.fail("[CHECKOUT] Error in Calculating Shipping Method" + e.getMessage());
		}
		
	}

	public static void returntoCart(WebDriver driver) throws FileNotFoundException {
		
		FileReader reader = new FileReader("pcm.properties");
		properties = new Properties();
		
		//Go back to Shopping Cart
		try {
			properties.load(reader);
			ClickElement.byXPath(driver,properties.getProperty("CHECKOUT_LINK_BACKTOCART_XPATH"));
			
		} catch (Exception e) {

			Assert.fail("[CHECKOUT] Error in going back to cart." + e.getMessage());
		}
		
	}

	public static boolean verifyShipMethodAvailability(WebDriver driver, String shipMtd) throws FileNotFoundException {
		
		FileReader reader = new FileReader("pcm.properties");
		properties = new Properties();
		boolean flag;
		
		try{
			properties.load(reader);
			shipMtdName = properties.getProperty(shipMtd);
			
			flag = verifyXPath.isfound(driver, "//label[contains(@for,'shippingMethod')]/span[contains(text(),'" + shipMtdName + "')]");
			return flag;
			
		} catch (Exception e){
			return false;
		}
		
	
		
	}

	public static void enterCreditCardExisting(WebDriver driver, String cardNo,
			String exMonth, String exYear, String securityCode) throws Exception {
		
		FileReader reader = new FileReader("pcm.properties");
		properties = new Properties();
		
		//Wait
		//WebDriverWait wait = new WebDriverWait(driver, 10);
		
		
		try {
			properties.load(reader);
			
			String cardXPath = properties.getProperty("CHECKOUT_INPUT_CARDNO_XPATH");
			String exMonthXPath = properties.getProperty("CHECKOUT_SELECT_CCMONTHSELECT_XPATH");
			String exYearXPath  = properties.getProperty("CHECKOUT_SELECT_CCYEARSELECT_XPATH");
			String ccCodeXPath = properties.getProperty("CHECKOUT_INPUT_SECCODE_XPATH");
			
			SetInputField.byXPath(driver, cardXPath , cardNo);
			
			//wait
			//WebElement element = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("id('paymentExpirationMonth_12')")));
			//element.click();
			
			//make the select Month field visible and select a month.
			WebElement monthSelecty = driver.findElement(By.xpath(exMonthXPath));
			JavascriptExecutor jse=(JavascriptExecutor) driver;
			jse.executeScript("arguments[0].setAttribute('style', 'display: block;')",monthSelecty);
			SetSelectField.byXPath(driver, exMonthXPath, exMonth);
			
			//make the select Year field visible and select a year.
			WebElement yearSelecty = driver.findElement(By.xpath(exYearXPath));
			JavascriptExecutor jsy=(JavascriptExecutor) driver;
			jsy.executeScript("arguments[0].setAttribute('style', 'display: block;')",yearSelecty);
			SetSelectField.byXPath(driver, exYearXPath , exYear);
			
			SetInputField.byXPath(driver, ccCodeXPath , securityCode);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Assert.fail("[CHECKOUT] Error in entering a Credit Card Information." + e.getMessage());
		} //end catch
		
	} // end enterCreditCardExisting

	public static void placeOrderAccept(WebDriver driver) throws Exception {
		
		FileReader reader = new FileReader("pcm.properties");
		properties = new Properties();
		
		try {
			properties.load(reader);
			
			ClickElement.byXPath(driver, properties.getProperty("CHECKOUT_RADIO_PLACEORDERTERMS_XPATH"));
			ClickElement.byXPath(driver, properties.getProperty("CHECKOUT_BTN_PLACEYOURORDER_XPATH"));
			
		} catch (IOException e) {
			Assert.fail("[CHECKOUT] Error in accepting terms and clicking Place Order button." + e.getMessage());
		} //end catch
		
		
	}//end placeOrderAccept

	public static void deleteItem(WebDriver driver, String sku) throws FileNotFoundException {
		// TODO Auto-generated method stub
		FileReader reader = new FileReader("pcm.properties");
		properties = new Properties();
		
		try{
			properties.load(reader);
			
			//GET THE NUMBER OF ITEM ROW
			int itemCtr = verifyXPath.count(driver, properties.getProperty("CHECKOUT_ORDER_ITEMROW_XPATH"));
			ClickElement.byXPath(driver, properties.getProperty("CHECKOUT_LINK_EDITCART_XPATH")); 
			
			for(int i = 1; i <= itemCtr; i++){
				Boolean itemRowFound = verifyXPath.isfound(driver, "(" + properties.getProperty("CHECKOUT_ORDER_ITEMROW_XPATH") + ") [position()=" + i + "]//span[contains(text(),'" + sku + "')]");
				if(itemRowFound){
					
					ClickElement.byXPath(driver, "(" + properties.getProperty("CHECKOUT_ORDER_ITEMROW_XPATH") + ") [position()=" + i + "]"+ properties.getProperty("CHECKOUT_LINK_ORDER_DELETE_ITEM_XPATH") +"");
					break;
				} //end if
			} // end for
			
		} catch (Exception e){
			Assert.fail("[CHECKOUT] Error in deleting the item." + e.getMessage());
		} //end catch
		
	} //end deleteItem

	public static void enterNewCustomerBillAdd(WebDriver driver, ArrayList<String> billFields) throws FileNotFoundException {
		
		FileReader reader = new FileReader("pcm.properties");
		properties = new Properties();
		
		try{
			properties.load(reader);
			
			SetInputField.byXPath(driver, properties.getProperty("CHECKOUT_INPUT_BILLEMAIL_XPATH"), billFields.get(0));
			SetInputField.byXPath(driver, properties.getProperty("CHECKOUT_INPUT_BILLCONFIRMEMAIL_XPATH"), billFields.get(1));
			SetInputField.byXPath(driver, properties.getProperty("CHECKOUT_INPUT_BILLPSWD_XPATH"), billFields.get(2));
			SetInputField.byXPath(driver, properties.getProperty("CHECKOUT_INPUT_BILLCONFIRMPSWD_XPATH"), billFields.get(3));
			SetInputField.byXPath(driver, properties.getProperty("CHECKOUT_INPUT_BILLFIRSTNAME_XPATH"), billFields.get(4));
			SetInputField.byXPath(driver, properties.getProperty("CHECKOUT_INPUT_BILLLASTNAME_XPATH"), billFields.get(5));
			SetInputField.byXPath(driver, properties.getProperty("CHECKOUT_INPUT_BILLCO_XPATH"), billFields.get(6));
			SetInputField.byXPath(driver, properties.getProperty("CHECKOUT_INPUT_BILLADD1_XPATH"), billFields.get(7));
			SetInputField.byXPath(driver, properties.getProperty("CHECKOUT_INPUT_BILLADD2_XPATH"), billFields.get(8));
			SetInputField.byXPath(driver, properties.getProperty("CHECKOUT_INPUT_BILLZIP_XPATH"), billFields.get(9));
			SetInputField.byXPath(driver, properties.getProperty("CHECKOUT_INPUT_BILLPHONE_XPATH"), billFields.get(10));
			SetInputField.byXPath(driver, properties.getProperty("CHECKOUT_INPUT_BILLMOBILE_XPATH"), billFields.get(11));
				
		} catch(Exception e){
			
		} //end try
		
	} //end enterNewCustomerFormTableContainer

	
		

}
