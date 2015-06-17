package com.pcm.tests;

import java.io.IOException;
import java.util.Properties;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Test;

import com.pcm.engine.Config;
import com.pcm.includes.NikeStore;
import com.pcm.request.ClickElement;
import com.pcm.utility.SendMail;
import com.pcm.utility.TableContainer;
import com.pcm.utility.TakeScreenShot;
import com.pcm.verify.VerifyText;
import com.pcm.verify.verifyXPath;



public class TwitterSearchNikeStoreProductLink {

	public String keyword;
	public String size;
	public String qty;
	public String email;
	public String password;
	public String expandedURL;
	public String txtFileName;
	
	
	Properties sys = System.getProperties();
	
	@Test
	public void Twitter_Search_Nike_Store_Product_Link() throws Exception{
		
		try{
			
			//setup driver,properties and includes
			Properties pr = Config.properties(); //create a method for the pcm.properies
			Config.setup(pr.getProperty("TEST_HOST"));
	
			//Close the Sign in modal
			
			if(verifyXPath.isfound(Config.driver, pr.getProperty("TW_BTN_CLOSE_MODAL_XPATH"))){
				ClickElement.byXPath(Config.driver, pr.getProperty("TW_BTN_CLOSE_MODAL_XPATH"));
			}
			
			
			
			//Get the data in the excel and quick add the skus
			int rowCtr = TableContainer.getRowCount();
			
			for(int i = 1; i <= rowCtr; i++){
				
				keyword = TableContainer.getCellValue(i, "keyword");
				size = TableContainer.getCellValue(i, "size");
				qty = TableContainer.getCellValue(i, "qty");
				email = TableContainer.getCellValue(i, "email");
				password = TableContainer.getCellValue(i, "password");
				
				//int twCount = verifyXPath.count(Config.driver, pr.getProperty("TW_LINK"));
				
				for(int t = 1; t <= 8; t++){
					expandedURL = verifyXPath.getText(Config.driver, "(" + pr.getProperty("TW_LINK") + ") [position()=" + t + "]");
					System.out.println(expandedURL);
					ClickElement.byXPath(Config.driver,"//span[text()='" + expandedURL + "']");
					String parentWindow = Config.driver.getWindowHandle();
					
					//go to the new window.
					for(String windHandle : Config.driver.getWindowHandles()){
						if(!parentWindow.equals(windHandle)){
							
							//switch to the nike store
							Config.driver.switchTo().window(windHandle);
							System.out.println(Config.driver.getTitle());
							
							//String itemTitle = verifyXPath.getText(Config.driver, pr.getProperty("LBL_ITEMTITLE_XPATH"));
							
							Boolean keywordFound = VerifyText.isFound(Config.driver,keyword);
							
							if(keywordFound){
								//NikeStore.addToCart(Config.driver,size,qty,email,password);
								String curURL = NikeStore.storeCurrentURL(Config.driver);

								if(!curURL.isEmpty()){
									String emailSubject = keyword + " link";
									System.out.println("[EMAIL] Add to cart button found. Sending email for the url...");
									SendMail.sendEmail(emailSubject,curURL);
								}
								
							} //end if
							
							
							//Close the child window
							Config.driver.close();
							
							//Switch to the parent window
							Config.driver.switchTo().window(parentWindow);
							System.out.println(Config.driver.getTitle());
							break;
						} //end if
					}//end for windHandle
					
					
					
				
				} //end for i
			} //end Table Container for
			
		
		} catch (Exception e){
			
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
